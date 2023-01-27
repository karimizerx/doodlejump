package gameobjects;

public interface Moveable {

    /**
     * @param deltaT temps passé
     * Modifie les champs x et y
     */
    public void move(double deltaT);
}
