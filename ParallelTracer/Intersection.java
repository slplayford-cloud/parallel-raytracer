public class Intersection
{
    private Point position;
    private Vector normal;
    private double distance;
    private Material surfaceMaterial;
    private double imageX, imageY;
    
    //contructor
    public Intersection(Point pos, Vector norm, double dist, Material material){
        position = pos;
        normal = norm.normalize();
        distance = dist;
        surfaceMaterial = material;
        imageX = 0;
        imageY = 0;
    }
    public Intersection(Point pos, Vector norm, double dist, Material material, 
                        double imgX, double imgY){
        position = pos;
        normal = norm.normalize();
        distance = dist;
        surfaceMaterial = material;
        imageX = imgX;
        imageY = imgY;
    }
    
    //getter methods for all variables
    public Point getPosition(){return position;}
    public Vector getNormal(){return normal;}
    public double getDistance(){return distance;}
    public Material getMaterial(){return surfaceMaterial;}
    public double getImageX(){return imageX;}
    public double getImageY(){return imageY;} 
    
}
