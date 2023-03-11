package gameobjects;

import java.util.Timer;
import java.util.TimerTask;

// Un Helicoptere est un item qui augmente la vitesse de montée (le saut)
public class Helicoptere extends Items {

    long time;
    public Helicoptere(double x, double y, double w, double h, double saut,double off,double time) {
        super(x, y, w, h, saut, true,off);
        this.time=(long)time*1000;
    }
    // Méthode move !

    public void runEffect(Personnage p) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                p.setDy(Helicoptere.this.getSaut());
                // p.setCollide(false);
            }
        };
        timer.schedule(task, this.time);
        p.setDy(0);
        // p.setCollide(true);
    }

}