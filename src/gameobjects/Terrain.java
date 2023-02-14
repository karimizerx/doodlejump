package gameobjects;

// Import de packages java
import java.util.ArrayList;
import java.util.Random;

// Import d'autres dossiers
import gui.Vue;

// Gère tous les éléments du terrain
public class Terrain {

    private ArrayList<Plateforme> plateformesListe; // Liste des plateformes sur le terrain
    private ArrayList<Joueur> ListeJoueurs; // Liste des joueurs
    private final double height, width; // Dimensions du terrain
    private double difficulty = 1.0;
    private double diff_plateformes; // Différence de y entre 2 plateformes
    // La difficulté baisse plus le score monte. Affecte la densite des plateformes.
    // Affecte la proba qu'un item bonus ou malus (sûrement 1/diff) apparaisse.

    public Terrain(ArrayList<Joueur> ljoueur, double height, double width) {
        // Initialisation des champs
        this.plateformesListe = new ArrayList<Plateforme>();
        this.ListeJoueurs = ljoueur;
        this.height = height;
        this.width = width;
        this.diff_plateformes = this.height * 0.039;

        // Création des plateformes
        generateObstacles();
    }

    /// Méthodes de la classe

    // Crée la liste des plateformes (avec un nbPlateformes en entrée)
    private void generateObstacles() {
        // Taille des plateformes en fonction de la taille de la fenêtre
        double w = this.width * 0.09375, h = this.height * 0.0195;
        // Génère des plateformes à coord aléatoires pour la liste des plateformes
        for (int i = (int) height; i > 0; i -= diff_plateformes) {
            // On définit la largeur/hauteur des plateformes de base
            int x = new Random().nextInt((int) (this.width - w));
            int y = i;
            double c = new Random().nextDouble();
            if (c < 0.1) {
                plateformesListe.add(new MovingPlateforme(x, y, w, h, -(this.height / 1026), (this.width * 0.003125)));
            } else
                plateformesListe.add(new PlateformeBase(x, y, w, h, -(this.height / 1026)));

        }
        // On s'assure d'aboird toujours une solution au début
    }

    // Renvoie la plateforme la plus haute sur le terrain
    private Plateforme highestPlateforme() {
        Plateforme plateformeLaPlusHaute = plateformesListe.get(0);
        for (Plateforme p : plateformesListe) {
            if (p.getY() <= plateformeLaPlusHaute.getY()) {
                plateformeLaPlusHaute = p;
            }
        }
        return plateformeLaPlusHaute;
    }

    // Gère, pour le perso, le débordement de l'écran
    private void limite(Personnage p) {
        // 0.43 est la valeur exacte de la moitié du perso
        // Si + de la moitié du perso est sortie du côté gauche de l'écran
        // => on place la moitié du perso au côté droit de l'écran
        if (p.getX() + p.getWidth() * 0.43 <= 0)
            p.setX(this.width - (p.getWidth() * 0.43));
        else if (p.getX() + p.getWidth() * 0.43 >= width) // Et inversement
            p.setX(-(p.getWidth() * 0.43));
    }

    // Mise à jour du jeu.
    public void update(double deltaTime) {
        // On effectue une mise à jour pour tous les joueurs
        for (int i = 0; i < ListeJoueurs.size(); ++i) {
            Joueur j = ListeJoueurs.get(i);
            Personnage p = j.getPerso();

            // Ralentissement progressif après un saut
            System.out.println("wwwwwwwwwwwwwwwwwwwwwww,hhhhhhhhhhh : " + this.width + " , " + this.height + " , "
                    + (this.height * 0.5));
            double ralentissement = this.height * 0.0000195;
            p.setDy(p.getDy() + (ralentissement * deltaTime));
            p.setY(p.getY() + p.getDy());

            // Si les pieds du perso touchent le bas de la fenêtre, on a perdu
            if (p.getY() + 0.87 * p.getHeight() >= this.height) {
                Vue.isRunning = false;
            }

            // Si la tête du personnage dépasse la moitié de l'écran
            if (p.getY() < this.height / 2) {
                // plus la difficulté augmente plus les plateformes sont écarté jusqu'a a
                // certain seuil qu'on a défini préalablement
                difficulty = (difficulty > 5) ? 5 : difficulty + 0.0006;
                p.setY(this.height / 2);
                j.setScore(j.getScore() + 1); // On incrémente le score de 1
                // On descend toutes les plateforme
                for (Plateforme pf : plateformesListe) {
                    pf.setY(pf.getY() - (int) p.getDy());
                    if (pf.getY() - pf.getHeight() >= this.height) { // Si la plateformes baissées déborde de l'écran
                        pf.setY(highestPlateforme().getY() - (diff_plateformes * difficulty)
                                + (((new Random().nextInt(10) + 1) * (new Random().nextInt(3) - 1)) * difficulty / 2));
                    }
                }
            }
            // On gère les collisions & les débordements du personnage
            for (Plateforme pf : plateformesListe) {
                p.collides_plateforme(pf, deltaTime);
                if (pf instanceof MovingPlateforme)
                    ((MovingPlateforme) pf).move(this);
            }
            limite(p);
        }
    }

    // Getter & Setter

    public ArrayList<Plateforme> getPlateformesListe() {
        return plateformesListe;
    }

    public void setPlateformesListe(ArrayList<Plateforme> plateformesListe) {
        this.plateformesListe = plateformesListe;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<Joueur> getListeJoueurs() {
        return ListeJoueurs;
    }

    public void setListeJoueurs(ArrayList<Joueur> listeJoueurs) {
        ListeJoueurs = listeJoueurs;
    }

    public double getDiff_plateformes() {
        return diff_plateformes;
    }

    public void setDiff_plateformes(double diff_plateformes) {
        this.diff_plateformes = diff_plateformes;
    }
}