package gameobjects;

public class Projectile extends GameObject {

    private double dx;
    private double dy;

    public Projectile(double x, double y, double w, double h, double dx, double dy) {
        super(x, y, w, h);
        this.dx = dx;
        this.dy = dy;
    }

    // Méthodes de la classe:
    public void collides_monster() {
        return;
    }

    public boolean limiteProjectile() {
        return this.getY() - this.getHeight() / 2 <= 0;
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