package gameobjects;

// MovingPlateforme est une plateforme avec une vitesse en x.
public class MovingPlateforme extends Plateforme {


    public MovingPlateforme(double x, double y, double w, double h, double saut, double dx) {
<<<<<<< HEAD
        super(x, y, w, h, saut, dx);
=======
        super(x, y, w, h, saut);
        this.dx = dx;
    }

    // Méthodes de la classe :
    public void move(Terrain t) { // Modifie la position en x
        this.setX(this.getX() + dx);
        if (this.getX() >= t.getWidth() - this.getWidth() || this.getX() <= 0) {
            dx = -dx;
        }
    }

    // Getter

    public double getDx() {
        return dx;
>>>>>>> master
    }
}