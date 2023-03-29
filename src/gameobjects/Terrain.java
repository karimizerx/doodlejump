package gameobjects;

// Import de packages java
import java.util.*;

// Import d'autres dossiers
import gui.*;
import multiplayer.*;

public class Terrain {

    private ArrayList<Plateforme> plateformesListe; // Liste des plateformes sur le terrain
    private ArrayList<Items> itemsListe; // La liste des items sur le terrain
    private ArrayList<Joueur> ListeJoueurs; // Liste des joueurs
    private final double height, width; // Dimensions du terrain
    private double difficulty = 1.0;
    private double diff_plateformes; // Différence de y entre 2 plateformes
    // La difficulté baisse plus le score monte. Affecte la densite des plateformes.
    // Affecte la proba qu'un item bonus ou malus (sûrement 1/diff) apparaisse.

    private boolean pause;
    public boolean multiplayer, isHost;
    public Serveur host = null;
    public JoueurConnecte client = null;
    public final int playerID;// si c'est 0, il est host ou il est pas multijoueur.

    public Terrain(ArrayList<Joueur> ljoueur, double height, double width, boolean host, boolean multiplayer,
            int id) {
        // Initialisation des champs
        this.plateformesListe = new ArrayList<Plateforme>();
        this.ListeJoueurs = ljoueur;
        this.height = height;
        this.width = width;
        this.diff_plateformes = 41040 / this.height;
        this.multiplayer = multiplayer;
        this.isHost = host;
        this.playerID = id;
        if (!multiplayer)
            isHost = false;

        // Création des plateformes
        generateObstacles();

        // Ajout d'un item
        Plateforme pf = plateformesListe.get(plateformesListe.size() - 1);
        Items i = new Fusee(pf.getX() + 0.5 * pf.getWidth(), pf.getY() - 50, 30, 50, -5, 0);
        pf.setItem(i);
    }

    // Génère une probabilité croissante selon la difficultée
    private boolean willMove(double x) {
        int c = new Random().nextInt(31);
        if (c * x >= 30) {
            return true;
        }
        return false;
    }

    // Crée la liste des plateforme
    private void generateObstacles() {
        // Taille des plateformes en fonction de la taille de la fenêtre
        double w = this.width * 0.09375, h = 0.0194931774 * this.height;
        // Génère des plateformes à coord aléatoires pour la liste des plateformes
        for (int i = (int) height; i > 0; i -= diff_plateformes) {
            // On définit la largeur/hauteur des plateformes de base
            int x = new Random().nextInt((int) (this.width - w));
            int y = i;
            double c = new Random().nextDouble();
            plateformesListe.add(new PlateformeBase(x, y, w, h, -(this.height * 0.0009746589)));
        }
        // On s'assure d'abord toujours une solution au début
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

    // Mises à jour du jeu.
    public void update(double deltaTime) {
        if ((isHost && multiplayer))
            update(ListeJoueurs.get(0), deltaTime);
        else if ((!isHost && multiplayer) || !multiplayer) {
            for (Joueur j : ListeJoueurs)
                update(j, deltaTime);
        }
    }

    private void update(Joueur j, double deltaTime) {
        // On effectue une mise à jour pour tous les joueurs
        Personnage p = j.getPerso();

        // Ralentissement progressif après un saut
        double ralentissement = 0.0000194942 * this.height;
        p.setDy(p.getDy() + (ralentissement * deltaTime));
        p.setY(p.getY() + p.getDy());

        // Mise à jour de l'item du perso
        if (p.getDy() >= 0) { // Si on redescend, on perd l'item
            p.setItem(null);
        }
        // Si les pieds du perso touchent le bas de la fenêtre, on a perdu
        if (p.getY() + 0.87 * p.getHeight() >= this.height) {
            Vue.isRunningGame = false;
        }

        // Affichage des projectiles :
        for (int i = 0; i < p.getListProjectiles().size(); ++i) {
            Projectile pro = p.getListProjectiles().get(i);
            pro.setY(pro.getY() + pro.getDy());
            if (pro.limiteProjectile()) {
                p.getListProjectiles().remove(pro);
            }
        }

        // Si la tête du personnage dépasse la moitié de l'écran
        if (p.getY() < this.height / 2 && (((isHost && multiplayer) || !multiplayer))) {
            // plus la difficulté augmente plus les plateformes sont écarté jusqu'à un
            // certain seuil qu'on a défini préalablement (la moitié de la taille)
            difficulty = (difficulty > 5) ? 5 : difficulty + 0.0001;
            p.setY(this.height / 2);
            j.setScore(j.getScore() + 1); // On incrémente le score

            // On descend toutes les plateforme
            for (int i = 0; i < plateformesListe.size(); ++i) {
                Plateforme pf = plateformesListe.get(i);
                pf.setY(pf.getY() - (int) p.getDy());

                if (pf.getY() + pf.getHeight() >= this.height) { // Si la plateformes baissées déborde de l'écran
                    pf.setY(highestPlateforme().getY() - (diff_plateformes * difficulty)
                            + (((new Random().nextInt(10) + 1) * (new Random().nextInt(3) - 1)) * difficulty / 2));
                    pf.setX(new Random().nextInt((int) (this.width - pf.getWidth())));
                    if (willMove(difficulty)) {
                        plateformesListe.remove(pf);
                        Plateforme pf2 = new MovingPlateforme(pf.getX(), pf.getY(), pf.getWidth(), pf.getHeight(),
                                -(this.height * 0.0013645224), (0.003125 * this.width));
                        plateformesListe.add(pf2);
                        plateformesListe.get(plateformesListe.size() - 1)
                                .setDx((0.003125 * this.width) * difficulty / 3.5);
                        Items it = new Fusee(pf2.getX(), pf2.getY() - 50, 30, 50, -(this.height * 0.013645224), 0.5);
                        pf2.setItem(it);
                    } else {
                        plateformesListe.remove(pf);
                        plateformesListe.add(new PlateformeBase(pf.getX(), pf.getY(), pf.getWidth(), pf.getHeight(),
                                -(this.height * 0.0009746589)));
                        plateformesListe.get(plateformesListe.size() - 1).setSaut(-(this.height * 0.0009746589));
                    }
                }
                if (pf.getY() < -50) {
                    plateformesListe.remove(pf);
                }
            }
        }
        // On gère les collisions
        for (Plateforme pf : plateformesListe) {
            p.collides_plateforme(pf, deltaTime);
            pf.move(this);

            // On met à jour la position de l'item (s'il existe)
            if (pf.getItem() != null) {
                Items it = pf.getItem();
                it.setY(pf.getY() - it.getHeight());
                it.setX(pf.getX() + it.getPlacement() * pf.getWidth());
                pf.setItem(it);
                if (p.getItem() == null && p.collides_item(it, deltaTime)) {
                    p.setItem(pf.getItem());
                    pf.setItem(null);
                }
            }
        }
        // On met à jour la position de l'item (s'il existe)
        if (p.getItem() != null) {
            Items it = p.getItem();
            it.setY(p.getY());
            it.setX(p.getX());
            p.setItem(it);
        }

        // On gère les collisions & les débordements du personnage
        limite(p);
    }

    // Getter & Setter

    public ArrayList<Plateforme> getPlateformesListe() {
        return plateformesListe;
    }

    public void setPlateformesListe(ArrayList<Plateforme> plateformesListe) {
        this.plateformesListe = plateformesListe;
    }

    public ArrayList<Joueur> getListeJoueurs() {
        return ListeJoueurs;
    }

    public void setListeJoueurs(ArrayList<Joueur> listeJoueurs) {
        ListeJoueurs = listeJoueurs;
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

    public double getDiff_plateformes() {
        return diff_plateformes;
    }

    public void setDiff_plateformes(double diff_plateformes) {
        this.diff_plateformes = diff_plateformes;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public Serveur getHost() {
        return host;
    }

    public void setHost(Serveur host) {
        this.host = host;
    }

    public JoueurConnecte getClient() {
        return client;
    }

    public void setClient(JoueurConnecte client) {
        this.client = client;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Joueur getMyPlayer() {
        return this.ListeJoueurs.get(playerID);
    }

    public void setJoueurConnecte(JoueurConnecte j) {
        client = j;
    }

    public void setJoueur(ArrayList<Joueur> l) {
        ListeJoueurs = l;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

}