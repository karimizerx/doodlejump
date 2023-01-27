package gameobjects;

// Import de packages java

// Import de packages projet

// La classe Plateforme représente une plateforme (qui peut être de différents types).
public abstract class Plateforme extends GameObject { // C'est un GameObject

    double saut; // Constante de saut, différente en fonction de la plateforme


    /**
     * @param x
     * @param y
     * @param w
     * @param h
     * @param saut
     */
    public Plateforme(double x, double y, double w, double h, double saut) {
        super(x, y, 50, 10);
        this.saut = saut;
    }

}