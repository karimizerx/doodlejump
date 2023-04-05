package gameobjects;

// Un Helicoptere est un item qui augmente la vitesse de montée (le saut).
public class Helicoptere extends Items {

    public Helicoptere(double x, double y, double w, double h, double saut, double plc) {
        super(x, y, w, h, saut, false, plc);
    }
}