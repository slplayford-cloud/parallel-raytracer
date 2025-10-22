public class Lambert extends Material{
    //this is the color of the object
    private Color diffuse;
    public Lambert(Color c){
        diffuse = c;
    }
    public Color getDiffuse(){
        return diffuse;
    }
    public Color computeLighting(Intersection i, Ray viewingRay, Light li){
        //compute the direction of the light vector and the normal vector 
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
            return finalC;
        }
    }
}
