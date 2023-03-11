package gameobjects;

// Un item augmente la vitesse de montée (le saut)
public abstract class Items extends GameObject {

    private double saut; // Constante de saut (différente en fonction de l'item).
    private boolean NeedsHead; // Indique si l'objet doit être touché avec les pieds (ressort...)
    private double off;
    public Items(double x, double y, double w, double h, double s, boolean p, double off) {
        super(x, y, w, h);
        this.saut = s;
        this.NeedsHead = p;
        this.off=off;
    }

    public abstract void runEffect(Personnage p);


    // Getter & Setter
    public double getSaut() {
        return saut;
    }

    public void setSaut(double saut) {
        this.saut = saut;
    }

    public boolean getNeedsHead() {
        return NeedsHead;
    }

    public void setNeedsHead(boolean isNeedHead) {
        this.NeedsHead = isNeedHead;
    }
    public double getoffset(){
        return off;
    }

}