package gameobjects;

// Import de packages java :
import java.util.*;

import gui.Game;

// Le personnage est un objet avec vitesse.
public class Personnage extends GameObject {

    private double dx, dy; // Vitesse en x et en y
    // isRight/Left gère les boutons appuyés, isInert gère le relâchement
    // isShoot & canShoot indique si l'on tire/peut tirer un projectile.
    private boolean isRight, isInertRight, isLeft, isInertLeft, isShoot, canShoot;
    private ArrayList<Projectile> listProjectiles; // Stock tous les projectiles du personnage encore sur le terrain
    public boolean collides=true;
    private int monnaie;

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
        monnaie=0;
    }

    // Méthodes de la classe

    // Colision entre le personnage et une plateforme
    public void collides_plateforme(Plateforme pf, double deltaTime) {
        if(!collides) return;
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
    public void collides_item(Items it, double deltaTime) {
        if(!collides) return;
        if (it.isNeedPied()) {
            if ((this.getX() + (this.getWidth() * 0.65) >= it.getX()) // si ça ne dépasse pas par la gauche de l'item.
                    // + witdh*0.65 sert à ne compter que le x du dernier pied
                    && (this.getX() + (this.getWidth() * 0.25) <= it.getX() + it.getWidth())
                    // si ça ne dépasse pas par la droite de la item.
                    // + witdh*0.25 sert à ne compter que le x du premier pied
                    && (this.getY() + 0.87 * this.getHeight() >= it.getY())
                    && (this.getY() + 0.87 * this.getHeight() <= it.getY() + it.getHeight())
                    && (this.getDy() > 0)) { // Si le personnage descend
                dy = it.getSaut();
            }
        } else {
            if ((this.getX() + (this.getWidth() * 0.65) >= it.getX()) // si ça ne dépasse pas par la gauche de l'item.
                    // + witdh*0.5 sert à ne compter que le x du dernier pied
                    && (this.getX() + (this.getWidth() * 0.25) <= it.getX() + it.getWidth())
                    // si ça ne dépasse pas par la droite de la item.
                    // + witdh*0.25 sert à ne compter que le x du premier pied
                    && (this.getY() <= (it.getY() + it.getHeight()))
                    && ((it.getY() + it.getHeight()) <= (this.getY() + this.getHeight()))
                    && (this.getDy() < 0)) { // Si le personnage monte
                dy = it.getSaut();
            }
        }

    }
    
    public boolean collides(GameObject m) {
        if(!collides) return false;    //0.7                       0.6         0.87
        boolean ver = (Math.abs((m.getY() - m.getHeight() / 2) - (this.getY() -this.getHeight() / 2)) < Math.abs((m.getHeight() + 0.87*this.getHeight()) / 2 ));
        boolean hor = (Math.abs((m.getX() + m.getWidth() *0.5/ 2)  - (this.getX() +  this.getWidth() / 2)) <((m.getWidth()  + this.getWidth()) / 2));
        //                              0.9        0.4
        return (ver && hor);   //TODO reparer les coeffs
    }
    public boolean projectileCollide(Monstre m){
       double epsilone = 3;
       for (Projectile p :listProjectiles){
            boolean ver = (Math.abs((m.getY() + m.getHeight() / 2) - (p.getY() + p.getHeight() / 2)) < ((m.getHeight() + p.getHeight()) / 2 + epsilone));
            boolean hor = (Math.abs((m.getX() + m.getWidth() / 2) - (p.getX() + p.getWidth() / 2)) < ((m.getWidth() + p.getWidth()) / 2 + epsilone));
            if (ver && hor){ 
                listProjectiles.remove(p);
                return true;
            }
       }
       return false;
    }
    public void dead(){
        this.collides=false;
        // Timer timer = new Timer();
        // TimerTask task = new TimerTask() {
        //     @Override
        //     public void run() {
        //         Personnage.this.setDy(0);
        //     }
        // };
        // timer.schedule(task, 3000);
        // task = new TimerTask() {
        //     @Override
        //     public void run() {
        //         Personnage.this.setDy(-1);
        //         // p.setCollide(false);
        //     }
        // };
        // timer.schedule(task, 1000);
        // task = new TimerTask() {
        //     @Override
        //     public void run() {
        //         Personnage.this.setDy(10);
        //         // p.setCollide(false);
        //     }
        // };
        // timer.schedule(task, 1000);
        // task=null;
    }

    // Tir un projectile avec ce personnage
    public void tirer(double w, double h, double vx, double vy) {
        this.listProjectiles.add(new Projectile(this.getX() + this.getWidth() * 0.43, this.getY(), w, h, vx, vy));
    }

    /*
     * @Override
     * public void move(double deltaT) {
     * // partie gravite
     * double g = 9.81;
     * double newX, newY;
     * dy = dy > -g ? (-g * deltaT + dy) : -g;// chute libre ;
     * 
     * this.setY((-g / 2) * (deltaT * deltaT) + dy * deltaT + this.getY());
     * 
     * // partie horizental
     * newX = dx * deltaT + this.getX();
     * // if(newX+this.getHeight()/2>)
     * this.setX(newX);
     * 
     * }
     */

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

    public void addCoin(){
        monnaie++;
    }
}