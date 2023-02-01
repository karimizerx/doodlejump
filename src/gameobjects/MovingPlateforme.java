package gameobjects;

public class MovingPlateforme extends Plateforme implements Moveable {

    private final double dx;

    public MovingPlateforme(double x, double y, double w, double h, double saut, double dx) {
        super(x, y, w, h, saut);
        this.dx = dx;
    }

    @Override
    public void move(double deltaT) { // Modifie la position en x
        this.setX(this.getX() + dx * deltaT);
    }

    // Getter

    public double getDx() {
        return dx;
    }
}