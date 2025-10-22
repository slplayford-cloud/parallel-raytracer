public class LightBulb extends Light
{
    private Color intensity;
    private Point position;
    private Point randP;
    private int radius;
    private Surface sourceOBJ;
    
    
    public LightBulb(Color c, Point p, int r){
        intensity = c;
        radius = r;
        position = p;
    }

    public void computeRandomPoint(){
        //choose a random point within the radius of the sphere
        Vector pointShift = new Vector((Math.random()*2*radius)-radius, (Math.random()*2*radius)-radius, (Math.random()*2*radius)-radius);
        pointShift = pointShift.normalize();
        //randP = position.add(pointShift);
        randP = position.add(pointShift.scale(radius));
    }
    
    //compute the direction which the light is going
    public Vector computeLightDirection(Point surfacePoint){
        Vector v = randP.subtract(surfacePoint);
        return v.normalize();
    }
    //compute the color of the light
    public Color computeLightColor(Point surfacePoint){
        return intensity;
        //return intensity.scale(Math.pow(minDist/this.computeLightDistance(surfacePoint), 2));
    }
    //compute the distance from a point to the light
    public double computeLightDistance(Point surfacePoint){
        Vector v = randP.subtract(surfacePoint);
        return v.length();
    }
}
