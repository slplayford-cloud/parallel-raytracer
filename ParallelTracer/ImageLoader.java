import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * Provides a method for loading an image from a file and converting it to our internal
 * ColorImage representation.
 * 
 * @author Ben Farrar
 * @version 2019.06.06
 */
public class ImageLoader {
    public static ColorImage loadImage(String filename){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println("Problem loading texture: " + filename);
            System.out.println(e);
            System.exit(1);
        }
        ColorImage c = new ColorImage(img.getWidth(), img.getHeight());
        for (int i=0; i<img.getWidth(); i++){
            for (int j=0; j<img.getHeight(); j++){
                c.setColor(i,img.getHeight()-1-j,fromARGB(img.getRGB(i,j)));
            }
        }
        return c;
    }
    
    public static Color fromARGB(int packed){
        double r = ((packed >> 16) & 255) / 255.0;
        double g = ((packed >> 8) & 255) / 255.0;
        double b = (packed & 255) / 255.0;
        return new Color(r,g,b);
    }
}
