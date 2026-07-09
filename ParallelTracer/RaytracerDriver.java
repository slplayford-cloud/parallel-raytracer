import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * Driver for the various stages of the image generation and saving process.
 * 
 * @author Ben Farrar
 * @version 2019.05.22
 */
public class RaytracerDriver {
    public static void main(String[] args){
        //Size of the final image. This will DRAMATICALLY affect the runtime.
        int xResolution = 1280;
        int yResolution = 720;

        //Command line flags:
        //  -v  show a single live preview window while rendering
        //  -c  race a sequential render against a parallel render, side by side (takes precedence over -v)
        boolean showPreview = false;
        boolean raceMode = false;
        for(String arg : args){
            if(arg.equals("-v")){
                showPreview = true;
            }
            if(arg.equals("-c")){
                raceMode = true;
            }
        }

        //-c: launch the sequential-vs-parallel race and return. This is a self-contained demo mode.
        if(raceMode){
            renderRace(xResolution, yResolution);
            return;
        }

        //Create the scene. You can change this when you make other scene creation methods to select
        //which scene to render.
        //for(int i = 0; i < 360; i++){
            long starttime = System.currentTimeMillis();
            System.out.println("Creating scene...");
            Scene s = SceneCreator.cornellBox(xResolution, yResolution);

            //Live preview window, fills in pixel-by-pixel as the parallel render threads compute them.
            //Only created when "-v" was passed; otherwise preview stays null and render() runs headless.
            PreviewWindow preview = showPreview ? new PreviewWindow(xResolution, yResolution) : null;

            //Render the scene into a ColorImage
            System.out.println("Rendering image...");
            ColorImage image = s.render(xResolution,yResolution, 16, 2, preview);
            if(preview != null){
                preview.finish();
            }

            //Save the image out as a png
            System.out.println("Saving file...");
            String filename = "CornellBox.png";
            saveImage(filename, image);
            long endtime = System.currentTimeMillis();
        //}
        
        /* Simple image write. Use this to test if image writing is broken.
           If this doesn't work, that means something is wrong with your Java installation.
           If it DOES work, and you get a color gradient image written out as "testGradient.png",
           but the normal saveImage does not work, that means something is wrong with your raytracing code.
           */
        //saveTestImage();
        
        System.out.println("Done");
        System.out.println("Rendering Took: "+((endtime-starttime)/1000.0/60.0) + " minutes");
    }

    /**
     * -c demo mode: render the same scene sequentially and in parallel AT THE SAME TIME, both inside
     * ONE full-screen window (RaceWindow) with explanatory text, so a first-time viewer immediately
     * sees the parallel render race ahead and finish first. Prints the wall-clock times and speedup.
     *
     * Note: because both renders run concurrently they share the CPU cores, so the times are an
     * illustrative demo rather than a rigorous benchmark. Each render gets its own Scene instance so
     * they never touch shared mutable state (Lights mutate an internal random point during rendering).
     */
    public static void renderRace(int xResolution, int yResolution){
        System.out.println("Racing sequential vs parallel render...");

        int cores = Runtime.getRuntime().availableProcessors();

        //Two independent scenes so the concurrent renders share no mutable state.
        Scene seqScene = SceneCreator.cornellBox(xResolution, yResolution);
        Scene parScene = SceneCreator.cornellBox(xResolution, yResolution);

        //One full-screen window hosting both renders side by side.
        RaceWindow window = new RaceWindow(xResolution, yResolution, cores);

        //Result holders written from the worker threads.
        final long[] seqMillis = new long[1];
        final long[] parMillis = new long[1];
        final ColorImage[] parResult = new ColorImage[1];

        Thread seqThread = new Thread(() -> {
            long t = System.currentTimeMillis();
            seqScene.render(xResolution, yResolution, 16, 2, window.leftSink(), false);
            seqMillis[0] = System.currentTimeMillis() - t;
            window.markDone(true, seqMillis[0]);
        });
        Thread parThread = new Thread(() -> {
            long t = System.currentTimeMillis();
            parResult[0] = parScene.render(xResolution, yResolution, 16, 2, window.rightSink(), true);
            parMillis[0] = System.currentTimeMillis() - t;
            window.markDone(false, parMillis[0]);
        });

        //Start together so it's a real race.
        seqThread.start();
        parThread.start();
        try {
            seqThread.join();
            parThread.join();
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        System.out.printf("Sequential: %.1fs | Parallel: %.1fs | Speedup: %.1fx%n",
            seqMillis[0]/1000.0, parMillis[0]/1000.0, (double)seqMillis[0]/parMillis[0]);

        //Save the parallel result (the window stays open so you can compare the finished images + times).
        saveImage("CornellBox.png", parResult[0]);
        System.out.println("Done");
    }

    /**
     * Reads in each pixel from a ColorImage, and then writes the image out to a PNG file.
     */
    public static void saveImage(String filename, ColorImage image){
        try {
            
            BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            for(int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    //This line reverses the y axis. Use the following line instead if your image is upside down.
                    bi.setRGB(x,image.getHeight()-1-y,image.getColor(x,y).toARGB());
                    //bi.setRGB(x,y,image.getColor(x,y).toARGB());
                }
            }
            ImageIO.write(bi, "PNG", new File(filename));
            
        } catch(Exception e) {
            System.out.println("Problem saving image: " + filename);
            System.out.println(e);
            System.exit(1);
        }
    }
    
    /**
     * Simpler version of the saveImage method for testing. Doesn't require integration with ColorImage, just writes
     * a gradient of colors out to an image to make sure the BufferedImage, ImageIO, and File libraries are working
     * as expected.
     */
    public static void saveTestImage(){
        try {
            
               BufferedImage biTest = new BufferedImage(250,200,BufferedImage.TYPE_INT_RGB);
               for(int x = 0; x < 250; x++){
                   for(int y = 0; y < 200; y++){
                       biTest.setRGB(x,200-1-y,(x << 16) | (y << 8) | (0 << 0));
                    }
                }
                ImageIO.write(biTest, "PNG", new File("testGradient.png"));
                
        } catch(Exception e) {
            System.out.println("Problem saving test gradient image");
            System.out.println(e);
            System.exit(1);
        }
    }
}
