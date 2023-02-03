package gameobjects;

public class MovingPlateforme extends Plateforme {

    private final double dx;

    public MovingPlateforme(double x, double y, double w, double h, double saut, double dx) {
        super(x, y, w, h, saut);
        this.dx = dx;
    }



    // Getter

    public double getDx() {
        return dx;
    }
}