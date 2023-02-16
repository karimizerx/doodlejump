package gameobjects;

public class Projectile extends GameObject{

    private double dx;
    private double dy;

    public Projectile(double x, double y, double w, double h, double dx, double dy ) {
        super(x, y, w, h);
        this.dx=dx;
        this.dy=dy;
    }

    public void collides_monster(){
        return;   
    }

    public boolean limite(double y){
        return this.getY()<=y;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }


}

