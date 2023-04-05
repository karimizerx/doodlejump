package gameobjects;

// Import de packages java
import java.util.*;

// Import d'autres dossiers
import gui.*;

// Le personnage est l'objet principale du jeu, pour chaque joueur.
public class Personnage extends GameObject {

    private double dx, dy, ralentissement; // Vitesse en x et en y.
    // Pour l'inertie, ralentissement gère le ralentissement de la vitesse du perso.
    // isRight/Left gère les boutons appuyés, isInert gère le relâchement.
    // isShoot & canShoot indique si l'on tire/peut tirer un projectile.
    // collides indique si l'on a PAS touché un monstre.
    private boolean isRight, isInertRight, isLeft, isInertLeft, isShoot, canShoot, collides;
    private ArrayList<Projectile> listProjectiles; // Stock tous les projectiles du personnage encore sur le terrain.
    private Items item; // Peut être équipé d'un item (jetpack...).

    public Personnage(double x, double y, double w, double h, double dy) {
        super(x, y, w, h);
        this.dy = dy;
        this.dx = 0;
        this.isRight = false;
        this.isLeft = false;
        this.isInertRight = false;
        this.isInertLeft = false;
        this.isShoot = false;
        this.canShoot = true;
        this.listProjectiles = new ArrayList<Projectile>();
        this.ralentissement = 0;
        this.collides = true;
    }

    // Méthodes de la classe
    /*
     * Conditions d'un collides :
     * coordoonnées du perso : x,xw (= x + w) & y,yh (= y + h)
     * coordoonnées de l'objet : ox,oxw (= x + w) & oy,oyh (= y + h)
     * x <= oxw (sinon impossible d'avoir collides)
     * xw >= ox (sinon impossible d'avoir collides)
     * y <= oyh (sinon impossible d'avoir collides)
     * yh >= oy (sinon impossible d'avoir collides)
     */

    // Colision entre le personnage et une plateforme.
    public void collides_plateforme(Plateforme pf, double deltaTime) {
        if (!collides) // Si on a collides un monstre, on n'applique plus les collides aux plateformes.
            return;
        if ((this.getX() + this.getWidth() * 0.65 >= pf.getX())
                // + w*0.65 sert à ne compter que le x du dernier pied.
                && (this.getX() + this.getWidth() * 0.25 <= pf.getX() + pf.getWidth())
                // + w*0.25 sert à ne compter que le x du premier pied.
                && (this.getY() + this.getHeight() * 0.87 >= pf.getY())
                // + h*0.87 sert à ne compter que le y des pieds.
                && (this.getY() + this.getHeight() * 0.87 <= pf.getY() + pf.getHeight())
                && (this.getDy() > 0)) { // Si le personnage descend.
            this.dy = pf.getSaut() * deltaTime; // On incrémente la vitesse (simulation de saut).
        }
    }

    // Colision entre le personnage et un item.
    public boolean collides_item(Items it, double deltaTime) {
        if (!collides) // Si on a collides un monstre, on n'applique plus les collides aux items.
            return false;

        if (it.isNeedPied()) { // Si l'item doit être touché avec les pieds
            if ((this.getX() + this.getWidth() * 0.65 >= it.getX())
                    // + w*0.65 sert à ne compter que le x du dernier pied.
                    && (this.getX() + this.getWidth() * 0.25 <= it.getX() + it.getWidth())
                    // + w*0.25 sert à ne compter que le x du premier pied.
                    && (this.getY() + this.getHeight() * 0.87 >= it.getY())
                    // + h*0.87 sert à ne compter que le y des pieds.
                    && (this.getY() + this.getHeight() * 0.87 <= it.getY() + it.getHeight())
                    && (this.getDy() > 0)) { // Si le personnage descend.
                this.dy = it.getSaut() * deltaTime; // On incrémente la vitesse (simulation de saut).
                return true;
            }
        } else {
            if ((this.getX() + this.getWidth() * 0.65 >= it.getX())
                    // + w*0.65 sert à ne compter que le x du dernier pied.
                    && (this.getX() + this.getWidth() * 0.25 <= it.getX() + it.getWidth())
                    // + w*0.25 sert à ne compter que le x du premier pied.
                    && (this.getY() + this.getHeight() * 0.87 >= it.getY())
                    // + h*0.87 sert à ne compter que le y des pieds.
                    && (this.getY() <= it.getY() + it.getHeight())) {
                this.dy = it.getSaut() * deltaTime; // On incrémente la vitesse (simulation de saut).
                return true;
            }
        }

        return false;
    }

    // Colision entre le personnage et un coins.
    public boolean collides_coin(Coins c, double deltaTime) {
        if (!collides) // Si on a collides un monstre, on n'applique plus les collides aux coins.
            return false;

        if (this.item != null) // Si on a un jetPack, on collecte automatiquement.
            return true;

        if ((this.getX() + this.getWidth() * 0.65 >= c.getX())
                // + w*0.65 sert à ne compter que le x du dernier pied.
                && (this.getX() + this.getWidth() * 0.25 <= c.getX() + c.getWidth())
                // + w*0.25 sert à ne compter que le x du premier pied.
                && (this.getY() + this.getHeight() * 0.87 >= c.getY())
                // + h*0.87 sert à ne compter que le y des pieds.
                && (this.getY() <= c.getY() + c.getHeight())) {
            return true;
        }

        return false;
    }

    // Colision entre le personnage et un monstre.
    public boolean collides_monstre(Monstre m, double deltaTime) {
        if (!collides || this.item != null) // Si on a un jetpack, on ne meurs pas en touchant les montres.
            return false;

        // Cas du rebond sur le monstre.
        if ((this.getX() + this.getWidth() >= m.getX())
                && (this.getX() <= m.getX() + m.getWidth())
                && (this.getY() + 0.87 * this.getHeight() >= m.getY())
                // + h*0.87 sert à ne compter que le y des pieds.
                && (this.getY() + 0.87 * this.getHeight() <= m.getY() + m.getHeight() * 0.1)
                && (this.getDy() > 0)) { // Si le personnage descend.
            this.dy = m.getSaut() * deltaTime;
            return true;
        }

        // Cas de collides & mort.
        if ((this.getX() + this.getWidth() >= m.getX())
                && (this.getX() + this.getWidth() <= m.getX() + m.getWidth())
                && (this.getY() + this.getHeight() * 0.87 >= m.getY())
                // + h*0.87 sert à ne compter que le y des pieds.
                && (this.getY() <= m.getY() + m.getHeight())) {
            this.collides = false;
            return false;
        }

        return false;
    }

    // Colision entre les projectiles et un monstre.
    public boolean collides_projectile(Monstre m) {
        for (Projectile p : listProjectiles) { // True si l'un des projectiles à collides.
            if ((p.getX() + p.getWidth() >= m.getX())
                    && (p.getX() + p.getWidth() <= m.getX() + m.getWidth())
                    && (p.getY() + p.getHeight() >= m.getY())
                    && (p.getY() <= m.getY() + m.getHeight())) {
                return true;
            }
        }
        return false;
    }

    // Tir un projectile avec ce personnage.
    public void tirer(double w, double h, double vx, double vy) {
        this.listProjectiles.add(new Projectile(this.getX() + this.getWidth() * 0.43, this.getY(), w, h, vx, vy));
    }

    // Règle le ralentissement (inertie activée ou pas).
    public void setInertie(Vue vue) {
        if (vue == null) // Sans d'inertie.
            this.ralentissement = this.getDx() < 0 ? -this.getDx() : this.getDx();
        else // Avec inertie.
            this.ralentissement = 0.000375 * vue.getWidth();
    }

    // Getter & Setter

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean isRight) {
        this.isRight = isRight;
    }

    public boolean isInertRight() {
        return isInertRight;
    }

    public void setInertRight(boolean isInertRight) {
        this.isInertRight = isInertRight;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public boolean isInertLeft() {
        return isInertLeft;
    }

    public void setInertLeft(boolean isInertLeft) {
        this.isInertLeft = isInertLeft;
    }

    public boolean isShoot() {
        return isShoot;
    }

    public void setShoot(boolean isShoot) {
        this.isShoot = isShoot;
    }

    public ArrayList<Projectile> getListProjectiles() {
        return listProjectiles;
    }

    public void setListProjectiles(ArrayList<Projectile> listProjectiles) {
        this.listProjectiles = listProjectiles;
    }

    public boolean iscanShoot() {
        return canShoot;
    }

    public void setcanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public double getRalentissement() {
        return ralentissement;
    }
}