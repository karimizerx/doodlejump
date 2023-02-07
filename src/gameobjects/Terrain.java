package gameobjects;

// Import de packages java
import java.util.ArrayList;
import java.util.Random;

// Import d'autres dossiers
import gui.Vue;

public class Terrain {

    private ArrayList<Plateforme> plateformesListe;
    private Joueur joueur;
    private final double height, width;// dimensions du terrain
    private double y = 0;// hauteur du jeu. On l'utilisera aussi pour le score

    // Entre 0 et 1, indique de combien on fais monter le jeu
    private double advancement = 0.3;

    /**
     * Hauteur maximal que peut atteindre le personnage, maxHeight*height,
     * avant de faire monter le jeu.
     */
    private double H = 0.3;

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
        generateObstacles(20);
    }

    /**
     * Crée la liste des plateformes
     * 
     * @param nb nombres d'obstacles à générer
     * 
     */
    private void generateObstacles(int nb) {
        nb = (nb > 50) ? 50 : (nb < 7) ? 8 : nb;
        plateformesListe = new ArrayList<Plateforme>();

        for (int i = 0; i < (nb * difficulty); i++) {
            Plateforme p = new PlateformeBase(new Random().nextInt((int) this.width),
                    new Random().nextInt((int) this.height - 20), 60, 20, -10);
            plateformesListe.add(p);
        }
        nb = (nb > 10) ? 10 : nb;
        for (int i = 1; i < nb; i++) {
            plateformesListe.get(i).setY(height - i * 90);
        }
    }

    public Plateforme highestPlateforme() {
        Plateforme temp = plateformesListe.get(0);
        for (Plateforme p : plateformesListe) {
            if (p.getY() <= temp.getY()) {
                temp = p;
            }
        }
        return temp;
    }

    /**
     * 
     * @param maxY indique la coordonnee maximale que peut avoir un objet en y,
     *             si supérieur, on le retire
     */
    private void removeObstacles(double maxY) {
        int c = 0;// compte le nombres d'obstacles qu'on enleve
        for (GameObject gameObject : plateformesListe) {
            if (gameObject.getY() >= maxY) {
                plateformesListe.remove(gameObject);
                ++c;
            }
        }
        // si on veut des nouveaux obstacles.
        generateObstacles(c);
    }

    private void limite(GameObject object) {
        if (object.getX() + object.getWidth() / 2 <= 0) // bord a droite, on le mets a gauche
            object.setX(width - (object.getWidth() / 2));
        else if (object.getX() + object.getWidth() / 2 >= width) // bord a gauche, on le mets a gauche
            object.setX((object.getWidth() / 2) - 60);
    }

    public static boolean first = true;

    public void update() {
        Joueur j = this.joueur;
        Personnage p = j.getPerso();
        double next = (highestPlateforme().getY() - 85);
        p.setDy(p.getDy() + 0.2);
        p.setY(p.getY() + p.getDy());

        // Si on est tout en bas de la fenêtre, endGame()
        if (p.getY() + 0.7 * p.getHeight() >= this.height) {
            Vue.isRunning = false;
        }

        if (p.getY() < this.height / 2) {
            p.setY(this.height / 2);
            j.setScore(j.getScore() + 1); // On incrémente le score de 1
            for (Plateforme pf : plateformesListe) {
                pf.setY(pf.getY() - (int) p.getDy());
                if (pf.getY() - pf.getHeight() >= this.height * 0.95) {
                    if (next < 300) {
                        pf.setY(0);
                    } else {
                        pf.setY(next);
                    }
                    int r = new Random().nextInt(530);
                    pf.setX(r);

                }
            }
        }
        for (Plateforme pf : plateformesListe) {
            p.collides_plateforme(pf);
        }

        limite(p);
    }

    // Getter & Setter

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

    public double getH() {
        return H;
    }

    public void setH(double H) {
        this.H = H;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

}