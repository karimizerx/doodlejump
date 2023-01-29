package gameobjects;

import java.util.Random;

public class Terrain {

    Plateforme[] pl;
    double mindensite, densite, maxdensite;

    public Terrain(double d) {
        densite = d;
        mindensite = 5;
        maxdensite = 15;
    }

    public void createLeR1T() {
        for (int i = 0; i < 10; i++) {
            pl[i] = new PlateformeBase(new Random().nextInt(500), new Random().nextInt(new Random().nextInt(833)), i, i, i, i)
        }
    }

}