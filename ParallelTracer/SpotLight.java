public class SpotLight extends Light
{
    private Color intensity;
    private Point position;
    private double fov;
    private Vector dir;
    private double minDist;
    
    public SpotLight(Color c, Point location, double angle, Vector normal, double power)
    {
        position = location;
        intensity = c;
        fov = angle;
        dir = normal.normalize();
        //this is the distance at which the light is 100% bright
        minDist = power;
    }

    //compute the direction which the light is going
    public Vector computeLightDirection(Point surfacePoint){
        Vector v = position.subtract(surfacePoint);
        return v.normalize();
    }
    //compute the color of the light
    public Color computeLightColor(Point surfacePoint){
        //check if the object is within the fov
        if(Math.acos(this.computeLightDirection(surfacePoint).dot(dir)) > 0){
            if(Math.acos(Math.abs(this.computeLightDirection(surfacePoint).dot(dir))) < Math.toRadians(fov)){
                //add light with a square root falloff (numerator = distance where light is 100%)
                return intensity.scale(Math.pow(minDist/this.computeLightDistance(surfacePoint), 2));
            }
        }
        return new Color(0,0,0);
    }
    //compute the distance from a point to the light
    public double computeLightDistance(Point surfacePoint){
        Vector v = position.subtract(surfacePoint);
        return v.length();
    }
}
