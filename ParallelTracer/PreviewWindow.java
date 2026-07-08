import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A window that shows the render filling in live, pixel by pixel, as the parallel
 * render threads compute them. Render threads call setPixel() directly; a Swing
 * Timer repaints on its own schedule instead of after every pixel, since repainting
 * per-pixel across hundreds of thousands of calls would slow the render down badly.
 *
 * The title carries a caller-supplied label plus a live progress % and elapsed
 * timer, which is what makes the -c sequential-vs-parallel race legible.
 */
public class PreviewWindow extends JFrame {
    private final BufferedImage image;
    private final int height;
    private final int totalPixels;
    private final String label;
    private final long startTime;
    private final AtomicInteger pixelsDone = new AtomicInteger(0);
    private final Timer repaintTimer;

    public PreviewWindow(int width, int height){
        this(width, height, "Rendering");
    }

    public PreviewWindow(int width, int height, String label){
        super(label);
        this.height = height;
        this.label = label;
        this.totalPixels = width * height;
        this.startTime = System.currentTimeMillis();

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        add(imageLabel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        repaintTimer = new Timer(33, e -> {
            imageLabel.repaint();
            double pct = 100.0 * pixelsDone.get() / totalPixels;
            double elapsed = (System.currentTimeMillis() - startTime) / 1000.0;
            setTitle(String.format("%s — %.0f%% — %.1fs", label, pct, elapsed));
        });
        repaintTimer.start();
    }

    //called from the (parallel) render threads; flips y to match the orientation RaytracerDriver saves to PNG
    public void setPixel(int x, int y, Color c){
        image.setRGB(x, height - 1 - y, c.toARGB());
        pixelsDone.incrementAndGet();
    }

    //stops the repaint timer and shows the final elapsed time once the render is done
    public void finish(){
        repaintTimer.stop();
        double elapsed = (System.currentTimeMillis() - startTime) / 1000.0;
        setTitle(String.format("%s — done in %.1fs", label, elapsed));
        repaint();
    }
}
