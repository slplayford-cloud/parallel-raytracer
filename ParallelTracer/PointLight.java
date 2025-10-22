public class PointLight extends Light
{
    private Color intensity;
    private Point position;
    
    //constructor
    public PointLight(Color c, Point location){
        intensity = c;
        position = location;
    }
    
    //compute the direction which the light is going
    public Vector computeLightDirection(Point surfacePoint){
        Vector v = position.subtract(surfacePoint);
        return v.normalize();
    }
    //compute the color of the light
    public Color computeLightColor(Point surfacePoint){
        return intensity;
    }
    //compute the distance from a point to the light
    public double computeLightDistance(Point surfacePoint){
        Vector v = position.subtract(surfacePoint);
        return v.length();
    }
}
