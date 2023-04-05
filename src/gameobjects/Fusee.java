package gameobjects;

// Une Fusée est un item qui augmente la vitesse de montée (le saut).
public class Fusee extends Items {

    public Fusee(double x, double y, double w, double h, double saut, double plc) {
        super(x, y, w, h, saut, false, plc);
    }
}