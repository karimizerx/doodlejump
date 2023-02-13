package gameobjects;

import gui.Skin;

// Représente une plateforme (qui peut être de différents types).
public abstract class Plateforme extends GameObject {

    private double saut; // Constante de saut (différente en fonction de la plateforme)
    private Skin skin;
    public Plateforme(double x, double y, double w, double h, double saut, Skin skin) {
        super(x, y, w, h);
        this.saut = saut;
        this.skin = skin;
    }

    // Getter & Setter

    public double getSaut() {
        return saut;
    }

    public void setSaut(double saut) {
        this.saut = saut;
    }

    public void move(Terrain t) {
        return;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }

}