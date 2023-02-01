package gameobjects;

// Import de packages java :
import java.util.Timer;
import java.util.TimerTask;

public class Helicoptere extends Items {

    public Helicoptere(double x, double y, double w, double h, int t) {
        super(x, y, w, h, t);
        this.setId("helicoptere.png");
    }

    // Méthodes de la classe
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
        timer.schedule(task, this.getTime());
        p.setDy(0);
    }
}