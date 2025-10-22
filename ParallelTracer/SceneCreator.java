
/**
 * Details the static methods to creating various scenes for use in the raytracer.
 * scene1() is included as an example. You can add more static methods (for example scene2(),
 * scene3(), etc.) to create different scenes without affecting scene1.
 *
 * @author Ben Farrar
 * @version 2019.05.22
 */
public class SceneCreator {
    /*
    public static Scene scene1(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(0,0,0),       // camera location
                                new Vector(0,0,-1),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                20,                     // field of view
                                xResolution/yResolution ); // aspect ratio
        Scene s = new Scene(cam);
        
        //Each sphere takes a Point (its center), the radius, and a material.
        //For now, since we have not implemented the Material classes, we simply say they are null.
        Surface s1 = new Sphere(new Point(0,0,-20),3, null);
        s.addSurface(s1);
        Surface s2 = new Sphere(new Point(0,4,-15),1, null);
        s.addSurface(s2);
        Surface s3 = new Sphere(new Point(5,0,-20),1.5, null);
        s.addSurface(s3);
        //Each triangle takes 3 Points (its vertexes), and a material.
        Surface t1 = new Triangle(new Point(-3.5,-1,-15), new Point(-3.5,1,-15), new Point(-5,0,-16), null);
        s.addSurface(t1);
        Surface floor = new Triangle(new Point(0,-5,0), new Point(3000,-5,-1000), new Point(-3000,-5,-1000), null);
        s.addSurface(floor);
        
        return s;
    }
    public static Scene scene2(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(0,0,0),       // camera location
                                new Vector(0,0,-1),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                20,                     // field of view
                                xResolution/yResolution ); // aspect ratio
        Scene s = new Scene(cam);                        
                                
        PointLight l1 = new PointLight(new Color(1,1,1), new Point(20, 40, 10));
        s.addLight(l1);
        
        Lambert redRubber = new Lambert(new Color(1, 0, 0));
        Lambert greenRubber = new Lambert(new Color(0, 1, 0));

        Surface s1 = new Sphere(new Point(0,0,-20),3, redRubber);
        s.addSurface(s1);
        // Surface s2 = new Sphere(new Point(0,4,-15),1, redRubber);
        // s.addSurface(s2);
        Surface s3 = new Sphere(new Point(5,0,-20),1.5, redRubber);
        s.addSurface(s3);
        Surface t1 = new Triangle(new Point(-3.5,-1,-15), new Point(-3.5,1,-15), new Point(-5,0,-16), greenRubber);
        s.addSurface(t1);
        Surface floor = new Triangle(new Point(0,-5,0), new Point(3000,-5,-1000), new Point(-3000,-5,-1000), greenRubber);
        s.addSurface(floor);
        
        return s;
    }
    
    public static Scene scene3(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(-50,50,50),       // camera location
                                new Vector(1, -1, -1),     // forward vector/view direction
                                new Vector(1,1,-1),      // up vector
                                40,                     // field of view
                                xResolution/yResolution, 1, 1 ); // aspect ratio
        Scene s = new Scene(cam);
        
        SpotLight l1 = new SpotLight(new Color(1, 1, 1), new Point(0, 30, 30), 70, new Vector(0, -1, -1), 40);
        s.addLight(l1);
        // DirLight l2 = new DirLight(new Color(1, 1, 1), new Point(0, 30, 30), new Vector(0, -1, -1));
        // s.addLight(l2);
        PointLight l2 = new PointLight(new Color(.3, .3, .3), new Point(-100, 130, -20));
        s.addLight(l2);
        
        Lambert greyRubber = new Lambert(new Color(.8, .8, .8));
        Lambert blueRubber = new Lambert(new Color(.14, .72, 1));
        
        Phong blueShine = new Phong(new Color(.14, .72, 1), new Color(.8, .8, .8), 10);
        BPhong newBShine = new BPhong(new Color(.14, .72, 1), new Color(.8, .8, .8), 10);
        
        Surface floor = new Triangle(new Point(0,-5,500), new Point(3000,-5,-1000), new Point(-3000,-5,-1000), greyRubber);
        s.addSurface(floor);
        
        for(int i = 0; i < 8; i++){
            s.addSurface(new Sphere(new Point(0, 0, 20-(15*i)), 3, blueShine));
        }
        
        return s;
    }
    */
    
    public static Scene scene4(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(0,20,0),       // camera location
                                new Vector(0,0,-1),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                50,                     // field of view
                                xResolution/yResolution, // aspect ratio
                                0,                     //lens size
                                40);                    //focal length
        Scene s = new Scene(cam);                        
                                
        Light l1 = new PointLight(new Color(.5,.5,.5), new Point(50, 100, -20));
        Light l2 = new PointLight(new Color(.5,.5,.5), new Point(-50, 100, -20));
        Light l3 = new SpotLight(new Color(.5, .5, .5), new Point(0, 20, 0), 20, new Vector(0, 1, -1), 50);
        Light l4 = new PointLight(new Color(.5,.5,.5), new Point(0, 100, -99));
        s.addLight(l1);
        s.addLight(l2);
        s.addLight(l3);
        //s.addLight(l4);
        
        Color grey = new Color(.8,.8,.8);        
        
        Surface wall1 = new Rectangle(new Point(-100,0,-100), new Point(100,0,-100), 
                                      new Point(-100,100,-100), new Point(100,100,-100), 
                                      new Lambert(grey));
                                      
        Surface wall2 = new Rectangle(new Point(-100,0,100), new Point(-100,0,-100), 
                                      new Point(-100,100,100), new Point(-100,100,-100), new Lambert(grey));
                                      
        Surface wall3 = new Rectangle(new Point(100,0,100), new Point(100,0,-100), 
                                      new Point(100,100,100), new Point(100,100,-100), new Lambert(grey));
                                      
        s.addSurface(wall1);
        s.addSurface(wall2);
        s.addSurface(wall3);
        Surface floor = new Rectangle(new Point(-100,0,100), new Point(-100,0,-100), 
                                      new Point(100,0,-100), new Point(100,0,100), new Lambert(grey));
        s.addSurface(floor);
        
        Surface s1 = new Sphere(new Point(20, 10, -40), 10, new Phong(new Color(.14, .72, 1), grey, 10), new Vector(6, 0, 0));
        Surface s2 = new Sphere(new Point(0, 6, -60), 6, new MirrorPhong(new Color(233/255.0, 153/255.0, 1), grey, 10, .4, 0));
        //Surface s2 = new Sphere(new Point(0, 6, -60), 6, new Lambert(new Color(233/255.0, 153/255.0, 1)));
        Surface s3 = new Sphere(new Point(0, 70, -50), 20, new MirrorPhong(new Color(.6,.6,.6), grey, 10, 1, 0));
        Surface s4 = new Cone(new Point(80, 0, -80), new Point(80, 40, -80), 15, new Phong(new Color(253/255.0, 1, 117/255.0), grey, 10));
        Surface s5 = new Sphere(new Point(-10, 5, -20), 5, new Lambert(new Color(.6, .4, .2)));
        s.addSurface(s1);
        s.addSurface(s2);
        s.addSurface(s4);
        s.addSurface(s3);
        s.addSurface(s5);
        
        Surface mirror = new Rectangle(new Point(-30, 0, -20), new Point(-10, 0, -70),
                                       new Point(-30, 40, -20), new Point(-10, 40, -70), 
                                       new MirrorPhong(new Color(.7,.7,.7), grey, 10, .8, 0));
        Surface glassBall = new Sphere(new Point(0, 7, -30), 5, new Glass(new Color(.1, .1, .1), grey, 15, .15, .2, 0, true));
        s.addSurface(glassBall);
        s.addSurface(mirror);
        return s;
    }
    public static Scene scene4AreaLights(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(0,20,0),       // camera location
                                new Vector(0,0,-1),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                50,                     // field of view
                                xResolution/yResolution, // aspect ratio
                                2.2,                     //lens size
                                40);                    //focal length
        Scene s = new Scene(cam);                        
                                
        LightBulb l1 = new LightBulb(new Color(.5,.5,.5), new Point(50, 100, -20), 10);
        //s.addSurface(l1.getSourceOBJ());
        LightBulb l2 = new LightBulb(new Color(.5,.5,.5), new Point(-50, 100, -20), 10);
        //s.addSurface(l2.getSourceOBJ());
        Light l3 = new SpotLight(new Color(.5, .5, .5), new Point(0, 20, 0), 20, new Vector(0, 1, -1), 50);
        Light l4 = new PointLight(new Color(.5,.5,.5), new Point(0, 100, -99));
        s.addLight(l1);
        s.addLight(l2);
        s.addLight(l3);
        //s.addLight(l4);
        
        Color grey = new Color(.8,.8,.8);        
        
        Surface wall1 = new Rectangle(new Point(-100,0,-100), new Point(100,0,-100), 
                                      new Point(-100,100,-100), new Point(100,100,-100), 
                                      new Lambert(grey));
                                      
        Surface wall2 = new Rectangle(new Point(-100,0,100), new Point(-100,0,-100), 
                                      new Point(-100,100,100), new Point(-100,100,-100), new Lambert(grey));
                                      
        Surface wall3 = new Rectangle(new Point(100,0,100), new Point(100,0,-100), 
                                      new Point(100,100,100), new Point(100,100,-100), new Lambert(grey));
                                      
        s.addSurface(wall1);
        s.addSurface(wall2);
        s.addSurface(wall3);
        Surface floor = new Rectangle(new Point(-100,0,100), new Point(-100,0,-100), 
                                      new Point(100,0,-100), new Point(100,0,100), new Lambert(grey));
        s.addSurface(floor);
        
        Surface s1 = new Sphere(new Point(20, 10, -40), 10, new Phong(new Color(.14, .72, 1), grey, 10), new Vector(10, 0, 0));
        Surface s2 = new Sphere(new Point(0, 6, -60), 6, new MirrorPhong(new Color(233/255.0, 153/255.0, 1), grey, 10, .4, 0));
        //Surface s2 = new Sphere(new Point(0, 6, -60), 6, new Lambert(new Color(233/255.0, 153/255.0, 1)));
        Surface s3 = new Sphere(new Point(0, 70, -50), 20, new MirrorPhong(new Color(.6,.6,.6), grey, 10, 1, 0));
        Surface s4 = new Cone(new Point(80, 0, -80), new Point(80, 40, -80), 15, new Phong(new Color(253/255.0, 1, 117/255.0), grey, 10));
        Surface s5 = new Sphere(new Point(-10, 5, -20), 5, new Lambert(new Color(.6, .4, .2)));
        s.addSurface(s1);
        s.addSurface(s2);
        s.addSurface(s4);
        s.addSurface(s3);
        s.addSurface(s5);
        
        Surface mirror = new Rectangle(new Point(-30, 0, -20), new Point(-10, 0, -70),
                                       new Point(-30, 40, -20), new Point(-10, 40, -70), 
                                       new MirrorPhong(new Color(.7,.7,.7), grey, 10, .8, 0));
        Surface glassBall = new Sphere(new Point(0, 7, -30), 5, new Glass(new Color(.1, .1, .1), grey, 15, .15, .2, 0, false));
        s.addSurface(glassBall);
        s.addSurface(mirror);
        return s;
    }
    /*
    public static Scene scene5(double xResolution, double yResolution, int frame){
        Camera cam;
        if(frame <= 100){
            cam = new Camera(new Point(0+(.7*frame),20,0-(.5*frame)),       // camera location
                                new Vector(0-(.01*frame),0,-1+(.01*frame)),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                50,                     // field of view
                                xResolution/yResolution, // aspect ratio
                                2.2,                     //lens size
                                40);                    //focal length
        }
        else{
            cam = new Camera(new Point(70-(.7*(frame-100)),20,-50+(.5*(frame-100))),       // camera location
                                new Vector(-1+(.01*(frame-100)),0,0-(.01*(frame-100))),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                50,                     // field of view
                                xResolution/yResolution, // aspect ratio
                                2.2,                     //lens size
                                40);                    //focal length
        }
        Scene s = new Scene(cam);                        
                                
        Light l1 = new PointLight(new Color(.5+(.0025*frame),.5-(.0005*frame),.5+(.0015*frame)), new Point(50, 100, -20));
        Light l2 = new PointLight(new Color(.5-(.0025*frame),.5+(.001*frame),.5+(.0018*frame)), new Point(-50, 100, -20));
        Light l3 = new SpotLight(new Color(.5, .5, .5), new Point(0, 20, 0), 20, new Vector(0, 1, -1), 50);
        Light l4;
        if(frame <= 66){
            l4 = new PointLight(new Color(.5,.5,.5), new Point(0+(1.5*frame), 100, -99));
        }
        else{
            l4 = new PointLight(new Color(.5,.5,.5), new Point(100-(1.5*(frame-66)), 100, -99));
        }
        s.addLight(l1);
        s.addLight(l2);
        s.addLight(l3);
        s.addLight(l4);
        
        Color grey = new Color(.8,.8,.8);        
        
        Surface wall1 = new Rectangle(new Point(-100,0,-100), new Point(100,0,-100), 
                                      new Point(-100,100,-100), new Point(100,100,-100), 
                                      new Lambert(grey));
                                      
        Surface wall2 = new Rectangle(new Point(-100,0,100), new Point(-100,0,-100), 
                                      new Point(-100,100,100), new Point(-100,100,-100), new Lambert(grey));
                                      
        Surface wall3 = new Rectangle(new Point(100,0,100), new Point(100,0,-100), 
                                      new Point(100,100,100), new Point(100,100,-100), new Lambert(grey));
                                      
        Surface wall4 = new Rectangle(new Point(-100, 0, 100), new Point(100, 0, 100),
                                      new Point(-100, 100, 100), new Point(100, 100, 100), new Lambert(grey));
                    
        s.addSurface(wall1);
        s.addSurface(wall2);
        s.addSurface(wall3);
        s.addSurface(wall4);
        Surface floor = new Rectangle(new Point(-100,0,-100), new Point(100,0,-100), 
                                      new Point(-100,0,100), new Point(100,0,100), new Lambert(grey));
        s.addSurface(floor);
        
        Surface s1 = new Sphere(new Point(20, 10, -40), 0+(.05*frame), new Phong(new Color(.14, .72, 1), grey, 10));
        Surface s2 = new Sphere(new Point(0, 6, -60), 6, new MirrorPhong(new Color(233/255.0, 153/255.0, 1), grey, 10, .4, 0));
        //Surface s2 = new Sphere(new Point(0, 6, -60), 6, new Lambert(new Color(233/255.0, 153/255.0, 1)));
        Surface s3 = new Sphere(new Point(0, 70, -50), 20, new MirrorPhong(new Color(.6,.6,.6), grey, 10, 1, 0));
        Surface s4 = new Cone(new Point(80, 0, -80), new Point(80, 40, -80), 15, new Phong(new Color(253/255.0, 1, 117/255.0), grey, 10));
        Surface s5 = new Sphere(new Point(-10, 5, -20), 5, new Lambert(new Color(.6, .4, .2)));
        //Surface s6 = new 
        s.addSurface(s1);
        s.addSurface(s2);
        s.addSurface(s4);
        s.addSurface(s3);
        s.addSurface(s5);
        
        Surface mirror = new Rectangle(new Point(-30, 0, -20), new Point(-10, 0, -70),
                                       new Point(-30, 40, -20), new Point(-10, 40, -70), 
                                       new MirrorPhong(new Color(.7,.7,.7), grey, 10, .8, 0+(.0005*frame)));
        s.addSurface(mirror);
        return s;
    }
    */
    public static Scene texturesScene(double xResolution, double yResolution, int frame){
        Camera cam = new Camera(new Point(40,30,40),       // camera location
                                new Vector(-4,-3,-4),     // forward vector/view direction
                                new Vector(-4,3,-4),      // up vector
                                60,                     // field of view
                                xResolution/yResolution, // aspect ratio
                                0,                     //lens size
                                40);                    //focal length
        Scene s = new Scene(cam);
        
        Light l1 = new PointLight(new Color(1,1,1), new Point(0, 8, 0));
        Light l2 = new PointLight(new Color(1,1,1), new Point(0, -8, 0));
        Light l3 = new PointLight(new Color(1,1,1), new Point(0, 0, 8));
        Light l4 = new PointLight(new Color(1,1,1), new Point(0, 0, -8));
        Light l5 = new PointLight(new Color(1,1,1), new Point(8, 0, 0));
        Light l6 = new PointLight(new Color(1,1,1), new Point(-8, 0, 0));
        s.addLight(l1);
        s.addLight(l2);
        s.addLight(l3);
        s.addLight(l4);
        s.addLight(l5);
        s.addLight(l6);
        
        /*
         * FOR ALL PLANETS:
         * The x corresponds to cos (like unit circle)
         * Z corresponds to sin (using the x-z plane instad of x-y)
         * The speed of the planet will be scaled by 2* planet num
         */
        
        //sun
        Texture sunT = new Texture("sun.jpeg");
        Surface sun = new Sphere(new Point(0,0,0), 4, sunT, new Vector(0,0,1), new Vector(0,1,0));
        s.addSurface(sun);
        
        //mercury
        double mercuryA = Math.toRadians(14*frame);
        double mercuryX = Math.cos(mercuryA)*-10;
        double mercuryZ = Math.sin(mercuryA)*-10;
        Texture mercuryT = new Texture("mercury.jpeg");
        Surface mercury = new Sphere(new Point(mercuryX,0,mercuryZ),2, mercuryT, new Vector(0,0,1), new Vector(0,1,0));
        Surface mercuryOrbit = new Ring(new Point(0,0,0), 10.2, 9.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        s.addSurface(mercury);
        s.addSurface(mercuryOrbit);
        
        //venus
        double venusA = Math.toRadians(12*frame);
        double venusX = Math.cos(venusA)*-16;
        double venusZ = Math.sin(venusA)*-16;
        Texture venusT = new Texture("venus.jpeg");
        Surface venus = new Sphere(new Point(venusX,0,venusZ),3, venusT, new Vector(0,0,1), new Vector(0,1,0));
        Surface venusOrbit = new Ring(new Point(0,0,0), 16.2, 15.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        s.addSurface(venusOrbit);
        s.addSurface(venus);
        
        //earth
        double earthA = Math.toRadians(10*frame);
        double earthX = Math.cos(earthA)*-24;
        double earthZ = Math.sin(earthA)*-24;
        Texture earthT = new Texture("earth.jpeg");
        Surface earth = new Sphere(new Point(earthX,0,earthZ),3.5, earthT, new Vector(0,0,1), new Vector(0,1,0));
        Surface earthOrbit = new Ring(new Point(0,0,0), 24.2, 23.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        s.addSurface(earthOrbit);
        s.addSurface(earth); 
        
        //mars
        double marsA = Math.toRadians(8*frame);
        double marsX = Math.cos(marsA)*-30;
        double marsZ = Math.sin(marsA)*-30;
        Texture marsT = new Texture("mars.jpeg");
        Surface mars = new Sphere(new Point(marsX,0,marsZ),2.7, marsT, new Vector(0,0,1), new Vector(0,1,0));
        Surface marsOrbit = new Ring(new Point(0,0,0), 30.2, 29.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        s.addSurface(marsOrbit);
        s.addSurface(mars);
        
        //jupiter
        double jupiterA = Math.toRadians(6*frame);
        double jupiterX = Math.cos(jupiterA)*-40;
        double jupiterZ = Math.sin(jupiterA)*-40;
        Texture jupiterT = new Texture("jupiter.jpeg");
        Surface jupiter = new Sphere(new Point(jupiterX,0,jupiterZ),5, jupiterT, new Vector(0,0,1), new Vector(0,1,0));
        Surface jupiterOrbit = new Ring(new Point(0,0,0), 40.2, 39.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        s.addSurface(jupiterOrbit);
        s.addSurface(jupiter);
        
        //saturn
        double saturnA = Math.toRadians(4*frame);
        double saturnX = Math.cos(saturnA)*-50;
        double saturnZ = Math.sin(saturnA)*-50;
        Point saturnPos = new Point(0,0,-50);
        Texture saturnT = new Texture("saturn.jpeg");
        Surface saturn = new Sphere(new Point(saturnX,0,saturnZ), 4, jupiterT, new Vector(0,0,1), new Vector(0,1,0));
        Surface saturnOrbit = new Ring(new Point(0,0,0), 50.2, 49.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        Surface saturnRing = new Ring(new Point(0,0,-50), 6, 5.8, new Vector(1,1,1), new Lambert(new Color(194/255, 139/255, 83/255)));
        s.addSurface(saturnRing);
        s.addSurface(saturnOrbit);
        s.addSurface(saturn);
        
        //Uranus
        double uranusA = Math.toRadians(2*frame);
        double uranusX = Math.cos(uranusA)*-58;
        double uranusZ = Math.sin(uranusA)*-58;
        
        Texture uranusT = new Texture("uranus.jpeg");
        Surface uranus = new Sphere(new Point(uranusX,0,uranusZ),3.5, uranusT, new Vector(0,0,1), new Vector(0,1,0));
        Surface uranusOrbit = new Ring(new Point(0,0,0), 58.2, 57.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        s.addSurface(uranusOrbit);
        s.addSurface(uranus);
        
        //neptune
        double neptuneAngle = Math.toRadians(frame);
        double neptuneX = Math.cos(neptuneAngle)*-65;
        double neptuneZ = Math.sin(neptuneAngle)*-65;
        
        Texture neptuneT = new Texture("neptune.jpeg");
        Surface neptune = new Sphere(new Point(neptuneX,0,neptuneZ),3, neptuneT, new Vector(0,0,1), new Vector(0,1,0));
        Surface neptuneOrbit = new Ring(new Point(0,0,0), 65.2, 64.8, new Vector(0,1,0), new Phong(new Color(.9,.9,.9), new Color(1, 1, 1), 10));
        s.addSurface(neptuneOrbit);
        s.addSurface(neptune);
        
        return s;
    }
    public static Scene cornellBox(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(0,25,40),       // camera location
                                new Vector(0,0,-1),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                45,                     // field of view
                                xResolution/yResolution, // aspect ratio
                                0,                     //lens size
                                40); 
        Scene s = new Scene(cam);
                                
        //Light l1 = new LightBulb(new Color(.7,.7,.7), new Point(0, 50, 10), 5);
        Light l1 = new LightBulb(new Color(184/255.0, 165/255.0, 136/255.0), new Point(0, 50, -15), 5);
        s.addLight(l1);
        
        Color grey = new Color(.8,.8,.8);        
        
        Surface wall1 = new Rectangle(new Point(-50,0,-50), new Point(50,0,-50), 
                                      new Point(-50,100,-50), new Point(50,100,-50), 
                                      new Lambert(grey));
                                      
        Surface wall2 = new Rectangle(new Point(-50,0,50), new Point(-50,0,-50), 
                                      new Point(-50,100,50), new Point(-50,100,-50), new Lambert(new Color(.8, .1, .1)));
                                      
        Surface wall3 = new Rectangle(new Point(50,0,50), new Point(50,0,-50), 
                                      new Point(50,100,50), new Point(50,100,-50), new Lambert(new Color(.1, .8, .1)));
                                      
        Surface wall4 = new Rectangle(new Point(50, 75, 50), new Point(-50, 75, 50),
                                      new Point(50, 0, 50), new Point(-50, 0, 50), new Lambert(grey));
                                      
        s.addSurface(wall1);
        s.addSurface(wall2);
        s.addSurface(wall3);
        s.addSurface(wall4);
        Surface floor = new Rectangle(new Point(-50,0,50), new Point(50,0,50), 
                                      new Point(-50,0,-50), new Point(50,0,-50), new Lambert(grey));
                                      
        Surface roof = new Rectangle(new Point(-50,75,50), new Point(50,75,50), 
                                      new Point(-50,75,-50), new Point(50,75,-50), new Lambert(grey));
        
        s.addSurface(roof);                              
        s.addSurface(floor);
        
        Surface cube1 = new Cube(new Point(-30, 0, -25), new Vector(0, 30, 0), new Vector(10, 0, -4), new Vector(-10, 0, -10), new Lambert(grey));
        s.addSurface(cube1);
        Surface cube2 = new Cube(new Point(15, 0, -15), new Vector(0, 15, 0), new Vector(25, 0, -8), new Vector(-10, 0, -20), new Lambert(grey));
        s.addSurface(cube2);
        Surface sphere1 = new Sphere(new Point(-5, 7, -7), 7, new Glass(new Color(0, 0, 0), grey, 15, .15, .2, 0, false));
        s.addSurface(sphere1);
        
        return s;
    }
}
