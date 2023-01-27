package gameobjects;

public class Joueur {

    int score;
    Personnage perso;
    public Joueur(double x, double y, double w, double h, double dy) {
        this.perso = new Personnage();
    }
}
