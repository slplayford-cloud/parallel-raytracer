import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Full-screen, self-explanatory demo window for the -c race. Shows the SAME scene
 * being rendered two ways at once — sequentially (one CPU core) on the left and in
 * parallel (all cores) on the right — with explanatory text above, live per-side
 * progress/timers under each image, and the final speedup across the bottom.
 *
 * Each render writes into its own BufferedImage through a PixelSink (leftSink /
 * rightSink). A Swing Timer repaints ~30fps rather than repainting per pixel.
 */
public class RaceWindow extends JFrame {
    //palette
    private static final Color BG        = new Color(0x1E, 0x1E, 0x22);
    private static final Color TEXT_MAIN = new Color(0xEA, 0xEA, 0xEA);
    private static final Color TEXT_DIM  = new Color(0x9A, 0x9A, 0xA2);
    private static final Color SEQ_COLOR = new Color(0xFF, 0xFF, 0xFF); //amber
    private static final Color PAR_COLOR = new Color(0xFF, 0xFF, 0xFF); //green
    private static final Color FRAME_LINE = new Color(0x3A, 0x3A, 0x42);

    private final int imgW, imgH;
    private final int totalPixels;
    private final int cores;
    private final long startTime;

    private final BufferedImage leftImage;
    private final BufferedImage rightImage;
    private final AtomicInteger leftDone = new AtomicInteger(0);
    private final AtomicInteger rightDone = new AtomicInteger(0);

    //frozen elapsed time (ms) for each side once it finishes; -1 while still rendering
    private volatile long leftMillis = -1;
    private volatile long rightMillis = -1;

    public RaceWindow(int imgW, int imgH, int cores){
        super("Parallel Processing, Visualized");
        this.imgW = imgW;
        this.imgH = imgH;
        this.cores = cores;
        this.totalPixels = imgW * imgH;
        this.startTime = System.currentTimeMillis();

        leftImage = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
        rightImage = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);

        JPanel panel = new JPanel(){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                draw((Graphics2D) g, getWidth(), getHeight());
            }
        };
        panel.setBackground(BG);
        setContentPane(panel);

        //ESC exits (undecorated full screen has no close button)
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.registerKeyboardAction(e -> dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        //go full screen (fall back to a maximized undecorated window if exclusive mode is refused)
        setUndecorated(true);
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        boolean fullScreen = false;
        if(device.isFullScreenSupported()){
            try { device.setFullScreenWindow(this); fullScreen = true; } catch(Exception ignored){}
        }
        if(!fullScreen){
            setBounds(device.getDefaultConfiguration().getBounds());
            setVisible(true);
        }

        Timer repaintTimer = new Timer(33, e -> panel.repaint());
        repaintTimer.start();
    }

    //--- pixel sinks handed to the two renders ---
    public PixelSink leftSink(){
        return (x, y, c) -> { leftImage.setRGB(x, imgH - 1 - y, c.toARGB()); leftDone.incrementAndGet(); };
    }
    public PixelSink rightSink(){
        return (x, y, c) -> { rightImage.setRGB(x, imgH - 1 - y, c.toARGB()); rightDone.incrementAndGet(); };
    }

    //freeze a side's final time when its render returns
    public void markDone(boolean left, long millis){
        if(left) leftMillis = millis; else rightMillis = millis;
    }

    private void draw(Graphics2D g2, int W, int H){
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setColor(BG);
        g2.fillRect(0, 0, W, H);

        int pad = Math.max(24, W / 60);

        //--- title + subtitle (top) ---
        g2.setColor(TEXT_MAIN);
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
        drawCentered(g2, "Raytracing Engine Demo", W / 2, 62);

        g2.setColor(TEXT_DIM);
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 21));
        drawCentered(g2, "The left image uses a single CPU core and the right uses "
             + cores + " cores.", W / 2, 100);

        //ESC hint, top-right
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        g2.setColor(FRAME_LINE.brighter());
        String esc = "ESC to exit";
        g2.drawString(esc, W - pad - g2.getFontMetrics().stringWidth(esc), 30);

        //--- layout regions ---
        int footerH = 96;
        int captionH = 96;
        int areaTop = 132;
        int areaBottom = H - footerH;
        int cellW = (W - 3 * pad) / 2;
        int availH = areaBottom - areaTop - captionH;

        double scale = Math.min((double) cellW / imgW, (double) availH / imgH);
        int drawW = (int) (imgW * scale);
        int drawH = (int) (imgH * scale);

        int leftCellX = pad;
        int rightCellX = pad + cellW + pad;
        int leftImgX = leftCellX + (cellW - drawW) / 2;
        int rightImgX = rightCellX + (cellW - drawW) / 2;
        int imgY = areaTop + (availH - drawH) / 2;

        //--- the two images ---
        g2.drawImage(leftImage, leftImgX, imgY, drawW, drawH, null);
        g2.drawImage(rightImage, rightImgX, imgY, drawW, drawH, null);
        g2.setColor(FRAME_LINE);
        g2.drawRect(leftImgX, imgY, drawW, drawH);
        g2.drawRect(rightImgX, imgY, drawW, drawH);

        //--- captions under each image ---
        long now = System.currentTimeMillis();
        int captionY = imgY + drawH + 34;
        drawCaption(g2, leftCellX + cellW / 2, captionY, "SEQUENTIAL", SEQ_COLOR,
            "1 CPU core · one pixel at a time", leftDone.get(), leftMillis, now);
        drawCaption(g2, rightCellX + cellW / 2, captionY, "PARALLEL", PAR_COLOR,
            "all " + cores + " CPU cores · at once", rightDone.get(), rightMillis, now);

        //--- footer (state-driven) ---
        boolean lDone = leftMillis >= 0, rDone = rightMillis >= 0;
        String footer;
        Color footerColor;
        Font footerFont;
        if(lDone && rDone){
            double sp = (double) leftMillis / rightMillis;
            footer = String.format("Parallel finished %.1f× faster  |  %.1fs  vs  %.1fs sequential",
                sp, rightMillis / 1000.0, leftMillis / 1000.0);
            footerColor = PAR_COLOR;
            footerFont = new Font(Font.SANS_SERIF, Font.BOLD, 32);
        } else if(rDone){
            footer = String.format("Parallel finished in %.1fs  |  sequential is still rendering  (%.0fs)…",
                rightMillis / 1000.0, (now - startTime) / 1000.0);
            footerColor = TEXT_MAIN;
            footerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
        } else {
            footer = "Rendering…";
            footerColor = TEXT_DIM;
            footerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
        }
        g2.setColor(footerColor);
        g2.setFont(footerFont);
        drawCentered(g2, footer, W / 2, H - footerH / 2 + 10);
    }

    private void drawCaption(Graphics2D g2, int centerX, int y, String label, Color labelColor,
                             String sub, int done, long doneMillis, long now){
        g2.setColor(labelColor);
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        drawCentered(g2, label, centerX, y);

        g2.setColor(TEXT_DIM);
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        drawCentered(g2, sub, centerX, y + 25);

        String status;
        if(doneMillis >= 0){
            status = String.format("✓ done in %.1fs", doneMillis / 1000.0);
        } else {
            int pct = (int) (100.0 * done / totalPixels);
            status = String.format("%d%%  ·  %.0fs", pct, (now - startTime) / 1000.0);
        }
        g2.setColor(doneMillis >= 0 ? labelColor : TEXT_MAIN);
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        drawCentered(g2, status, centerX, y + 52);
    }

    private void drawCentered(Graphics2D g2, String s, int centerX, int y){
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(s, centerX - fm.stringWidth(s) / 2, y);
    }
}
