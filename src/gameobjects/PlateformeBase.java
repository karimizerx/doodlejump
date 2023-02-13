package gameobjects;

import gui.Skin;

// PlateformeBase est un type de plateforme (basique)
public class PlateformeBase extends Plateforme {

    public PlateformeBase(double x, double y, double w, double h, double saut, Skin skin) {
        super(x, y, w, h, -10, skin);
    }
}