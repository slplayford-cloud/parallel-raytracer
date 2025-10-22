public class Glass extends MirrorPhong{
    //https://www.olympus-lifescience.com/en/microscope-resource/primer/lightandcolor/refractionintro/#:~:text=The%20curved%20surface%20of%20the,that%20of%20a%20convex%20lens.
    //Snell's Law of Refraction
    //n1 × sin(θ1) = n2 × sin(θ2)
    
    //if dist bewtween ray origin and intersection > 2 units, just use 1 (value of air refraction)
    
    // Material      Refractive Index
    // Air             1.0003
    // Water           1.333
    // Glycerin        1.473
    // Immersion Oil   1.515
    // Glass (Crown)   1.520
    // Glass (Flint)   1.656
    // Zircon          1.920
    // Diamond         2.417
    // Lead Sulfide    3.910
    
    private double refIndex; 
    private boolean hollow;
    
    //opacity is a double MUST BE 0-1 which represents how opaque it is (higher opacity = less see through)
    public Glass(Color diff, Color spec, double exp, double refPwr, double opacity, double refraction, boolean isHollow){
        super(diff, spec, exp, refPwr, 1-opacity);
        refIndex = refraction;
        hollow = isHollow;
    }
    
    //2 boolean methods which tell us if material is glass and if it is hollow vs solid
    public boolean isGlass(){
        return true;
    }    
    public boolean isHollow(){
        return hollow;
    }
}
