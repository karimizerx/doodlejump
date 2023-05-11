package gameobjects;

// Import de packages java
import java.util.*;

// Import d'autres dossiers
import gui.*;
import multiplayer.*;

// Classe principale qui fait le lien entre tous les éléments du jeu.
public class Terrain {

    private ArrayList<Plateforme> plateformesListe; // Liste des plateformes sur le terrain
    private ArrayList<Joueur> ListeJoueurs; // Liste des joueurs
    private ArrayList<Coins> coins; // Liste des coins sur le terrain
    private final double height, width; // Dimensions du terrain
    private double difficulty = 1.0; // facteur qui augmente au cours de la partie et qui permet d'avoir une
                                     // difficulté progressive.
    private double diff_plateformes; // Différence de y entre 2 plateformes.
    // La difficulté baisse plus le score monte. Affecte la densite des plateformes.
    // Affecte la proba qu'un item bonus ou malus apparaisse.
    private ArrayList<Monstre> monstres; // Liste des monstres présents.
    private boolean pause; // Si le jeu est en pause.
    public boolean multiplayer, isHost,multiDone=false;
    public Serveur host = null;
    public JoueurConnecte client = null;
    public final int playerID;// si c'est 0, il est host ou il est pas multijoueur.
    public static double intervalle_jetpack = 1;
    public static double intervalle_helico = 1;
    private double difficulty_level;// coefficient de variation de la difficulté, permet de choisir à quelle vitesse
                                    // la difficuté augmente.
    // Facile = 0.0001
    // Moyen = 0.0003
    // Difficile = 0.0006
    // Extreme 0.002

    public Terrain(ArrayList<Joueur> ljoueur, double height, double width, boolean host, boolean multiplayer,
            int id, double diff_l) {
        this.plateformesListe = new ArrayList<Plateforme>();
        this.ListeJoueurs = ljoueur;
        this.monstres = new ArrayList<Monstre>();
        this.coins = new ArrayList<Coins>();
        this.height = height;
        this.width = width;
        this.diff_plateformes = 41040 / this.height;
        this.multiplayer = multiplayer;
        this.isHost = host;
        this.playerID = id;
        this.difficulty_level = diff_l;
        if (!multiplayer)
            isHost = false;

        // Création des plateformes
        if ((isHost && multiplayer)||!multiplayer)
            generateObstacles();
    }

    // Génèrent une probabilité croissante selon la difficultée.
    private boolean willMove(double x) {
        int c = new Random().nextInt(31);
        if (c * x >= 30)
            return true;
        return false;
    }

    private boolean willMonstre(double x) {
        int c = new Random().nextInt(1300);
        if (c + x > 1298)
            return true;
        return false;
    }

    // Crée la liste des plateformes.
    private void generateObstacles() {
        // Taille des plateformes en fonction de la taille de la fenêtre.
        double w = this.width * 0.09375, h = 0.0194931774 * this.height;
        // Génère des plateformes à coord aléatoires pour la liste des plateformes.
        for (int i = (int) height; i > 0; i -= diff_plateformes) {
            // On définit la largeur/hauteur des plateformes de base.
            int x = new Random().nextInt((int) (this.width - w));
            int y = i;
            plateformesListe.add(new PlateformeBase(x, y, w, h, -(this.height * 0.0009746589)));
        }
    }

    // Renvoie la plateforme la plus haute sur le terrain.
    private Plateforme highestPlateforme() {
        Plateforme plateformeLaPlusHaute = plateformesListe.get(0);
        for (Plateforme p : plateformesListe) {
            if (p.getY() <= plateformeLaPlusHaute.getY())
                plateformeLaPlusHaute = p;
        }
        return plateformeLaPlusHaute;
    }

    // Gère, pour le perso, le débordement de l'écran.
    private void limite(Personnage p) {
        // 0.43 est la valeur exacte de la moitié du perso.
        // Si + de la moitié du perso est sortie du côté gauche de l'écran.
        // => on place la moitié du perso au côté droit de l'écran.
        if (p.getX() + p.getWidth() * 0.43 <= 0)
            p.setX(this.width - (p.getWidth() * 0.43));
        else if (p.getX() + p.getWidth() * 0.43 >= width) // Et inversement.
            p.setX(-(p.getWidth() * 0.43));
    }

    // Le jetpack n'apparaît que tous les 100000 de score afin de le rendre assez rare.
    private boolean jetpack(Joueur j, double intervalle) {
        return j.getScore() >= (100000 * intervalle) + (new Random().nextInt(10000));
    }

    private boolean helico(Joueur j, double intervalle) {
        return j.getScore() >= (40000 * intervalle) + (new Random().nextInt(10000));
    }

    // Mises à jour du jeu.
    public void update(double deltaTime) {
        if ((multiplayer))
        {    if(isHost)
                update(ListeJoueurs.get(0), deltaTime);
            else 
                update(ListeJoueurs.get(1), deltaTime);
        }else{
            for (Joueur j : ListeJoueurs)
                update(j, deltaTime);
        }
    }

    private void update(Joueur j, double deltaTime) {
        // On effectue une mise à jour pour tous les joueurs.
        Personnage p = j.getPerso();

        // Ralentissement progressif après un saut.
        double ralentissement = 0.0000194942 * this.height;
        p.setDy(p.getDy() + (ralentissement * deltaTime));
        p.setY(p.getY() + p.getDy());

        // Mise à jour de l'item du perso.
        if (p.getDy() >= 0) { // Si on redescend, on perd l'item.
            p.setItem(null);
        }
        // Si les pieds du perso touchent le bas de la fenêtre, on a perdu.
        if (p.getY() + 0.87 * p.getHeight() >= this.height) {
            Vue.isRunningGame = false;
        }

        // Positions des projectiles.
        for (int i = 0; i < p.getListProjectiles().size(); ++i) {
            Projectile pro = p.getListProjectiles().get(i);
            pro.setY(pro.getY() + pro.getDy());
            if (pro.limiteProjectile()) {
                p.getListProjectiles().remove(pro);
            }
        }

        // Si la tête du personnage dépasse la moitié de l'écran.
        if (p.getY() < this.height / 2 && (((isHost && multiplayer) || !multiplayer))) {
            // plus la difficulté augmente plus les plateformes sont écarté jusqu'à un
            // certain seuil qu'on a défini préalablement (la moitié de la taille).
            this.difficulty = (this.difficulty > 5) ? 5 : this.difficulty + this.difficulty_level;
            p.setY(this.height / 2);
            j.setScore(j.getScore() + Math.abs((int) (p.getDy() * deltaTime))); // On incrémente le score.

            // On descend toutes les plateformes.
            for (int i = 0; i < plateformesListe.size(); ++i) {
                Plateforme pf = plateformesListe.get(i);
                pf.setY(pf.getY() - (int) p.getDy());

                if (pf.getY() + pf.getHeight() >= this.height) { // Si la plateforme baissée déborde de l'écran.
                    pf.setY(highestPlateforme().getY() - (diff_plateformes * difficulty)
                            + (((new Random().nextInt(10) + 1) * (new Random().nextInt(3) - 1)) * difficulty / 2));
                    pf.setX(new Random().nextInt((int) (this.width - pf.getWidth())));

                    if (willMove(difficulty)) {
                        plateformesListe.remove(pf);
                        Plateforme pf_mobile = new MovingPlateforme(pf.getX(), pf.getY(), pf.getWidth(), pf.getHeight(),
                                -(this.height * 0.0013645224), (0.003125 * this.width));
                        plateformesListe.add(pf_mobile);
                        plateformesListe.get(plateformesListe.size() - 1)
                                .setDx((0.003125 * this.width) * difficulty / 3.5);

                        if (jetpack(j, intervalle_jetpack)) {
                            intervalle_jetpack+=intervalle_jetpack+1.1;
                            Items it = new Fusee(pf_mobile.getX(), pf_mobile.getY(), 50, 50,
                                    -0.5*(this.height * 0.013645224), 0.5);
                            pf_mobile.setItem(it);
                        }
                        else if (helico(j, intervalle_helico)) {
                            intervalle_helico+=intervalle_helico+3;
                            Items helicopter = new Helicoptere(pf_mobile.getX(), pf_mobile.getY(), 50, 50,
                                    -0.3*(this.height * 0.013645224), 0.5);
                            pf_mobile.setItem(helicopter);
                        }

                    } else {
                        plateformesListe.remove(pf);
                        Plateforme pf_statique = new PlateformeBase(pf.getX(), pf.getY(), pf.getWidth(), pf.getHeight(),
                                -(this.height * 0.0009746589));
                        plateformesListe.add(pf_statique);
                        plateformesListe.get(plateformesListe.size() - 1).setSaut(-(this.height * 0.0009746589));

                        if (jetpack(j, intervalle_jetpack)) {
                            intervalle_jetpack+=intervalle_jetpack+1.1;
                            Items it = new Fusee(pf_statique.getX(), pf_statique.getY(), 50, 50,
                                    -0.5*(this.height * 0.013645224), 0.5);
                            pf_statique.setItem(it);
                        }
                        else if (helico(j, intervalle_helico)) {
                            intervalle_helico+=intervalle_helico+3;
                            Items helicopter = new Helicoptere(pf_statique.getX(), pf_statique.getY(), 50, 50,
                                    -0.3*(this.height * 0.013645224), 0.5);
                            pf_statique.setItem(helicopter);
                        }

                    }

                    if (new Random().nextDouble() > 1 / difficulty) {
                        int x1 = new Random().nextInt((int) (this.width - 80));
                        coins.add(new Coins(x1, -80, 30, 30));
                    }
                }

                if (pf.getY() < -50) {
                    plateformesListe.remove(pf);
                }
            }

            if (willMonstre(difficulty)) {
                int x1 = new Random().nextInt((int) (this.width - 80));
                int id = new Random().nextInt(2) + 1;
                monstres.add(new Monstre(x1, -80, id == 1 ? 70 : 80, id == 1 ? 50 : 90,
                        -(this.height * 0.0013645224), -(0.003125 * this.width), id));
                // pour id = 1 : 70,50
                // pour id = 2 : 80,90
            }

            // On descend tous les coins.
            ArrayList<Coins> suppCoins = new ArrayList<Coins>();
            for (Coins c : coins) {
                c.setY(c.getY() - (int) p.getDy());
                if (c.getY() + c.getHeight() >= this.height) { // Si les coins baissés débordent de l'écran.
                    suppCoins.add(c);
                }
            }
            coins.removeAll(suppCoins);

            // On descend tous les monstres.
            ArrayList<Monstre> suppMonstres = new ArrayList<Monstre>(); // La liste des monstres à supprimer.
            for (Monstre m : monstres) {
                m.setY(m.getY() - (int) p.getDy());
                if (m.getY() + m.getHeight() >= this.height) // Si les monstres baissés débordent de l'écran.
                    suppMonstres.add(m);
            }
            monstres.removeAll(suppMonstres);
        }

        /// On gère les collisions.

        // Collisions de monstres.
        ArrayList<Monstre> suppMonstres = new ArrayList<Monstre>(); // La liste des monstres à supprimer.
        for (Monstre m : monstres) {
            m.move(this);
            if (p.collides_projectile(m)) {
                if (m.shot())
                    suppMonstres.add(m);
            } else if (p.collides_monstre(m, deltaTime)) { // Si on a tué le monstre par rebond
                suppMonstres.add(m);
            }
        }
        monstres.removeAll(suppMonstres);

        // Collisions de coins
        ArrayList<Coins> suppCoins = new ArrayList<Coins>();
        for (Coins c : coins) {
            if (p.collides_coin(c, deltaTime)) {
                j.addCoin();
                suppCoins.add(c);
            }
        }
        coins.removeAll(suppCoins);

        // Collisions du personnage / items.
        for (Plateforme pf : plateformesListe) {
            p.collides_plateforme(pf, deltaTime);
            pf.move(this);

            // On met à jour la position de l'item (s'il existe).
            if (pf.getItem() != null && !multiplayer) {
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
        if (p.getItem() != null && !multiplayer) {
            Items it = p.getItem();
            it.setY(p.getY());
            it.setX(p.getX());
            p.setItem(it);
        }

        // On gère les collisions & les débordements du personnage.
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

    public ArrayList<Monstre> getMontresArrayList() {
        return monstres;
    }

    public void setMonstres(ArrayList<Monstre> m) {
        monstres = m;
    }

    public ArrayList<Coins> getCoins() {
        return this.coins;
    }

    public void setCoins(ArrayList<Coins> readObject) {
        coins=readObject;
    }

    public boolean isMultiDone() {
        return multiDone;
    }

    public void setMultiDone(boolean multiDone) {
        this.multiDone = multiDone;
    }

}