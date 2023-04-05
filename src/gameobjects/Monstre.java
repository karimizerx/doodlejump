package gameobjects;

// Un Monstre est un objet qui fait obstacle au joueur & qui peut être éliminé.
public class Monstre extends GameObject {

    private double saut; // Constante de saut (différente en fonction du monstre).
    private double dx; // Vitesse en x.
    private int id; // Identifiant pour chaque type de monstres.
    private int health; // Santé du monstre.

    public Monstre(double x, double y, double w, double h, double saut, double dx, int id) {
        super(x, y, w, h);
        // id = 1 : 70,50
        // id = 2 : 80,90
        this.dx = dx;
        this.id = id;
        this.health = id;
        this.saut = saut;
    }

    // Méthodes de la classe

    public void move(Terrain t) { // Modifie la position en x du monstre.
        this.setX(this.getX() + dx);
        if (this.getX() + this.getWidth() >= t.getWidth() || this.getX() <= 0) {
            this.dx = -this.dx;
        }
    }

    public boolean shot() {
        this.health--;
        return health <= 0;
    }

    // Getter & Setter

    public int getId() {
        return id;
    }

    public double getSaut() {
        return saut;
    }

    public void setSaut(double saut) {
        this.saut = saut;
    }

}
