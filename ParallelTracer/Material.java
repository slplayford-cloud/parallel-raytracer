public abstract class Material
{
    public abstract Color computeLighting(Intersection i, Ray viewingRay, Light li);
    public double getReflectiveness(){
        return 0;
    }
    public double getDeviance(){
        return 0;
    }
    public boolean isGlass(){
        return false;
    }
    public boolean isHollow(){
        return false;
    }
    public Color getDiffuse(){
        return new Color(0,0,0);
    }
}
