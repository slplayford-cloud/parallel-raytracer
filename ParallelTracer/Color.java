public class Color
{
    private double r;
    private double g;
    private double b;
    
    public Color(double newR, double newG, double newB){
        r = newR;
        g = newG;
        b = newB;
    }
    
    public double getR(){return r;}
    public double getG(){return g;}
    public double getB(){return b;}
    
    //add the rgb values from 2 different colors
    public Color add(Color c){
        Color newC = new Color(r+c.getR(), g+c.getG(), b+c.getB());
        return newC;
    }
    //scale the brightness of the color by an arbitrary amount
    public Color scale(double scalar){
        Color newC = new Color(r*scalar, g*scalar, b*scalar);
        return newC;
    }
    //translate to ARGB
    public int toARGB() {
            int ir = (int)(Math.min(Math.max(r,0),1) * 255 + 0.1);
            int ig = (int)(Math.min(Math.max(g,0),1) * 255 + 0.1);
            int ib = (int)(Math.min(Math.max(b,0),1) * 255 + 0.1);
            return (ir << 16) | (ig << 8) | (ib << 0);
    }
    //multiply the rgb values of 2 colors, (merge colors)
    public Color shade(Color c){
        Color newC = new Color(r * c.getR(), g * c.getG(), b * c.getB());
        return newC;
    }
    //tint one color by the rgb values of another color
    public Color tint(Color c){
        Color newC = new Color(r + (1-r)*c.getR(), g + (1-b)*c.getG(), b + (1-g)*c.getB());
        return newC;
    }
}
