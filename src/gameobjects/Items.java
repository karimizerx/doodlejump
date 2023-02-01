package gameobjects;

public abstract class Items extends GameObject {

    private int time;
    private String id; // Identifiant de l'image correspondante

    public Items(double x, double y, double w, double h, int t) {
        super(x, y, w, h);
        this.time = t;
    }

    public abstract void runEffect(Personnage p);

    // Getter & Setter

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}