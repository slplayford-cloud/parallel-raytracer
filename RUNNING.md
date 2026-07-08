# Running the Parallel Ray Tracer

This project has no build tool (no Maven/Gradle) and no package declarations —
it's a flat folder of `.java` files in the default package, originally built
and run inside BlueJ. This guide gets it running from a plain terminal.

## 1. Install a JDK

Any JDK 17+ works (nothing in the code needs anything newer). On Arch/CachyOS:

```
sudo pacman -S --needed jdk21-openjdk
```

Verify:

```
java -version
javac -version
```

## 2. Compile

All source files live directly in `ParallelTracer/`, with no package
statement, so compile them together from inside that folder:

```
cd ParallelTracer
javac *.java
```

This produces one `.class` file per `.java` file, in the same directory.
`.class` files are gitignored — don't commit them.

## 3. Run

```
java RaytracerDriver        # headless: render straight to CornellBox.png, then exit
java RaytracerDriver -v     # also show a live preview window while rendering
java RaytracerDriver -c     # race a sequential vs a parallel render, side by side
```

By default the program renders straight to `CornellBox.png` and exits when
done — no window. Pass **`-v`** ("verbose"/visual) to open a preview window
that pops up immediately and fills in pixel-by-pixel as the parallel render
threads compute them (see `PreviewWindow.java`) — its title shows a live `%`
progress readout. **With `-v` the window stays open after the render
finishes** so you can look at the final image; the program only exits once
you close it (Swing keeps the JVM alive while a window is open). `-v`
requires a graphical display (X11/Wayland) — leave it off when running over a
plain SSH session without X forwarding.

### `-c`: the parallel-vs-sequential race demo

Pass **`-c`** to show what the parallel streams actually buy you. It renders
the *same scene twice at once* — once sequentially, once in parallel — each in
its own preview window, so you watch the parallel window race ahead and finish
first. When both finish it prints the timings and speedup, e.g.:

```
Sequential: 60.3s | Parallel: 12.5s | Speedup: 4.8x
```

Notes:
- Both renders run **at the same time**, so they share CPU cores — the numbers
  are an illustrative demo, not a rigorous benchmark (the parallel side would
  be a touch faster if it had the machine to itself).
- The race honors the resolution set at the top of `main` (currently
  1280x720). Sequential is the slow one — at 1280x720 it takes ~a minute on an
  8-core machine. **Lower the resolution** in `main` for a snappier race.
- Window placement (side by side) is a hint; a tiling window manager may
  arrange the two windows however it likes (e.g. stacked).
- Also needs a graphical display, like `-v`.

**Why the speedup is as big as it is:** the render calls the RNG many times per
ray (camera depth-of-field, ambient/reflection jitter, soft-shadow sampling).
Those were originally `Math.random()`, which routes every call through a single
shared global generator — under many parallel threads that shared generator
becomes a contention point and throttled the speedup to ~1.6x. They were
switched to `java.util.concurrent.ThreadLocalRandom` (one generator per thread,
no contention), which is what lets the parallel render scale to ~5x. If you add
new random sampling, use `ThreadLocalRandom.current().nextDouble()`, **not**
`Math.random()`.

### The one bug you'd have hit

`RaytracerDriver.main()` was originally declared as `public static void main()`
with no arguments. BlueJ lets you invoke any static method directly, so that
worked fine in the IDE — but the standard `java` launcher requires exactly
`public static void main(String[] args)`, and refuses to run anything else
(`Error: Main method not found in class RaytracerDriver`). **This has already
been fixed** in `RaytracerDriver.java` (the `args` parameter is unused, so
behavior is unchanged) — that's the only change needed to run this outside
BlueJ.

### What happens when you run it

`RaytracerDriver.main` builds the `cornellBox` scene at **1920x1080** with
**1024 samples/pixel** (a 32x32 anti-aliasing grid), 4 reflection bounces, and
2 ambient-light bounces, then writes `CornellBox.png` to whatever directory
you ran `java` from (a relative path — so run it from `ParallelTracer/` unless
you want the PNG elsewhere).

**Expect this to take a long time.** The render is CPU-bound and single-run,
using `Runtime.availableProcessors()` worth of parallelism automatically (see
"Parallel streams" below). As a reference point, on an 8-core machine a
160x90 render at 16 samples/pixel took ~0.8s; scaling that up to the default
1920x1080 @ 1024 samples is roughly **9,000x more pixel-samples**, so budget
on the order of **1–2+ hours** for the default settings on similar hardware.
Progress prints to stdout at each stage (`Creating scene...`,
`Rendering image...`, `Saving file...`, `Done`, plus total render time in
minutes) but nothing prints *during* the render itself — it will look idle
for the whole duration.

**For fast iteration while you're changing code**, drop the resolution and
sample count way down in `RaytracerDriver.java`:

```java
int xResolution = 320;
int yResolution = 180;
...
ColorImage image = s.render(xResolution, yResolution, 16, 2); // 16 samples instead of 1024
```

A 320x180 @ 16-sample render finishes in seconds and is enough to sanity-check
a scene or material change before committing to a full-quality render.

## 4. Rendering a different scene

`SceneCreator.java` has several scene-building methods (`scene1` ... `scene5`,
`texturesScene`, `cornellBox`). `RaytracerDriver.main` only ever calls one —
swap which one it calls to render something else:

```java
Scene s = SceneCreator.scene4(xResolution, yResolution);
```

(`scene1` is commented out in the source as a documented example of the
scene-building API — see `ARCHITECTURE.md` for how to add your own.)

## 5. Sanity-checking a broken environment

If image output ever looks wrong (blank/corrupt PNG) and you're not sure
whether it's your raytracing code or your Java/ImageIO setup, uncomment the
`saveTestImage()` call in `main` — it writes a plain color-gradient PNG using
only `BufferedImage`/`ImageIO`, bypassing all raytracer code, so you can tell
which half of the pipeline is broken.
