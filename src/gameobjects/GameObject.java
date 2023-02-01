package gameobjects;

// Import de packages java

// Import de packages projet

// La classe GameObject représente les différents types d'objets du jeu.
public abstract class GameObject { // C'est une classe abstraite

    private double x, y; // Coordonnées position x,y
    private double w, h; // Largeur et hauteur de l'objet

    public GameObject(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // Getter & Setter

    public final double getWidth() {
        return w;
    }

    public final double getHeight() {
        return h;
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    public final double getLeft() {
        return x - w / 2;
    }

    public final double getRight() {
        return x + w / 2;
    }

    public final double getUp() {
        return y - h / 2;
    }

    public final double getDown() {
        return y + h / 2;
    }

    public void setX(double v) {
        x = v;
    }

    public void setY(double v) {
        y = v;
    }

    public void setW(double v) {
        w = v;
    }

    public void setH(double v) {
        h = v;
    }

    public void addX(double v) {
        x += v;
    }

    public void addY(double v) {
        y += v;
    }

    // Méthodes de la classe GameObject
}