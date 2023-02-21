package gameobjects;

// Représente une plateforme (qui peut être de différents types).
public abstract class Plateforme extends GameObject {

    private double saut; // Constante de saut (différente en fonction de la plateforme)
    private double dx;

    public Plateforme(double x, double y, double w, double h, double saut, double dx) {
        super(x, y, w, h);
        this.saut = saut;
    }

    // Getter & Setter


    public void move(Terrain t) { // Modifie la position en x
        this.setX(this.getX() + this.getDx());
        if (this.getX() >= t.getWidth() - this.getWidth() || this.getX() <= 0) {
            this.setDx(-getDx());
        }
    }

    public double getSaut() {
        return saut;
    }

    public void setSaut(double saut) {
        this.saut = saut;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }
    
}