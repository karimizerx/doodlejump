package gameobjects;

import java.io.Serializable;

// Représente un personnage. C'est un objet, avec vitesse
public class Personnage extends GameObject implements Serializable {

    private double dy;
    public boolean isRight=false, isLeft=false;

    public Personnage(double x, double y, double w, double h, double dy) {
        super(x, y, w, h);
        this.dy = dy;
    }


    public void collides_plateforme(Terrain rainT) {
        for (Plateforme pf : rainT.getPlateformesListe()) {
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


    // Getter & Setter



    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

}