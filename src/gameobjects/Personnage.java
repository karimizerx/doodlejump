package gameobjects;

// Représente un personnage. C'est un objet, avec vitesse
public class Personnage extends GameObject implements Moveable {

    private double dx, dy;

    public Personnage(double x, double y, double w, double h, double dy) {
        super(x, y, w, h);
        this.dy = dy;
        this.dx = 0;
    }

    // Méthodes de la classe
    @Override
    public void move(double deltaT) {
        // partie gravite
        double g = 9.81;
        double newX, newY;
        dy = dy > -g ? (-g * deltaT + dy) : -g;// chute libre ;

        this.setY((-g / 2) * (deltaT * deltaT) + dy * deltaT + this.getY());

        // partie horizental
        newX = dx * deltaT + this.getX();
        // if(newX+this.getHeight()/2>)
        this.setX(newX);

    }

    public void collides_plateforme(Terrain rainT) {
        for (Plateforme pf : rainT.getPlateformesListe()) {
            /*
             * if ((this.getX() + this.getWidth() - 30 > pf.getX()) &&
             * (this.getX() - 30 < pf.getX() + pf.getWidth()) &&
             * (this.getY() + this.getHeight() > pf.getY()) &&
             * (this.getY() + this.getHeight() < pf.getY() + pf.getHeight()) &&
             * (this.getDy() > 0)) {
             * dy = pf.getSaut();
             * }
             */
            if ((this.getX() + (this.getWidth() * 0.5) >= pf.getX()) // si ça ne dépasse pas par la gauche de la
                    // plateforme. + witdh*0.5 sert à ne compter que le x du dernier pied
                    && (this.getX() + (this.getWidth() * 0.3) <= pf.getX() + pf.getWidth())
                    // si ça ne dépasse pas par la droite de la plateforme.
                    // + witdh*0.5 sert à ne compter que le x du premier pied
                    && (this.getY() + 0.7 * this.getHeight() >= pf.getY())
                    && (this.getY() + 0.7 * this.getHeight() <= pf.getY() + pf.getHeight() * 0.7)
                    && (this.getDy() > 0)) {
                dy = -10;
            }
        }
    }

    public void collides_item(Terrain rainT) {
        for (Plateforme pf : rainT.getPlateformesListe()) {
            if ((this.getX() + this.getWidth() - 30 > pf.getX()) &&
                    (this.getX() < pf.getX() + 50) &&
                    (this.getY() + this.getHeight() > pf.getY()) &&
                    (this.getY() + this.getHeight() < pf.getY() + pf.getHeight()) &&
                    (this.getDy() > 0)) {
                dy = pf.getSaut();
            }
        }
    }

    public boolean collides(GameObject go) {
        double epsilone = 0.0; // marge d'erreur qu'on se donne
        boolean val = false;
        if (go.getClass().getName().equals("Items")) {
            Items item = (Items) go;
            val = (// on test si ils se chevauchent horizentalement
            (this.getX() >= item.getX() && this.getX() <= item.getX() + item.getWidth())
                    ||
                    (this.getX() + this.getWidth() >= item.getX()
                            && this.getX() + this.getWidth() <= item.getX() + item.getWidth()))
                    &&
                    // condition vertical depends du type
                    Math.abs(this.getY() - item.getY()) < epsilone;
            if (val) {
                item.runEffect(this);
            }

        } else if (go.getClass().equals(this)) {
            Plateforme plateforme = (Plateforme) go;
            val = (// on test si ils se chevauchent horizentalement
            (this.getX() >= plateforme.getX() && this.getX() <= plateforme.getX() + plateforme.getWidth())
                    ||
                    (this.getX() + this.getWidth() >= plateforme.getX()
                            && this.getX() + this.getWidth() <= plateforme.getX() + plateforme.getWidth()))
                    &&
                    // condition vertical depends du type
                    Math.abs(this.getY() - plateforme.getY() + plateforme.getHeight()) < epsilone
                    &&
                    dy > 0 // le personnage decends
            ;
            if (val) {
                dy = plateforme.getSaut();
            }
        }
        return val;
    }

    // Getter & Setter

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