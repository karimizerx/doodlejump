package gameobjects;

// Un Item est un objet qui apporte bonus ou malus.
public abstract class Items extends GameObject { // C'est une classe abstraite.

    private double saut; // Constante de saut (différente en fonction de l'item).
    private boolean isNeedPied; // Indique si l'objet doit être touché avec les pieds (ressort...)
    private final double placement; // Indique à quel proportion de la plateforme se trouve l'item (en x).

    public Items(double x, double y, double w, double h, double s, boolean p, double plc) {
        super(x, y, w, h);
        this.saut = s;
        this.isNeedPied = p;
        this.placement = plc;
    }

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

    public double getPlacement() {
        return placement;
    }

}