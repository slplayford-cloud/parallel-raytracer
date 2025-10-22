
/**
 * Write a description of class Texture here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Texture extends Material
{
    //this is the color of the object
    private ColorImage image;
    public Texture(String fileName){
        image = ImageLoader.loadImage(fileName);
    }
    
    
    // Get the imageX and imageY from the Intersection parameter
    // Multiply those by the image's width and height, respectively
    // Cast the result to an int
    // Use those ints as your indexes for getting the color from the texture's ColorImage

    public Color computeLighting(Intersection i, Ray viewingRay, Light li){
        //compute the direction of the light vector and the normal vector 
        Vector vLight = li.computeLightDirection(i.getPosition());
        Vector vNorm = i.getNormal();
        
        //calculate the "percentage" of light hitting the object
        double lightPrcnt = vLight.dot(vNorm);
        if(lightPrcnt < 0){
            return new Color(0, 0, 0);
        }
        else{
            //compute the image "wrap" aka corresponding image color values
            int x = (int)(i.getImageX()*image.getWidth());
            int y = (int)(i.getImageY()*image.getHeight());
            Color imageColor = image.getColor(x, y);
            Color newC = imageColor.scale(lightPrcnt);
            Color finalC = newC.shade(li.computeLightColor(i.getPosition()));
            return finalC;
        }
    }
}
