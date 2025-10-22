public class Ray
{
    private Point position;
    private Vector direction;
    private double time;
    
    public Ray(Point p, Vector v, double t){
        position = p;
        direction = v.normalize();
        time = t;
    }
    //getter methods
    public Point getPosition(){return position;}
    public Vector getDirection(){return direction;}
    public double getTime(){return time;}
    //public double getTime(){return time;}
    //shift a point by a distance(dist)
    public Point evaluate(double dist){
        Vector v = direction.scale(dist);
        return position.add(v);
    }
}
