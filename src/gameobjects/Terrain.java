package gameobjects;

// Import de packages java
import java.util.ArrayList;
import java.util.Random;

// Import d'autres dossiers
import gameobjects.*;

public class Terrain {

    private ArrayList<Plateforme> plateformesListe;
    private Joueur joueur;
    private final double height, width;// dimensions du terrain
    private double y = 0;// hauteur du jeu. On l'utilisera aussi pour le score

    // Entre 0 et 1, indique de combien on fais monter le jeu
    private double advancement = 0.3;

    // Hauteur maximal que peut atteindre le personnage, maxHeight*height,
    // avant de faire monter le jeu.
    private double maxHeight = 0.8;

    /**
     * Baisse plus le score monte, affecte la densite des plateformes et la proba
     * qu'un helicoptere ou fusee apparaisse.
     * Inversement, affecte la proba d'apparition des monstres, sûrement on inverse
     * 1/difficulte
     */
    private double difficulty = 1.0;

    public Terrain(Joueur joueur, double height, double width) {
        this.plateformesListe = new ArrayList<Plateforme>();
        this.joueur = joueur;
        this.height = height;
        this.width = width;
    }

    /**
     * @param nb nombres d'obstacles à générer
     * 
     */
    private void generateObstacles(int nb) {
        nb = (int) (nb * difficulty);
        for (int i = 0; i < nb; i++) {
            int pfx = new Random().nextInt((int) this.width);
            int pfy = new Random().nextInt((int) this.height);
            Plateforme pf = new PlateformeBase(pfx, pfy, 100, 100, 0);
            plateformesListe.add(pf);
        }
    }

    /**
     * 
     * @param minY indique la coordonnee minimal y que peut avoir un objets, si en
     *             dessous on l'enleve
     */
    void removeObstacles(double minY) {
        int c = 0;// compte le nombres d'obstacles qu'on enleve
        for (GameObject gameObject : plateformesListe) {
            if (gameObject.getY() <= minY) {
                plateformesListe.remove(gameObject);
                ++c;
            }
        }
        // si on veut des nouveaux obstacles.
        // generateObstacles(c);
    }

    void endGame() {
    }

    public void update(double deltaT) {
        joueur.perso.move(deltaT);
        limite(joueur.perso);

        if (joueur.perso.getY() <= 0) {
            endGame();
        } else if (joueur.perso.getY() + joueur.perso.getHeight() > maxHeight * height) {
            y = height * advancement;
        }
        for (GameObject gameObject : plateformesListe) {
            if (gameObject.getClass().getName() == "MovingPlateforme") {
                // jsp s'il ya un meilleur moyen pour voir si l'objet est moveable
                MovingPlateforme a = (MovingPlateforme) gameObject;
                a.move(deltaT);
                limite(a);
            }
            joueur.perso.collides(gameObject);
        }
    }

    private void limite(GameObject object) {
        if (object.getX() + object.getWidth() / 2 <= 0) // bord a droite, on le mets a gauche
            object.setX(width - object.getWidth() / 2);
        else if (object.getX() + object.getWidth() / 2 >= width) // bord a gauche, on le mets a gauche
            object.setX(object.getWidth() / 2);
    }

    public ArrayList<Plateforme> getPlateformesListe() {
        return plateformesListe;
    }

    public void setPlateformesListe(ArrayList<Plateforme> plateformesListe) {
        this.plateformesListe = plateformesListe;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAdvancement() {
        return advancement;
    }

    public void setAdvancement(double advancement) {
        this.advancement = advancement;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

}