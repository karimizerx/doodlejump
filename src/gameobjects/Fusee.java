package gameobjects;

import java.util.Timer;
import java.util.TimerTask;

// Une Fusée est un item qui augmente la vitesse de montée (le saut)
public class Fusee extends Items {

    long time;
    /**
     * @param x
     * @param y
     * @param w
     * @param h
     * @param saut
     * @param time
     */
    public Fusee(double x, double y, double w, double h, double saut,double time) {
        super(x, y, w, h, saut, false);
        this.time=(long)time*1000;

    }
    // Méthode move !

    public void runEffect(Personnage p) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                p.setDy(Fusee.this.getSaut());
                System.out.println(p.getDy());
            }
        };
        timer.schedule(task, this.time);
        p.setDy(0);
    }


}