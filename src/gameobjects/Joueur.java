package gameobjects;

// Cette classe représente la personne qui joue.
public class Joueur {

    private int score;
    private Personnage perso;
    private String nom;

    public Joueur(Personnage p, String name) {
        this.perso = p;
        this.score = 0;
        this.nom = name;
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