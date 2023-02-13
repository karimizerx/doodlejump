package gameobjects;

import gui.Skin;

// Cette classe représente la personne qui joue.
public class Joueur {

    private int score;
    private Personnage perso;
    private String nom;
    private Skin skin;

    public Joueur(Personnage p, String name, Skin skin) {
        this.perso = p;
        this.score = 0;
        this.nom = name; // Nom par défaut est Mizer
        this.skin = skin;
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

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}