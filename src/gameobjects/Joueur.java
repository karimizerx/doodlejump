package gameobjects;

public class Joueur {

    private int score;
    private Personnage perso;

    public Joueur(Personnage p) {
        this.perso = p;
        this.score = 0;
    }

    // Getter & Setter

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