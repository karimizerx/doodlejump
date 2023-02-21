package gameobjects;

// Import de package java
import java.io.Serializable;

// Cette classe représente la personne qui joue.
public class Joueur implements Serializable {

    private int score;
    private Personnage perso;
    private String nom;

    public Joueur(Personnage p, String name) {
        this.perso = p;
        this.score = 0;
        this.nom = name; // Nom par défaut est Mizer ou 1er au classement

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}