/**
 * A destination for finished pixels as the render computes them. Decouples the render
 * loop (Scene.makeImage) from any specific window, so a single window can host two
 * renders at once by handing each one its own sink (see RaceWindow), while the -v
 * single preview (PreviewWindow) is just another implementation.
 */
public interface PixelSink {
    void setPixel(int x, int y, Color c);
}
