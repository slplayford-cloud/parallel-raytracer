public class Phong extends Material
{
    private Color diffuse;
    private Color specular;
    private double exponent;
    
    public Phong(Color diff, Color spec, double exp){
        diffuse = diff;
        specular = spec;
        exponent = exp;
    }
    public Color getDiffuse(){
        return diffuse;
    }
    
    public Color computeLighting(Intersection i, Ray viewingRay, Light li){
        //compute the direction of the light and the normal vector 
        Vector vLight = li.computeLightDirection(i.getPosition());
        Vector vNorm = i.getNormal();
        
        //calculate the "percentage" of light hitting the object
        double lightPrcnt = vLight.dot(vNorm);
        if(lightPrcnt < 0){
            return new Color(0, 0, 0);
        }
        else{
            Color newC = diffuse.scale(lightPrcnt);
            Color finalC = newC.shade(li.computeLightColor(i.getPosition()));
            
            //adding specular lighting
            Vector vMirror = (vNorm.scale((2*vNorm.dot(vLight))).subtract(vLight)).normalize();
            //cosine of the specular angle
            double cosSpec = viewingRay.getDirection().scale(-1).dot(vMirror);
            
            //if there is no specular color return the first color found
            if(cosSpec <= 0){
                return finalC;
            }
            double specCoeff = Math.pow(cosSpec, exponent);
            Color tempC = li.computeLightColor(i.getPosition()).scale(specCoeff);
            tempC = tempC.shade(specular);
            return tempC.tint(finalC);
        }
    }
}
