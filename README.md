**Parallel RayTracer Project**
High School Junior Year Computer Science Project

Credit to Ben Farrar for the image generation algorithms for this project and for teaching me the tools needed for this project

PROJECT TITLE: Parallel Ray Tracer

PURPOSE OF PROJECT: To render images using rays, vectors, and points, at a very high quality while also optimizing performance

VERSION or DATE: June 9th 2023 (V 4.3)

HOW TO START THIS PROJECT: Run RaytracerDriver.main()

AUTHORS: Stephen Playford

ADDITIONAL FEATURES, and SOURCES FOR OUTSIDE RESEARCH:

	PARALLEL PROCESSING
	Included separately is a project I used to practice streams in java
	https://blogs.oracle.com/javamagazine/post/java-parallel-streams-performance-benchmark
	https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
	https://stackoverflow.com/questions/40933362/how-can-i-create-a-parallel-stream-from-an-array
	https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html

	Ambient Lighting
	Texture Mapping
	Spotlight
	Motion Blur
	Solid Glass Material
	Hollow Glass Material
	Rectangle Composite Shape
	Movie Capabilities (Solar system + other weird movie)
	Soft Shadows
	Soft reflections
	Camera DOF


USER INSTRUCTIONS:
Assuming you know how to make a scene the following is a guide to using the program and all of its features:

-Note: the Point2D class is a back end class, no need to worry about using it

Types of Materials:
	Lambert: Only takes in color of the object, outputs a rubber like surface
	Phong: Takes color, color of the "glare", and density of "glare", outputs a shiny surface
	MirrorPhong: same as phong -> also takes in reflectiveness and "blurriness" of reflection, outputs a reflective surface
	Glass: same as mirrorPhonge -> this will produce an object made of solid glass which warps the viewing rays
	Texture: Takes an image which will be wrapped around a sphere (can only be used on spheres

Types of Lights:
	PointLight -> standard point light with no intensity falloff
	SpotLight -> this light takes in a direction vector and angle to produce a spotlight effect
	LightBulb -> this light takes a radius which creates an light which takes up area which produces soft shadows
