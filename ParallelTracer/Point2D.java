public class Point2D{
    /*
     * This class was made to assist with parallel processing
     * It stores the x and y values of the image plane that we are working on
     * We can then use parallel processing to generate, and trace, rays corresponding to each 2D point
     */
    private int x;
    private int y;

    //store x and y values in constructor
    public Point2D(int x0, int y0)
    {
        x = x0;
        y = y0;
    }

    //getter methods for x and y values
    public int getX(){return x;}
    public int getY(){return y;}
}
