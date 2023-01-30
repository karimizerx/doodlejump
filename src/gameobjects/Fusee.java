package gameobjects;

import java.util.Timer;
import java.util.TimerTask;

public class Fusee extends Items {

    public Fusee(double x, double y, double w, double h, int t) {
        super(x, y, w, h, t);
    }

    @Override
    public void runEffect(Personnage p) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            double V = 100;// vitesse de l'item, a voir

            @Override
            public void run() {
                p.setDy(V);
            }
        };
        timer.schedule(task, time);
        p.setDy(0);
    }

}
