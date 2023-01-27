package gameobjects;

// Import de packages java

// Import de packages projet

// La classe Plateforme représente une plateforme (qui peut être de différents types).
public abstract class Plateforme extends GameObject { // C'est un GameObject

    double saut; // Constante de saut, différente en fonction de la plateforme

    public Plateforme(double x, double y, double w, double h, double dx, double dy, double saut) {
        super(x, y, 50, 10, dx, dy);
        this.saut = saut;
    }

}