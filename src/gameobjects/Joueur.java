package gameobjects;

import java.io.Serializable;

public class Joueur implements Serializable{

    private int score;
    private Personnage perso;
    private String nom;

    public Joueur(Personnage p,String text) {
        this.perso = p;
        this.score = 0;
        nom=text;
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