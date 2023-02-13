package gameobjects;

// Le personnage est un objet avec vitesse.
public class Personnage extends GameObject {

    private double dx, dy; // Vitesse en x et en y

    public Personnage(double x, double y, double w, double h, double dy) {
        super(x, y, w, h);
        this.dy = dy;
        this.dx = 0;
    }

    // Méthodes de la classe

    // Colision entre le personnage et une plateforme
    public void collides_plateforme(Plateforme pf, double deltaTime) {
        if ((this.getX() + (this.getWidth() * 0.65) >= pf.getX()) // si ça ne dépasse pas par la gauche de la
                // plateforme. + witdh*0.65 sert à ne compter que le x du dernier pied
                && (this.getX() + (this.getWidth() * 0.25) <= pf.getX() + pf.getWidth())
                // si ça ne dépasse pas par la droite de la plateforme.
                // + witdh*0.25 sert à ne compter que le x du premier pied
                && (this.getY() + 0.87 * this.getHeight() >= pf.getY())
                && (this.getY() + 0.87 * this.getHeight() <= pf.getY() + pf.getHeight())
                && (this.getDy() > 0)) { // Si le personnage descent
            dy = pf.getSaut() * deltaTime;
        }
    }

    // Colision entre le personnage et un item
    public void collides_item(Items it) {
        if (it.isNeedPied()) {
            if ((this.getX() + (this.getWidth() * 0.5) >= it.getX()) // si ça ne dépasse pas par la gauche de l'item.
                    // + witdh*0.5 sert à ne compter que le x du dernier pied
                    && (this.getX() + (this.getWidth() * 0.3) <= it.getX() + it.getWidth())
                    // si ça ne dépasse pas par la droite de la item.
                    // + witdh*0.3 sert à ne compter que le x du premier pied
                    && (this.getY() + 0.7 * this.getHeight() >= it.getY())
                    && (this.getY() + 0.7 * this.getHeight() <= it.getY() + it.getHeight())
                    && (this.getDy() < 0)) { // Si le personnage monte
                dy = it.getSaut();
            }
        } else {
            if ((this.getX() + (this.getWidth() * 0.5) >= it.getX()) // si ça ne dépasse pas par la gauche de l'item.
                    // + witdh*0.5 sert à ne compter que le x du dernier pied
                    && (this.getX() + (this.getWidth() * 0.3) <= it.getX() + it.getWidth())
                    // si ça ne dépasse pas par la droite de la item.
                    // + witdh*0.3 sert à ne compter que le x du premier pied
                    && (this.getY() <= (it.getY() + it.getHeight()))
                    && ((it.getY() + it.getHeight()) <= (this.getY() + this.getHeight()))
                    && (this.getDy() < 0)) { // Si le personnage monte
                dy = it.getSaut();
            }
        }

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