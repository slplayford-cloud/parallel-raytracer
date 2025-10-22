import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.Future;
import java.util.stream.*;

public class Scene
{
    private Camera mainCam;
    private ArrayList<Surface> surfaces;
    private ArrayList<Light> lights;
    
    //constructor
    public Scene(Camera newCam){
        mainCam = newCam;
        lights = new ArrayList<Light>();
        surfaces = new ArrayList<Surface>();
    }
    //set the camera object and add a surface to the scene
    public void setCam(Camera newCam){
        mainCam = newCam;
    }
    public void addSurface(Surface s){
        surfaces.add(s);
    }
    //add a light
    public void addLight(Light li){
        lights.add(li);
    }
    
    public Color computeVisibleColor(Ray r, int bouncesLeft, int ambientBounce){
        double smallestDist = Integer.MAX_VALUE;
        Intersection closest = null;
        
        //for each surface in the scene, calculate which is closest to the camera so we can display it
        for(int i = 0; i < surfaces.size(); i++){
            if(surfaces.get(i).intersect(r) != null){
                Intersection maybeClosest = surfaces.get(i).intersect(r);
                double tempDistance = maybeClosest.getDistance();
                if(tempDistance < smallestDist){
                    smallestDist = tempDistance;
                    closest = maybeClosest;
                }
            }
        }
        if(closest == null){
            return new Color(0,0,0);
        } 
        Color newCol = new Color(0,0,0);
        Vector vNorm = closest.getNormal();

        for(int i = 0; i < lights.size(); i++){
            //only for area lights, select a random point on light to use for shadow ray
            lights.get(i).computeRandomPoint();
            if(!this.isShadowed(closest.getPosition(), lights.get(i), r.getTime())){
                newCol = newCol.tint(closest.getMaterial().computeLighting(closest, r, lights.get(i)));
            }
        }
        
        //AMBIENT LIGHTING    
        if(ambientBounce > 0){
            //take a vector of a completely random direction
            Vector bounceDir = new Vector(2*(Math.random()-0.5), 2*(Math.random()-0.5), 2*(Math.random()-0.5));
            
            //if its the wrong way inverse it
            if(vNorm.dot(bounceDir) < 0){
                bounceDir = bounceDir.scale(-1);
            }
            
            //tint the base color by the color at the new direction
            newCol = newCol.tint(this.computeVisibleColor(new Ray(closest.getPosition(), bounceDir, r.getTime()), bouncesLeft, ambientBounce-1).scale(vNorm.dot(bounceDir)));
        }
        
        //LIGHTING FOR GLASS MATERIAL
        Vector vNewNorm = vNorm;
        Color afterGlass = new Color(0,0,0);
        if(vNewNorm.dot(r.getDirection()) < 0){
            vNewNorm = vNewNorm.scale(-1);
            if(closest.getMaterial().isHollow()){
                vNewNorm = r.getDirection();
            }
        }
        if(closest.getMaterial().isGlass()){
            //compute the refracted ray and use that to calculate the new color
            Vector avgV = vNewNorm.scale(0.8).add(r.getDirection().scale(0.2));
            Ray pastGlass = new Ray(closest.getPosition(), avgV, r.getTime());
            afterGlass = this.computeVisibleColor(pastGlass, 1, ambientBounce);
        }
        
        //when talking about glass, deviance = opacity
        double maxDeviance = closest.getMaterial().getDeviance();
        newCol = newCol.tint(afterGlass.scale(maxDeviance));
        
        //BASE CASES FOR REFLECTION
        if(bouncesLeft == 0){
            return newCol;
        }
        if(closest.getMaterial().getReflectiveness() == 0){
            return newCol;
        }
        
        //calculate the direction and color of the reflection
        Vector vMirror = vNorm.scale(2*(vNorm.dot(r.getDirection().scale(-1)))).subtract(r.getDirection().scale(-1));
        vMirror = vMirror.add(new Vector(Math.random()*maxDeviance, Math.random()*maxDeviance, Math.random()*maxDeviance));
        Ray rMirror = new Ray(closest.getPosition(), vMirror, r.getTime());
        Color reflectColor = this.computeVisibleColor(rMirror, bouncesLeft-1, ambientBounce);
        
        //scale the new color by how reflective the material is
        reflectColor = reflectColor.scale(closest.getMaterial().getReflectiveness());
        
        //"combine" the color of the object and the color of the reflection
        newCol = newCol.tint(reflectColor);
        
        return newCol;
    }
    
    //use parallel processing here
    public ColorImage render(int xRes, int yRes, int numSamples, double ambientBlur){
        //create an image frame with corresponding resolution
        ColorImage frame = new ColorImage(xRes, yRes);
        
        /*
         * PARRALLEL PROCESSING EXPLINATION:
         *    1. Create an array which holds the x and y values of every point on the image plane
         *    2. Convert said array to a PARALLEL stream, NOT sequential 
         *       - Java automatically parallelizes method calls in this type of stream
         *    3. For each point in the new array, compute its color
         *    4. Set the color of frame[x][y] to new computed color
         */
        
        Point2D[] points = new Point2D[xRes*yRes];
        for(int x = 0; x < xRes; x++){
            for(int y = 0; y < yRes; y++){
                points[(yRes*x)+y] = new Point2D(x, y);
            }
        }
        
        //convert array to stream and make it parallel
        Stream<Point2D> pointStream = Arrays.stream(points).parallel();
        
        //compute the color of the point and change the color of frame
        pointStream.forEach(point ->
        {
            this.makeImage(frame, point.getX(), point.getY(), numSamples, xRes, yRes, ambientBlur);
        });
        return frame;
    }
    
    public void makeImage(ColorImage frame, int x, int y, int samples, int xRes, int yRes, double ambientBlur){
        int aaResolution = (int)Math.sqrt(samples);
        Color c = new Color(0,0,0);
        
        //anti aliasing
        for(int i = 0; i < aaResolution; i++){
            for(int j = 0; j < aaResolution; j++){
                
                //calculate 
                double u = (x+(i+0.5)/aaResolution)/xRes;
                double v = (y+(j+0.5)/aaResolution)/yRes;
                Ray fromCam = mainCam.generateRay(u, v);
                
                //check if the ray intersects with an object and color said pixels
                //second param is the number of times we "bounce" a reflection
                c = c.add(this.computeVisibleColor(fromCam, 4, 2));
            }
        }
        c = c.scale(1/Math.pow(aaResolution, 2));
        frame.setColor(x, y, c);
    }
    
    public boolean isShadowed(Point p, Light li, double time){
        //direction of the light
        Vector dir = li.computeLightDirection(p);
        Ray rShadow = new Ray(p, dir, time);
        //check if an object is blocking the rays path to the light
        for(int i = 0; i < surfaces.size(); i++){
            if(surfaces.get(i).intersect(rShadow) != null && surfaces.get(i).intersect(rShadow).getDistance() < li.computeLightDistance(p)){
                return true;
            }
        }
        return false;
    }
}
