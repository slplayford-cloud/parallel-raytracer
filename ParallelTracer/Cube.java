public class Cube extends Surface
{
    private Rectangle front, back, top, bottom, right, left;
    
    public Cube(Point p, Vector up, Vector x, Vector z, Material m){
        //make a rectangle for each side of the cube
        front = new Rectangle(p, p.add(x), p.add(up), p.add(up.add(x)), m);
        back = new Rectangle(p.add(z), p.add(up.add(z)), p.add(x.add(z)), p.add(x.add(up.add(z))), m);
        right = new Rectangle(p.add(x), p.add(x.add(z)), p.add(x.add(up)), p.add(x.add(z.add(up))), m);
        left = new Rectangle(p.add(z), p, p.add(z.add(up)), p.add(up), m);
        bottom = new Rectangle(p, p.add(x), p.add(z), p.add(x.add(z)), m);
        top = new Rectangle(p.add(up), p.add(x.add(up)), p.add(z.add(up)), p.add(x.add(z.add(up))), m);
    }

    
    public Intersection intersect(Ray ray){
        // Surface[] surfaces = new Surface[6];
        // surfaces[0] = front;
        // surfaces[1] = back;
        // surfaces[2] = right;
        // surfaces[3] = left;
        // surfaces[4] = bottom;
        // surfaces[5] = top;
        
        // double smallestDist = Integer.MAX_VALUE;
        // Surface closest = null;
        
        // //for each surface in the scene, calculate which is closest to the camera so we can display it
        // for(int i = 0; i < surfaces.length; i++){
            // if(surfaces[i].intersect(ray) != null){
                // Intersection maybeClosest = surfaces[i].intersect(ray);
                // double tempDistance = maybeClosest.getDistance();
                // if(tempDistance < smallestDist){
                    // smallestDist = tempDistance;
                    // closest = surfaces[i];
                // }
            // }
        // }
        
        // return closest.intersect(ray);
        
        if(front.intersect(ray) != null){
          return front.intersect(ray);
        }
        else if(left.intersect(ray) != null){
          return left.intersect(ray);
        }
        else if(top.intersect(ray) != null){
          return top.intersect(ray);
        }
        else if(bottom.intersect(ray) != null){
          return bottom.intersect(ray);
        }
        else if(right.intersect(ray) != null){
          return right.intersect(ray);
        }
        else{
          return back.intersect(ray);
        }
    }
}
