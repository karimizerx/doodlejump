package gameobjects;

public class Monstre extends GameObject{

    private double dx; // Vitesse en x
    private int id;
    public Monstre(double x, double y, double w, double h, double dx,int id) {
        super(x, y, w, h);
        this.dx=dx;
        this.id=id;
    }

    public void move(Terrain t) { // Modifie la position en x
        this.setX(this.getX() + dx);
        if (this.getX() >= t.getWidth() - this.getWidth() || this.getX() <= 0) {
            dx = -dx;
        }
    }

    public int getId(){
        return id;
    }
    
}
