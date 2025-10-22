public class Rectangle extends Surface
{
    Triangle triangle1;
    Triangle triangle2;
    
    //create 2 triangles with given points
    public Rectangle(Point bLeft, Point bRight, Point tLeft, Point tRight, Material type){
        triangle1 = new Triangle(bLeft, tRight, tLeft, type);
        triangle2 = new Triangle(bLeft, tRight, bRight, type);
    }
    
    public Intersection intersect(Ray ray){
        if(triangle1.intersect(ray) != null){
            return triangle1.intersect(ray);
        }
        return triangle2.intersect(ray);
    }
}
