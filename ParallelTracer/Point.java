public class Point
{
    private double x;
    private double y;
    private double z;
    public Point(double newX, double newY, double newZ){
        x = newX;
        y = newY;
        z = newZ;
    }
    
    //Getter Methods for all variables
    public double getX(){return x;}
    public double getY(){return y;}
    public double getZ(){return z;}
    
    //create new point shifted by a vector
    public Point add(Vector v){
        Point p = new Point(x + v.getDX(), y + v.getDY(), z + v.getDZ());
        return p;
    }
    //find a new vector based on the difference between two points
    public Vector subtract(Point p){
        Vector v = new Vector(x - p.getX(), y - p.getY(), z - p.getZ());
        return v;
    }
}
