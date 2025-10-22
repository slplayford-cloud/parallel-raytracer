public class Vector
{
    private double dx;
    private double dy;
    private double dz;
    public Vector(double newDX, double newDY, double newDZ){
        dx = newDX;
        dy = newDY;
        dz = newDZ;
    }
    
    //Getter Methods
    public double getDX(){return dx;}
    public double getDY(){return dy;}
    public double getDZ(){return dz;}
    
    //Scales a vector by arbitrary input
    public Vector scale(double scalar){
        Vector newV = new Vector(dx*scalar, dy*scalar, dz*scalar);
        return newV;
    }
    //subtract two vectors
    public Vector subtract(Vector v){
        Vector newV = new Vector(dx - v.getDX(), dy - v.getDY(), dz - v.getDZ());
        return newV;
    }
    //add two vectors
    public Vector add(Vector v){
        Vector newV = new Vector(dx + v.getDX(), dy + v.getDY(), dz + v.getDZ());
        return newV;
    }
    //calculate dot product of two vectors
    public double dot(Vector v){
        return (dx*v.getDX()) + (dy*v.getDY()) + (dz*v.getDZ());
    }
    //find the cross product of two vectors
    public Vector cross(Vector v){
        Vector newV = new Vector(dy*v.getDZ() - dz*v.getDY(), 
                                 dz*v.getDX() - dx*v.getDZ(), 
                                 dx*v.getDY() - dy*v.getDX());
        return newV;
    }
    //finds the length of a vector
    public double length(){
        return Math.sqrt(this.dot(this));
    }
    //scales a vector so that the length is 1
    public Vector normalize(){
        double L = this.length();
        return this.scale(1/L);
    }
    //returns a vector with all positive values
    public Vector abs(){
        Vector newV = new Vector(Math.abs(dx), Math.abs(dy), Math.abs(dz));
        return newV;
    }
    public Vector divide(Vector v){
        return new Vector(dx*(1/v.getDX()), dy*(1/v.getDY()), dz*(1/v.getDZ()));
    }
}
