package gameobjects;

public class Monstre extends GameObject{

    private double dx; // Vitesse en x
    private int id;
    private int health;
    public Monstre(double x, double y, double w, double h, double dx,int id) {
        super(x, y, w, h);
        // 70,50 pour id =1
        // 80,90 pour id =2
        this.dx=dx;
        this.id=id;
        health=id;
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

    public boolean shot(){
        health--;System.out.println(health);
            return health<=0;
    }
    
}
