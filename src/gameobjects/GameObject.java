package gameobjects;

// Import de packages java
import java.io.Serializable;

// La classe GameObject représente les différents types d'objets du jeu.
public abstract class GameObject implements Serializable { // C'est une classe abstraite.

    private double x, y; // Coordonnées position x,y.
    private double width, height; // Largeur et hauteur de l'objet.

    public GameObject(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    // Getter & Setter

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}