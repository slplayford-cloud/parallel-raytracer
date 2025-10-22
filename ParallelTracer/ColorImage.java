public class ColorImage
{
    private Color[][] image;
    private int height;
    private int width;
    
    public ColorImage(int newWidth, int newHeight){
        image = new Color[newWidth][newHeight];
        height = newHeight;
        width = newWidth;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                image[x][y] = new Color(0, 0, 0);
            }
        }
    }
    
    //getters for dimensions
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    
    //getter and setter methods for pixel color
    public Color getColor(int x, int y){
        return image[x][y];
    }
    public void setColor(int x, int y, Color c){
        image[x][y] = c;
    }
}
