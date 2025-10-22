public class Camera
{
    //Needs to store a Point location, Vectors for the forward, up, and right directions, and two doubles for the field of view: xFoV and yFoV.
    private Point location;
    private Vector forward;
    private Vector up;
    private Vector right;
    private double xFoV;
    private double yFoV;
    private double size;
    private double focal;
    
    public Camera(Point position, Vector forwardVector, Vector upVector, double fieldOfView, double aspectRatio, double lSize, double fLength){
        location = position;
        forward = forwardVector.normalize();
        up = upVector.normalize();
        right = forward.cross(up);
        xFoV = Math.toRadians(fieldOfView);
        yFoV = Math.atan( Math.tan(xFoV) / aspectRatio );
        size = lSize;
        focal = fLength;
    }
    //get camera position
    public Point getPosition(){return location;}
    
    //return the point in 3D space which corresponds to a point on the 2D image plane
    public Point imagePlanePoint(double u, double v){
        Point imagePlanePoint = location;
        //DOF INTEGRATED
        imagePlanePoint = imagePlanePoint.add(forward.scale(focal)).add(right.scale(2 * (u-0.5) * Math.tan(xFoV)).scale(focal)).add(up.scale(2 * (v-0.5) * Math.tan(yFoV)).scale(focal));
        return imagePlanePoint;
    }
    
    //generate a ray which starts at the camera and goes to a point in 3D space
    public Ray generateRay(double u, double v){
        //calculate a random point on the lens so we can apply DOF
        Point randP = location.add(forward.scale(Math.random()*size)).add(right.scale(Math.random()*size));
        Vector toImagePlane = imagePlanePoint(u, v).subtract(randP);
        Ray ray = new Ray(randP, toImagePlane, Math.random());
        return ray;
    }
}
