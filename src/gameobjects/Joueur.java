package gameobjects;

// Import de package java
import java.io.Serializable;

// Import d'autres dossiers
import leaderboard.*;

// Cette classe représente la personne qui joue.
public class Joueur implements Serializable {

    private String id, nom; // L'identifiant (unique) & le nom du joueur.
    private int score; // Son score à cette partie.
    private Personnage perso; // Le personnage utilisé.
    private int monnaie; // Compteur de pièces (coins).

    public Joueur(Personnage p, String name) {
        History h = new History();
        if (h.getLbData().size() > 0) // Si le joueur local a déjà un id.
            this.id = h.getLbData().get(h.getLbData().size() - 1)[0];
        else {
            Classement c = new Classement(); // Sinon on lui en génère un.
            this.id = (c.getLbData().size() == 0) ? "0" : String.valueOf(c.getMaxId() + 1);
        }
        this.nom = name; // Nom par défaut est Mizer ou nom de la dernière partie.
        this.perso = p;
        this.score = 0;
        this.monnaie = 0;
    }

    // Méthodes de la classe

    public void addCoin() {
        monnaie++;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(int monnaie) {
        this.monnaie = monnaie;
    }

}