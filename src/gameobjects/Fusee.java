package gameobjects;

import java.util.Timer;
import java.util.TimerTask;

// Une Fusée est un item qui augmente la vitesse de montée (le saut)
public class Fusee extends Items {

    long time;
    public Fusee(double x, double y, double w, double h, double saut,long time) {
        super(x, y, w, h, saut, false);
        this.time=time*1000;

    }
    // Méthode move !

    public void runEffect(Personnage p) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            double V = 100;// vitesse de l'item, a voir

            @Override
            public void run() {
                p.setDy(V);
            }
        };
        timer.schedule(task, this.time);
        p.setDy(0);
    }


}