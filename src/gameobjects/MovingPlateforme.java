package gameobjects;

public class MovingPlateforme extends Plateforme {

    private double dx;

    public MovingPlateforme(double x, double y, double w, double h, double saut, double dx) {
        super(x, y, w, h, saut);
        this.dx = dx;
    }

    
     public void move(Terrain t) { // Modifie la position en x
        this.setX(this.getX() + dx);
        if(this.getX() >= t.getWidth()-this.getWidth() || this.getX() <=0){
            dx=-dx;
        } 
    }
    

    // Getter

    public double getDx() {
        return dx;
    }
}