package gameobjects;

// Un item augmente la vitesse de montée (le saut)
public abstract class Items extends GameObject {

    private double saut; // Constante de saut (différente en fonction de l'item).
    private boolean isNeedPied; // Indique si l'objet doit être touché avec les pieds (ressort...)

    public Items(double x, double y, double w, double h, double s, boolean p) {
        super(x, y, w, h);
        this.saut = s;
        this.isNeedPied = p;
    }

    public abstract void runEffect(Personnage p);


    // Getter & Setter
    public double getSaut() {
        return saut;
    }

    public void setSaut(double saut) {
        this.saut = saut;
    }

    public boolean isNeedPied() {
        return isNeedPied;
    }

    public void setNeedPied(boolean isNeedPied) {
        this.isNeedPied = isNeedPied;
    }

}