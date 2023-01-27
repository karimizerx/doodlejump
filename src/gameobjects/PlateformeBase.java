package gameobjects;

// PlateformeBase est un type de plateforme (basique)
public class PlateformeBase extends Plateforme {

    private static String id = "pl0"; // Identifiant de l'image correspondante.

    public PlateformeBase(double x, double y, double w, double h, double saut) {
        super(x, y, w, h, saut);
    }

    // Getter & Setter
    public static String getId() {
        return id;
    }

}