package gameobjects;

// MovingPlateforme est une plateforme avec une vitesse en x.
public class MovingPlateforme extends Plateforme {

    public MovingPlateforme(double x, double y, double w, double h, double saut, double dx) {
        super(x, y, w, h, saut, dx);
    }
}