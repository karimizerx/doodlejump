package gameobjects;

// Import de packages java

// Import de packages projet

// La classe Plateforme représente une plateforme (qui peut être de différents types).
public abstract class Plateforme extends GameObject { // C'est un GameObject

    Items item; // null s'il n'y a pas d'item.
    static double width = 50;
    static double height = 10;

    public Plateforme(double x, double y, double dx, double dy) {
        super(x, y, width, height, dx, dy);
    }

}