# Codebase Architecture

A whistle-stop tour of how the ray tracer fits together, for picking the
project back up. Everything lives in `ParallelTracer/` in the default Java
package — no subpackages to navigate.

## Pipeline (start here)

`RaytracerDriver.main()` is the entry point:

1. `SceneCreator.<sceneMethod>(...)` builds a `Scene` — a `Camera` plus lists
   of `Surface`s and `Light`s.
2. If `-v` was passed on the command line, a `PreviewWindow` is created up
   front and passed into `render`; otherwise `preview` is `null` and the
   render runs headless. (See the arg loop at the top of `main`.)
3. `Scene.render(xRes, yRes, numSamples, ambientBlur, preview)` walks every
   pixel and returns a `ColorImage`. When `preview != null`, each
   parallel-stream worker calls `preview.setPixel(x, y, color)` right after
   computing a pixel, in addition to writing it into the `ColorImage` —
   that's what makes the window fill in live instead of only appearing once
   rendering is done. `PreviewWindow` repaints itself on its own `Timer` tick
   rather than on every `setPixel` call, since re-painting per-pixel across
   hundreds of thousands of parallel calls would slow the render down badly.
   The no-preview `render(...)` overload (no `PreviewWindow` argument) is
   still there and just passes `null` through.
4. `RaytracerDriver.saveImage(...)` converts the `ColorImage` to a
   `BufferedImage` and writes it out with `ImageIO`.

## The math primitives

- **`Point`** / **`Vector`** — Point is a position, Vector is a direction with
  `add`/`subtract`/`dot`/`cross`/`normalize`/`length`. Kept as separate types
  even though they're both `(x,y,z)` triples, which is why you'll see
  `Point.subtract(Point) -> Vector` but `Vector.add(Vector) -> Vector`.
- **`Color`** — RGB as three `double`s in roughly [0,1], not clamped until
  `toARGB()`. Has `add` (light accumulation), `scale` (brightness/intensity),
  `shade` (component-wise multiply, i.e. filtering light through a surface
  color), and `tint` (blend one color toward another, used to layer
  reflection/refraction contributions on top of a base color).
  - Note: `tint`'s formula mixes up the g/b channels
    (`g + (1-b)*c.getG()`, `b + (1-g)*c.getB()` — the blend factors are
    cross-wired between channels instead of using `1-g` and `1-b`
    respectively). It's subtle because it only shows up as slightly-off color
    blending, not a crash. Worth fixing if you're touching lighting/color code.
- **`Ray`** — origin `Point` + normalized `Vector` direction + a `time` field
  (a random `[0,1)` value stamped on each ray, used for motion blur — surfaces
  that move, like `Sphere`'s `movement` vector, interpolate position by
  `ray.getTime()`).

## Scene graph

- **`Surface`** (abstract) — one method: `intersect(Ray) -> Intersection` or
  `null`. Implementations: `Sphere`, `Triangle`, `Rectangle` (two
  `Triangle`s), `Cube` (six `Rectangle`s), `Cone`, `Tube` (finite open
  cylinder), `Ring` (flat annulus/disc).
  - Composite surfaces (`Cube`) call `intersect` on their children **without**
    picking the globally-nearest hit — `Cube.intersect` returns the first face
    in a fixed order (front, left, top, bottom, right, back) that reports a
    hit, rather than comparing distances like `Scene.computeVisibleColor`
    does for top-level surfaces. For a convex opaque cube viewed from outside
    this is invisible (only one face can be hit anyway), but it's an
    inconsistency worth knowing about if you extend it to something
    non-convex or need the closest intersection specifically.
  - `Intersection` bundles the hit position, normal, distance, `Material`,
    and (for `Sphere`/`Texture`) UV coordinates for texture lookup.
- **`Material`** (abstract) — `computeLighting(Intersection, Ray, Light)`,
  plus opt-in hooks with harmless defaults: `getReflectiveness()`,
  `getDeviance()` (reflection blur / glass opacity), `isGlass()`,
  `isHollow()`. Implementations, in order of increasing capability:
  - `Lambert` — matte diffuse only.
  - `Phong` — diffuse + specular highlight (Phong reflection model).
  - `BPhong` — same idea, Blinn-Phong (half-vector) specular term instead.
  - `MirrorPhong extends Phong` — adds reflectivity + reflection blur
    ("deviance").
  - `Glass extends MirrorPhong` — adds refraction index and a hollow/solid
    flag; opacity is passed in as `1 - opacity` to the parent's deviance slot.
  - `Texture` — samples a loaded image (`ImageLoader`/`ColorImage`) using the
    intersection's UV coords instead of a flat diffuse color; only wired up
    for `Sphere` (only `Sphere.intersect` populates `imageX`/`imageY`).
- **`Light`** (abstract) — `computeLightDirection`, `computeLightColor`,
  `computeLightDistance`, plus `computeRandomPoint()` (no-op by default).
  Implementations: `PointLight` (no falloff), `SpotLight` (cone + inverse
  square falloff), `LightBulb` (area light — `computeRandomPoint` jitters the
  sample point each call, which is what produces soft shadows over many
  samples/pixel).

## Rendering math

`Scene.computeVisibleColor(Ray, bouncesLeft, ambientBounce)` is the recursive
core (path-tracer style, not a simple Whitted tracer):

1. Linear-scan all surfaces for the nearest intersection (no acceleration
   structure — no BVH/kd-tree, so scene complexity is O(n) per ray; this is
   the first thing to optimize if you add many-surface scenes).
2. For each light, cast a shadow ray (`Scene.isShadowed`) and accumulate
   lit color from `Material.computeLighting`.
3. If `ambientBounce > 0`, recurse in a random hemisphere direction for
   ambient/global-illumination-ish bounce light, weighted by the cosine
   angle — this is why ambient lighting requires many samples/pixel to look
   clean rather than grainy.
4. If the material is glass, recurse along a refracted-ish ray (approximated
   by blending the normal and view direction rather than true Snell's law
   refraction) and blend by opacity/deviance.
5. If `bouncesLeft > 0` and the material is reflective, recurse along the
   mirror-reflection ray (perturbed by `deviance` for blurry reflections).

`Scene.render` builds one `Point2D` per pixel, converts the array to a
stream, and calls `makeImage` per point — this is the "parallel" in the
project name. The core overload takes a `boolean parallel`: when `true` it
does `Arrays.stream(points).parallel()`; when `false` it renders on a plain
sequential stream. That toggle is the *only* difference between the two modes
and is what the `-c` race (see below) flips to compare them. Each pixel is
independent (writes only to its own `frame[x][y]`), so there's no shared
mutable state between stream tasks, which is exactly what parallel streams
need to be safe. `makeImage` itself loops over an
`sqrt(numSamples) x sqrt(numSamples)` grid of jittered sub-pixel samples for
anti-aliasing/motion-blur/soft-shadow accumulation, averaging the result.

**RNG and parallel scaling.** All the random sampling in the render
(`Camera` depth-of-field, `Scene`'s ambient/reflection jitter, `LightBulb`
soft-shadow points) uses `ThreadLocalRandom.current().nextDouble()`. This
matters a lot: these were originally `Math.random()`, which funnels every call
through one shared global generator. Under a parallel stream, all worker
threads then contend on that single generator's seed, which capped the speedup
at ~1.6x on 8 cores. Switching to `ThreadLocalRandom` (an independent
generator per thread) is what lets the parallel render actually scale (~5x).
**Use `ThreadLocalRandom`, never `Math.random()`, in render-path code.**

**The `-c` race (`RaytracerDriver.renderRace`).** Demonstrates the above by
rendering two independent copies of the scene *simultaneously* — one with
`parallel=false`, one with `parallel=true` — each on its own `Thread` and into
its own side-by-side `PreviewWindow`, then printing the wall-clock speedup. It
deliberately builds **two separate `Scene` instances** because `Light`s mutate
an internal random sample point (`LightBulb.randP`) during rendering, so two
renders sharing one `Scene` would race on that field. (Within a *single*
render this same field is technically raced across parallel workers too — it's
a pre-existing source of extra soft-shadow noise, harmless but worth knowing.)

## Adding a scene

`SceneCreator` is a bag of static factory methods, one per scene
(`scene1`...`scene5`, `texturesScene`, `cornellBox`); `scene1` is left in as a
commented-out worked example. To add a new one: write
`public static Scene myScene(double xRes, double yRes){ ... }` building a
`Camera`, adding `Surface`s and `Light`s to a `new Scene(cam)`, and point
`RaytracerDriver.main` at it.

## Things you might want to tackle next

- Fix `Color.tint`'s cross-wired g/b channel bug (see above).
- Add an acceleration structure (BVH/grid) — the linear surface scan in
  `computeVisibleColor`/`isShadowed` is the main scaling bottleneck for
  complex scenes.
- `RaytracerDriver` has a commented-out `for(int i = 0; i < 360; i++)` loop
  around the render — this is the leftover scaffolding for the "Movie
  Capabilities" feature mentioned in `README.md` (see `Photo Collection/Solar
  Frames` and `SOLARMOVIE.mov`) — reviving it as a proper frame-sequence
  driver (writing `frame%03d.png` per iteration) would let you regenerate
  those animations or make new ones.
