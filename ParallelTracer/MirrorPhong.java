public class MirrorPhong extends Phong
{
    private double refl;
    private double deviance;
    
    public MirrorPhong(Color diff, Color spec, double exp, double refPwr, double spread){
        super(diff, spec, exp);
        refl = refPwr;
        deviance = spread;
    }   
    public double getReflectiveness(){
        return refl;
    }
    public double getDeviance(){
        return deviance;
    }
}
