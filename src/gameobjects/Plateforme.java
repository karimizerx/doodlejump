package gameobjects;

// Représente une plateforme (qui peut être de différents types).
public abstract class Plateforme extends GameObject {

    private double saut; // Constante de saut (différente en fonction de la plateforme)

    public Plateforme(double x, double y, double w, double h, double saut) {
        super(x, y, w, h);
        this.saut = saut;
    }

    // Getter & Setter

    public double getSaut() {
        return saut;
    }

    public void setSaut(double saut) {
        this.saut = saut;
    }

    public void move(Terrain t){
        return;
    }

}