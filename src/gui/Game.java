package gui;

// Import d'autres dossiers
import gameobjects.*;
import leaderboard.*;
import multiplayer.*;

// Import de packages java
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

// Représente l'état ou l'application est au niveau du "MENU DEMARRER".
public class Game extends Etat { // C'est donc un Etat.

    private double deltaTime; // Le temps nécessaire pour update le jeu
    private boolean isEndGame;
    private BufferedImage terrainView, platformeBaseView, platformeMobileView, scoreBackgroundView, projectileView;
    private ArrayList<ArrayList<BufferedImage>> joueurDataList;

    public Game(Vue vue) {
        super(vue);
        this.deltaTime = 10;
    }

    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
        // Stock des listes qui elles-mêmes stockes les données d'image de chaque joueur
        // et qui ne changent jamais, i.e le perso et le nom, contrairement au score
        joueurDataList = new ArrayList<ArrayList<BufferedImage>>();

        try {
            try {
                terrainView = ImageIO.read(new File(chemin + "/background/terrainBackground.png"));
                platformeBaseView = ImageIO.read(new File(chemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(chemin + "/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/background/scoreBackground1.png"));
                projectileView = ImageIO.read(new File(chemin + "/projectile.png"));
            } catch (Exception e) {
                terrainView = ImageIO.read(new File(winchemin + "/background/terrainBackground.png"));
                platformeBaseView = ImageIO.read(new File(winchemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(winchemin + "/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File(winchemin + "/background/scoreBackground1.png"));
                projectileView = ImageIO.read(new File(winchemin + "/projectile.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {

        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {

                // On remplit les données d'image de tous les joueurs
                for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
                    Joueur joueur = terrain.getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On a une liste qui ne contient que l'image du perso
                    ArrayList<BufferedImage> persoData = new ArrayList<BufferedImage>();
                    persoData.add(ImageIO.read(new File(chemin + "/personnages/persoBase.png")));
                    // Suivie d'une liste qui contient le nom du joueur (i.e toutes les lettres)
                    ArrayList<BufferedImage> nomData = createImageOfMot(nom);
                    joueurDataList.add(persoData);
                    joueurDataList.add(nomData);
                }
            } catch (Exception e) {
                // On remplit les données d'image de tous les joueurs
                for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
                    Joueur joueur = terrain.getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On a une liste qui ne contient que l'image du perso
                    ArrayList<BufferedImage> persoData = new ArrayList<BufferedImage>();
                    persoData.add(ImageIO.read(new File(winchemin + "/personnages/persoBase.png")));
                    // Suivie d'une liste qui contient le nom du joueur (i.e toutes les lettres)
                    ArrayList<BufferedImage> nomData = createImageOfMot(nom);
                    joueurDataList.add(persoData);
                    joueurDataList.add(nomData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update les images & autres variables.
    public void update() {
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            Personnage p = j.getPerso();
            // Gère les boutons flèches, avec inertie
            // Dans qu'on appuie, on set la vitesse à ± 4, et on avance de cette distance
            double vitesse = 0.0078125 * terrain.getWidth();
            double ralentissement = 0.000375 * terrain.getWidth();
            if (p.isRight()) {
                p.setDx(+vitesse);
                p.setX(p.getX() + p.getDx());
            } else if (p.isLeft()) {
                p.setDx(-vitesse);
                p.setX(p.getX() + p.getDx());
            } else if (p.isInertRight() && p.getDx() > 0) { // Si on arrête d'appuyer,
                p.setDx(p.getDx() - ralentissement); // la vitesse ralentie petit à petit jusqu'à devenir nulle
                p.setX(p.getX() + p.getDx());
            } else if (p.isInertLeft() && p.getDx() < 0) {
                p.setDx(p.getDx() + ralentissement);
                p.setX(p.getX() + p.getDx());
            } else {
                p.setInertRight(false);
                p.setInertLeft(false);
                p.setDx(0);
            }
            if (p.isShoot() && p.iscanShoot()) { // Si on tire, on ne tire plus
                p.tirer(0.046875 * terrain.getWidth(), 0.02923397661 * terrain.getHeight(), 0, -deltaTime);
                p.setShoot(false);
            }
        }
        terrain.update(this.deltaTime);
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        int tw = (int) terrain.getWidth(), th = (int) terrain.getHeight();

        // Affichage terrain
        g2.drawImage(terrainView, 0, 0, tw, th, null);

        // Affichage des plateformes
        for (Plateforme pf : terrain.getPlateformesListe()) {
            BufferedImage pfV = (pf instanceof PlateformeBase) ? platformeBaseView : platformeMobileView;
            g2.drawImage(pfV, (int) pf.getX(), (int) pf.getY(), (int) pf.getWidth(), (int) pf.getHeight(), null);
        }

        // Affichage du Score : seulement s'il n'y a qu'un joueur
        if (terrain.getListeJoueurs().size() == 1) {
            String score = String.valueOf(terrain.getListeJoueurs().get(0).getScore());
            int sw = (int) (tw * 0.09375), sh = (int) (th * 0.0536062378);
            g2.drawImage(scoreBackgroundView, 2, 2, sw + (sw / 2 * (score.length() - 1)), sh, null);
            ArrayList<BufferedImage> scoreView = createImageOfMot(score);
            int x = (int) (tw * 0.0078125); // Variable pour adapter en fonction de la résolution
            afficheMot(g2, scoreView, x, 5, x * 10, x * 10, 5 * x, 0);
        }

        // Affichage des personnages + Nom
        for (int i = 0; i < terrain.getListeJoueurs().size() * 2; i = i + 2) {
            // On récupère les données liées au joueur
            BufferedImage jPersoData = joueurDataList.get(i).get(0);
            ArrayList<BufferedImage> jNomData = joueurDataList.get(i + 1);
            Personnage p = terrain.getListeJoueurs().get(i / 2).getPerso();

            // Affichage du personnage :
            g2.drawImage(jPersoData, (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);
            int c = (int) ((20 * (jNomData.size() - 1)) - p.getWidth()) / 2; // Pour placer le nom au centre du perso
            afficheMot(g2, jNomData, (int) (p.getX() - c), (int) p.getY() - 15, 20, 20, 15, 10);
        }

        // Affichage des projectiles
        for (Joueur j : terrain.getListeJoueurs()) {
            Personnage pers = j.getPerso();
            for (Projectile pro : pers.getListProjectiles()) {
                g2.drawImage(projectileView, (int) pro.getX(), (int) pro.getY(), (int) pro.getWidth(),
                        (int) pro.getHeight(), null);
            }
        }

        /// Affichage final
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner cet état en boucle.
    public void running(Graphics g) {
        // Gestion de l'ups constant
        double cnt = 0.0; // Compteur du nombre d'update
        double acc = 0.0; // Accumulateur qui va gérer les pertes de temps
        long t0 = System.currentTimeMillis(); // Temps actuel
        while (Vue.isRunningGame) { // Tant que le jeu tourne
            if (!terrain.isPause()) { // Tant qu'on appuie pas sur pause
                long t1 = System.currentTimeMillis();
                long t = t1 - t0;
                t0 = System.currentTimeMillis();
                acc += t;
                while (acc > deltaTime) { // Si on peut update
                    update(); // On met à jour les variables
                    // On retire 1 Δ à chaque update. Si le reste > 0 & < Δ, ça veut dire qu'on a
                    // un retard, qu'on stock pour l'ajouter à l'étape suivante.
                    // Si on a reste > Δ, on relance cette boucle
                    acc -= deltaTime;
                    cnt += deltaTime; // On accumule le nombre d'update
                }
            }
            affiche(g); // On affiche les images une fois les données update
        }
        this.isEndGame = endGame();
    }

    // Gère les boutons.
    @Override
    public void keyControlPressed(KeyEvent e) {
        System.out.println("Menu FIN - Key Pressed");
        Personnage p1, p2;
        if (!terrain.multiplayer) // Si on ne joue pas en multijoueur :
            p1 = terrain.getListeJoueurs().get(0).getPerso(); // On récupère le personnage du premier joueur.
        else
            p1 = terrain.getListeJoueurs().get(terrain.playerID).getPerso();

        /// Gestion des déplacements horizontales des personnages :
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Si on clique sur la flèche droite :
            p1.setRight(true); // On indique qu'on avance vers la droite.
            p1.setInertRight(false); // On stop l'inertie (le ralentissement).
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Si on clique sur la flèche gauche :
            p1.setLeft(true); // On indique qu'on avance vers la gauche.
            p1.setInertLeft(false); // On stop l'inertie (le ralentissement).
        }

        if (terrain.getListeJoueurs().size() == 2) { // On fait la même chose avec "Q" & "D" s'il y a 2 joueurs.
            p2 = terrain.getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_D) {
                p2.setRight(true);
                p2.setInertRight(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
                p2.setLeft(true);
                p2.setInertLeft(false);
            }
        }

        /// Gestion des tires de projectiles :
        // Si on a le droit de tirer et qu'on tire :
        if (e.getKeyCode() == KeyEvent.VK_UP && p1.iscanShoot()) {
            p1.setShoot(true); // On indique qu'on vient de tirer.
            p1.setcanShoot(false); // On a pas le droit de re-tirer.
        }

        if (terrain.getListeJoueurs().size() == 2) { // On fait la même chose avec "Z" s'il y a 2 joueurs.
            p2 = terrain.getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_Z && p2.iscanShoot()) {
                p2.setShoot(true); // On indique qu'on vient de tirer.
                p2.setcanShoot(false); // On a pas le droit de re-tirer.
            }
        }

        /// Gestion de la pause :
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // pause();
        }
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
        // Si on est en pleine partie :
        Personnage p1, p2;
        if (!terrain.multiplayer) // Si on ne joue pas en multijoueur :
            p1 = terrain.getListeJoueurs().get(0).getPerso(); // On récupère le personnage du premier joueur.
        else
            p1 = terrain.getListeJoueurs().get(terrain.playerID).getPerso();

        /// Gestion des déplacements horizontales des personnages :
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Si on relâche pendant que l'on se déplace vers la droite :
            p1.setRight(false); // On arrête de se déplacer.
            p1.setInertRight(true); // On lance le ralentissement (inertie).
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Si on relâche pendant que l'on se déplace vers la gauche :
            p1.setLeft(false); // On arrête de se déplacer.
            p1.setInertLeft(true); // On lance le ralentissement (inertie).
        }

        if (terrain.getListeJoueurs().size() == 2) { // On fait la même chose avec "Q" & "D" s'il y a 2 joueurs.
            p2 = terrain.getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_D) {
                p2.setRight(false);
                p2.setInertRight(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
                p2.setLeft(false);
                p2.setInertLeft(true);
            }
        }

        /// Gestion des tires de projectiles :
        if (e.getKeyCode() == KeyEvent.VK_UP) // On oblige le joueur à lâcher pour tirer (pour éviter le spam).
            p1.setcanShoot(true); // Dès qu'on lâche, on a de nouveau le droit de tirer.

        if (terrain.getListeJoueurs().size() == 2) { // On fait la même chose avec "Z" s'il y a 2 joueurs.
            p2 = terrain.getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_Z) // On oblige le joueur à lâcher pour tirer (pour éviter le spam).
                p2.setcanShoot(true); // Dès qu'on lâche, on a de nouveau le droit de tirer.
        }
    }

    /// Méthodes générales utiles :
    // Crée une partie (en initialisant toutes les variables, i.e le terrain, ...)
    private void createPartie() {
        /// Initialisation des éléments :
        ArrayList<Joueur> ljou = new ArrayList<Joueur>(); // Liste des joueurs.

        /// Ajout des joueurs :
        // Variable (z) pour adapter les dimensions à la résolution d'écran.
        // L'image du perso doit être un carré. On prend la valeure la plus petite.
        double z = ((height * 0.09746) > (width * 0.15625)) ? (width * 0.15625) : (height * 0.09746);
        Personnage p = new Personnage(width / 2, height - z, z, z, -(height * 0.0097465887));
        ljou.add(new Joueur(p, nom1));
        if (nbJoueur == 2) { // S'il y a 2 joueurs :
            Personnage p2 = new Personnage(width / 2, height - z, z, z, -(height * 0.0097465887));
            ljou.add(new Joueur(p2, nom2));
        }

        int i = multijoueur ? host ? 0 : 1 : 0;
        this.terrain = new Terrain(ljou, height, width, host, multijoueur, i); // On crée le terrain.
        this.terrain.setClient(jconnect);
        this.terrain.setHost(serveur);
    }

    // Gère le cas de fin de la GAME
    private boolean endGame() {
        boolean isFin = false;
        // Si un joueur à perdu, c'est fini
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            isFin = (j.getPerso().getY() + j.getPerso().getHeight() > this.getHeight()) ? true : false;
        }
        return isFin;
    }

    public boolean isEndGame() {
        return isEndGame;
    }
}