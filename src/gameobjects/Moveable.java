package gameobjects;

// Interface qui gère la mobilité 
public interface Moveable {

    /**
     * @param deltaT
     *               Temps passé
     *               Modifie les coordonnées x et y
     */

    public void move(double deltaT);
}