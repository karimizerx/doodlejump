package gameobjects;

public class Joueur {

    int score;
    Personnage perso;

    public Joueur(double x, double y, double w, double h, double dy) {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Personnage getPerso() {
        return perso;
    }

    public void setPerso(Personnage perso) {
        this.perso = perso;
    }
}