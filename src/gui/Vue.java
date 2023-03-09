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

// S'occupe de tout l'affichage du jeu, des menus jusqu'à une partie.
public class Vue extends JPanel implements Runnable, KeyListener {

    // Ces variables static boolean indique le statut actuel du panel
    public static boolean isQuitte, isRunningGame, isMenuDemarrer, isMenu2, isMenuFin, isClassement, isMenuPause;
    private final int width, height; // Dimensions du panel
    // La fleche est un curseur qui indique sur quel boutton on agit actuellement
    private int fleche, xfleche, yfleche, wfleche, hfleche, sautLigne, nbJoueur;
    private JFrame menuPause; // Le menu pause
    private String chemin, winchemin, nom1, nom2; // Le chemin vers le package d'images & les noms des joueurs.
    private BufferedImage view, backgroundView, backgroundClView, backgroundClView1, backgroundClView2, flecheView,
            terrainView, platformeBaseView, platformeMobileView, scoreBackgroundView, projectileView;
    private ArrayList<BufferedImage> buttonJouer, buttonJouerSolo, button2joueur, buttonMultiJoueur, buttonLb,
            buttonQuitter, buttonRetourMenu, titreStatut, messageNom, nomJ1, nomJ2;
    private ArrayList<ArrayList<BufferedImage>> joueurDataList, lbView, scoreFinalView, hightScoreView;
    private Terrain terrain; // Le terrain sur lequel on joue
    private double deltaTime; // Le temps nécessaire pour update le jeu
    private ThreadMouvement threadMvt; // thread qui gere l'envoi et la reception des données pour le multijoueur
    private Thread thread; // La thread reliée à ce pannel, qui lance l'exécution
    private boolean multijoueur = false, host = false;
    private Serveur serveur;
    private JoueurConnecte jconnect;
    private String skin;

    public Vue(App frame, String skin) {
        // Taille du panel
        this.width = frame.getWidth();
        this.height = frame.getHeight();
        this.setPreferredSize(new Dimension(this.width / 3, (int) (this.height * 0.95)));

        this.threadMvt = null;
        // Initialisation des chemins
        this.skin = skin;
        this.chemin = (new File("gui/images/" + skin + "/")).getAbsolutePath();
        this.winchemin = "src/gui/images/" + skin + "/";

        // Gestion d'évènements boutons
        this.addKeyListener(this);
    }

    // Méthodes de la classe

    // PARTIE GENERALE
    // Crée une liste d'images représentant un mot
    private ArrayList<BufferedImage> createImageOfMot(String mot) {
        // On crée une liste d'image qui va contenir toutes les lettres du mot
        ArrayList<BufferedImage> motView = new ArrayList<BufferedImage>();
        if (mot == null) {// S'il n'y a rien on retourne le mot "espace ".
            motView.add(null);
            return motView;
        }
        mot = mot.toLowerCase(); // On met toutes les lettres en minuscules pour ne pas avoir d'erreur.

        for (int i = 0; i < mot.length(); ++i) { // Pour chaque lettre du mot :
            char c = mot.charAt(i);
            try { // Double try_catch pour gérer la portabilité sur Windows.
                try {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                } catch (Exception e) {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(winchemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return motView; // Cette méthode ne gère pas les caractères spéciaux (excepté " ").
    }

    // Permet d'afficher un mot
    private int afficheMot(Graphics2D g2, ArrayList<BufferedImage> mot, int x, int y, int w, int h, int ecart,
            int espacement) {
        // L'écart = l'espace entre 2 lettre, l'espacement = le caractère espace
        for (int i = 0; i < mot.size(); ++i) {
            BufferedImage lettreView = mot.get(i);
            if (lettreView != null) { // Si c'est null, c'est égal à espace
                g2.drawImage(lettreView, x, y, w, h, null);
                x += ecart;
            } else
                x += espacement;
        }
        return x;
    }

    // Permet d'afficher un point/double point
    private int affichePoint(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(Color.BLACK);
        g2.fillOval(x, y - h, w, h);
        return x + w;
    }

    private int afficheDoublepoint(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(Color.BLACK);
        g2.fillOval(x, y + h / 2, w, h);
        g2.fillOval(x, y + h / 2 + 2 * h, w, h);
        return x + w;
    }

    // On initialise toutes les images qui seront utilisées à plusieurs endroits
    private void initGENERAL() {
        // view est l'image qui contiendra toutes les autres
        this.view = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                this.backgroundView = ImageIO.read(new File(chemin + "/background/background.png"));
                this.flecheView = ImageIO.read(new File(chemin + "/icon/iconfleche.png"));

            } catch (Exception e) {
                this.backgroundView = ImageIO.read(new File(winchemin + "background/background.png"));
                this.flecheView = ImageIO.read(new File(winchemin + "/icon/iconfleche.png"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // On initialise ces 2 boutons qui seront ré-utiliser de nombreuses fois.
        this.buttonQuitter = createImageOfMot("Quitter");
        this.buttonRetourMenu = createImageOfMot("Revenir au menu");
    }

    /// PARTIE MENU DEMARRER :
    // Initialise toutes les images du menu DEMARRER
    private void initMenuDemarrer() {
        this.buttonJouerSolo = createImageOfMot("Jouer en solo");
        this.button2joueur = createImageOfMot("Jouer a 2");
        this.buttonMultiJoueur = createImageOfMot("Mode multijoueurs");
        this.buttonLb = createImageOfMot("Classement");
    }

    // Met à jour les images du menu DEMARRER
    private void updateMenuDemarrer() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x
        // Son placement en y dépend de ce qu'elle pointe
        this.yfleche = (10 * height / 100) + fleche * sautLigne;
    }

    // Dessine toutes les images du menu DEMARRER
    public void afficheMenuDemarrer() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        // Affichage des boutons
        int x = (9 * width / 100), y = (10 * height / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        afficheMot(g2, buttonJouerSolo, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y = y + sautLigne;
        afficheMot(g2, button2joueur, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y = y + sautLigne;
        afficheMot(g2, buttonMultiJoueur, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y = y + sautLigne;
        afficheMot(g2, buttonLb, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y = y + sautLigne;
        afficheMot(g2, buttonQuitter, x, y, w, h, ecart, espacement);

        // Affichage de la fleche
        g2.drawImage(flecheView, xfleche, yfleche, wfleche, hfleche, null);

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Lance le menu DEMARRER
    private void runningMenuDemarrer() {
        this.sautLigne = 50;
        this.fleche = 0; // On pointe le premier bouton
        while (isMenuDemarrer) {
            updateMenuDemarrer();
            afficheMenuDemarrer();
        }
    }

    /// PARTIE MENU 2 (Nom...) :
    // Initialise toutes les images du menu 2
    private void initMenu2() {
        // On initialise les noms des joueurs.
        History h = new History();
        if (this.nbJoueur == 2) {
            this.nom1 = "MIZER 1";
            this.nom2 = "MIZER 2";
        } else // Si le joueur a déjà joué une partie, on prend le nom de la dernière partie.
            nom1 = (h.getLbData().size() > 1) ? h.getLbData().get(h.getLbData().size() - 1)[1] : "MIZER";

        this.messageNom = createImageOfMot("Entrez un nom ");
        this.buttonJouer = createImageOfMot("Jouer");
        this.nomJ1 = createImageOfMot(nom1);
        this.nomJ2 = createImageOfMot(nom2);
    }

    // Met à jour les images du menu 2
    private void updateMenu2() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x

        // Son placement en y dépend de ce qu'elle pointe
        if (this.nbJoueur == 1) {
            this.yfleche = (fleche == 0) ? ((12 * height / 100) + sautLigne)
                    : (12 * height / 100) + sautLigne * (fleche + 2);
        } else {
            this.yfleche = (fleche <= 1) ? ((12 * height / 100) + sautLigne * (2 * fleche + 1))
                    : (12 * height / 100) + sautLigne * (fleche + 3);

        }
        this.nomJ1 = createImageOfMot(nom1); // On update les noms à afficher.
        this.nomJ2 = (this.nbJoueur == 2) ? createImageOfMot(nom2) : createImageOfMot(null);
    }

    // Dessine toutes les images du menu 2
    public void afficheMenu2() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        // Affichage des boutons
        int x = (9 * width / 100), y = (12 * height / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        x = afficheMot(g2, messageNom, x, y, w, h, ecart, espacement);
        afficheDoublepoint(g2, x, y, 7, 7);
        x = (15 * width / 100);
        y += sautLigne;
        afficheMot(g2, nomJ1, x, y, w, h, ecart, espacement);
        if (nbJoueur == 2) {
            x = (9 * width / 100);
            y += sautLigne;
            x = afficheMot(g2, messageNom, x, y, w, h, ecart, espacement);
            afficheDoublepoint(g2, x, y, 7, 7);
            x = (15 * width / 100);
            y += sautLigne;
            afficheMot(g2, nomJ2, x, y, w, h, ecart, espacement);
        }
        x = (9 * width / 100);
        y += sautLigne * 2;
        afficheMot(g2, buttonJouer, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y += sautLigne;
        afficheMot(g2, buttonRetourMenu, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y += sautLigne;
        afficheMot(g2, buttonQuitter, x, y, w, h, ecart, espacement);

        // Affichage de la fleche
        g2.drawImage(flecheView, xfleche, yfleche, wfleche, hfleche, null);

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Lance le menu 2
    private void runningMenu2() {
        this.sautLigne = 50;
        this.fleche = 0; // On pointe le premier bouton
        while (isMenu2) {
            updateMenu2();
            afficheMenu2();
        }
    }

    /// PARTIE MENU FIN :
    // Initialise toutes les images du menu FIN
    private void initMenuFin() {
        this.scoreFinalView = new ArrayList<ArrayList<BufferedImage>>();
        this.hightScoreView = new ArrayList<ArrayList<BufferedImage>>();

        if (terrain.getListeJoueurs().size() == 1) { // S'il n'y a qu'1 joueur
            this.titreStatut = createImageOfMot("Game Over");
            // Ce qu'on va afficher pour le score en fin de partie :
            Joueur j = terrain.getListeJoueurs().get(0);
            String s = String.valueOf(j.getScore());
            ArrayList<BufferedImage> phrase = createImageOfMot("Votre score ");
            ArrayList<BufferedImage> score = createImageOfMot(s);
            scoreFinalView.add(phrase);
            scoreFinalView.add(score);

            // Ce qu'on va afficher pour le meilleur score en fin de partie :
            String hs = String.valueOf((new Classement()).getMaxScoreOfId(j.getId()));
            ArrayList<BufferedImage> phrase1 = createImageOfMot("Votre meilleur score ");
            ArrayList<BufferedImage> hscore = createImageOfMot(hs);
            String hs2 = String.valueOf((new Classement()).getMaxScoreGlobal());
            ArrayList<BufferedImage> phrase2 = createImageOfMot("Meilleur score global ");
            ArrayList<BufferedImage> hscore2 = createImageOfMot(hs2);
            hightScoreView.add(phrase1);
            hightScoreView.add(hscore);
            hightScoreView.add(phrase2);
            hightScoreView.add(hscore2);
        }

        if (terrain.getListeJoueurs().size() == 2) { // S'il y a 2 joueurs
            this.titreStatut = createImageOfMot("Fin de la course");
            // On adapte l'init de sorte à ce que la fonction d'affichage ne change pas
            Joueur j0 = terrain.getListeJoueurs().get(0), j1 = terrain.getListeJoueurs().get(1);
            int sc0 = j0.getScore(), sc1 = j1.getScore();
            String s0 = String.valueOf(sc0), s1 = String.valueOf(sc1);
            ArrayList<BufferedImage> phrase = createImageOfMot("Score de " + j0.getNom() + " ");
            ArrayList<BufferedImage> score = createImageOfMot(s0);
            scoreFinalView.add(phrase);
            scoreFinalView.add(score);

            ArrayList<BufferedImage> phrase1 = createImageOfMot("Score de " + j1.getNom() + " ");
            ArrayList<BufferedImage> hscore = createImageOfMot(s1);
            String winner = (sc0 == sc1) ? "Aucun" : (sc0 > sc1) ? j0.getNom() : j1.getNom();
            ArrayList<BufferedImage> phrase2 = createImageOfMot("Vainqueur ");
            ArrayList<BufferedImage> hscore2 = createImageOfMot(winner);
            hightScoreView.add(phrase1);
            hightScoreView.add(hscore);
            hightScoreView.add(phrase2);
            hightScoreView.add(hscore2);
        }
    }

    // Met à jour les images du menu FIN
    private void updateMenuFin() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x
        // Son placement en y dépend de ce qu'elle pointe
        this.yfleche = (12 * height / 100) + sautLigne * (6 + fleche);
    }

    // Dessine toutes les images du menu FIN
    public void afficheMenuFin() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        /// Affichage du message final :
        int x = (9 * width / 100), y = (12 * height / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        afficheMot(g2, titreStatut, x, y, w, h, ecart, espacement);
        /// Affichage des scores :
        // Affichage du score à cette partie :
        y += sautLigne * 2;
        x = afficheMot(g2, scoreFinalView.get(0), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, 7, 7);
        x += espacement;
        x = afficheMot(g2, scoreFinalView.get(1), x, y, w, h, ecart, espacement);

        // Affichage du meilleur score local :
        y += sautLigne;
        x = (9 * width / 100);
        x = afficheMot(g2, hightScoreView.get(0), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, 7, 7);
        x += espacement;
        x = afficheMot(g2, hightScoreView.get(1), x, y, w, h, ecart, espacement);

        // Affichage du meilleur score global :
        y += sautLigne;
        x = (9 * width / 100);
        x = afficheMot(g2, hightScoreView.get(2), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, 7, 7);
        x += espacement;
        x = afficheMot(g2, hightScoreView.get(3), x, y, w, h, ecart, espacement);

        /// Affichage des boutons :
        y += sautLigne * 2;
        x = (9 * width / 100);
        afficheMot(g2, buttonRetourMenu, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y += sautLigne;
        afficheMot(g2, buttonQuitter, x, y, w, h, ecart, espacement);

        /// Affichage de la fleche
        g2.drawImage(flecheView, xfleche, yfleche, wfleche, hfleche, null);

        /// Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Lance le menu FIN
    private void runningMenuFin() {
        this.sautLigne = 50;
        this.fleche = 0;
        while (isMenuFin) {
            updateMenuFin();
            afficheMenuFin();
        }
    }

    /// PARTIE CLASSEMENT :
    // Initialise toutes les images du menu CLASSEMENT
    private void initClassement() {
        this.lbView = new ArrayList<ArrayList<BufferedImage>>();
        this.titreStatut = createImageOfMot("Classement ");
        try {
            try {
                this.backgroundClView1 = ImageIO.read(new File(chemin + "/background/backgroundClassement1.png"));
                this.backgroundClView2 = ImageIO.read(new File(chemin + "/background/backgroundClassement2.png"));
            } catch (Exception e) {
                this.backgroundClView1 = ImageIO.read(new File(winchemin + "/background/backgroundClassement1.png"));
                this.backgroundClView2 = ImageIO.read(new File(winchemin + "/background/backgroundClassement2.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Met à jour les données du CLASSEMENT
    private void updateClassement() throws IOException {
        if (terrain.getListeJoueurs().size() == 1) { // On update le classement que s'il n'y a qu'un joueur
            Joueur j = terrain.getListeJoueurs().get(0);
            String score = String.valueOf(j.getScore());

            // Mise à jour dans le Classement Global
            Classement c = new Classement();
            c.ajoutClassement(j.getId(), j.getNom(), score);

            // Mise à jour dans le Classement Local
            History h = new History();
            h.ajoutClassement(j.getId(), j.getNom(), score);
        }
    }

    // Met à jour les images du CLASSEMENT
    private void updateClassementVue() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x
        // Son placement en y dépend de ce qu'elle pointe
        this.yfleche = (12 * height / 100) + ((4 + lbView.size() / 3) + fleche) * sautLigne;
    }

    // Crée l'affichage à jour du classement Global
    public void afficheClassement() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        /// Affichage du message final :
        int x = (9 * width / 100), y = (12 * height / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        x = afficheMot(g2, titreStatut, x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, 7, 7);

        // Affichage du classement :
        y += sautLigne * 2;
        x = (11 * width / 100);
        this.backgroundClView = backgroundClView1;
        for (int z = 0; z < lbView.size(); z = z + 3) {
            this.backgroundClView = (z % 2 == 0) ? backgroundClView1 : backgroundClView2;
            g2.drawImage(backgroundClView, x * 85 / 100, y - h / 6, width, h * 3 / 2, null);
            x = afficheMot(g2, lbView.get(z), x, y, w, h, ecart, espacement);
            x += espacement / 2;
            affichePoint(g2, x, y + h - 7, 7, 7);
            x += espacement * 2;
            x = afficheMot(g2, lbView.get(z + 1), x, y, w, h, ecart, espacement);
            x = afficheDoublepoint(g2, x, y, 7, 7);
            x += espacement * 2;
            x = afficheMot(g2, lbView.get(z + 2), x, y, w, h, ecart, espacement);
            x = (11 * width / 100);
            y += sautLigne;
        }

        /// Affichage des boutons :
        y += sautLigne * 2;
        x = (9 * width / 100);
        afficheMot(g2, buttonRetourMenu, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y += sautLigne;
        afficheMot(g2, buttonQuitter, x, y, w, h, ecart, espacement);

        /// Affichage de la fleche
        g2.drawImage(flecheView, xfleche, yfleche, wfleche, hfleche, null);

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner
    private void runningClassement() {
        this.fleche = 0;
        this.sautLigne = 50;

        // On récupère les données du classementque l'on va afficher
        Classement c = new Classement();
        ArrayList<String[]> cl = c.getLbData();

        int imax = (cl.size() > 10) ? 10 : cl.size();
        for (int i = 0; i < imax; ++i) {
            String n = cl.get(i)[1];
            String s = cl.get(i)[2];

            ArrayList<BufferedImage> rank = createImageOfMot(String.valueOf(i + 1));
            ArrayList<BufferedImage> nom = createImageOfMot(n + " ");
            ArrayList<BufferedImage> score = createImageOfMot(s);
            lbView.add(rank);
            lbView.add(nom);
            lbView.add(score);
        }

        while (isClassement) {
            updateClassementVue();
            afficheClassement();
        }
    }

    /// PARTIE GAME :
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

    // Initialise toutes les images de la GAME
    private void initGame() {
        // Stock des listes qui elles-mêmes stockes les données d'image de chaque joueur
        // et qui ne changent jamais, i.e le perso et le nom, contrairement au score
        joueurDataList = new ArrayList<ArrayList<BufferedImage>>();

        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                terrainView = ImageIO.read(new File(chemin + "/background/terrainBackground.png"));
                platformeBaseView = ImageIO.read(new File(chemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(chemin + "/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/background/scoreBackground1.png"));
                projectileView = ImageIO.read(new File(chemin + "/projectile.png"));

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
                terrainView = ImageIO.read(new File(winchemin + "/background/terrainBackground.png"));
                platformeBaseView = ImageIO.read(new File(winchemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(winchemin + "/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File(winchemin + "/background/scoreBackground1.png"));
                projectileView = ImageIO.read(new File(winchemin + "/projectile.png"));

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

    // Met à jour l'affichage de la GAME
    private void updateGame(double dTime) {
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
        terrain.update(dTime);
    }

    // Dessine toutes les images de la GAME
    public void afficheGame() {
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

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);
        g.dispose(); // On libère les ressource
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

    // Fait tourner la GAME (avec ups constant)
    private void runningGame() {
        this.deltaTime = 10;
        // Gestion de l'ups constant
        double cnt = 0.0; // Compteur du nombre d'update
        double acc = 0.0; // Accumulateur qui va gérer les pertes de temps
        long t0 = System.currentTimeMillis(); // Temps actuel
        while (isRunningGame) { // Tant que le jeu tourne
            if (!terrain.isPause()) { // Tant qu'on appuie pas sur pause
                long t1 = System.currentTimeMillis();
                long t = t1 - t0;
                t0 = System.currentTimeMillis();
                acc += t;
                while (acc > deltaTime) { // Si on peut update
                    updateGame(deltaTime); // On met à jour les variables
                    // On retire 1 Δ à chaque update. Si le reste > 0 & < Δ, ça veut dire qu'on a
                    // un retard, qu'on stock pour l'ajouter à l'étape suivante.
                    // Si on a reste > Δ, on relance cette boucle
                    acc -= deltaTime;
                    cnt += deltaTime; // On accumule le nombre d'update
                }
            }
            afficheGame(); // On affiche les images une fois les données update
        }
    }

    // Fait tourner le jeu Doodle Jump au complet
    // Cette méthode contient les traitements
    @Override
    public void run() {
        try {
            // Cette méthode (ci dessous) demande à ce que ce composant obtienne le focus.
            // Le focus est le fait qu'un composant soit sélectionné ou pas.
            // Le composant doit être afficheable (OK grâce à addNotify())
            this.requestFocusInWindow();

            // ETAPE 1 : On initialise les images qui ne changent pas en fonction du statut
            initGENERAL();
            initMenuDemarrer(); // On initialise les images du menu DEMARRER.

            while (!isQuitte) { // Tant qu'on a pas quitter le jeu :

                // ETAPE 2 : On gère les différent statut du jeu !
                // Si on est au niveau du menu DEMARRER :
                if (isMenuDemarrer && !isClassement && !isMenu2 && !isRunningGame && !isMenuFin) {
                    runningMenuDemarrer(); // On lance le menu DEMARRER.
                }
                // Si on a cliqué sur le boutton "Classement" :
                if (!isMenuDemarrer && isClassement && !isMenu2 && !isRunningGame && !isMenuFin) {
                    initClassement(); // On initialise les images du CLASSEMENT.
                    runningClassement(); // On lance le CLASSEMENT.
                }
                // Si on a cliqué sur le boutton "Jouer solo" :
                if (!isMenuDemarrer && !isClassement && isMenu2 && !isRunningGame && !isMenuFin) {
                    initMenu2();
                    runningMenu2();
                }

                // Si on a lancé une GAME :
                if (!isMenuDemarrer && !isClassement && !isMenu2 && isRunningGame && !isMenuFin) {
                    initGame(); // On initialise les images de la GAME.
                    if (terrain.multiplayer) { // Si on est en mode multijoueur
                        Thread t = new Thread(new ThreadMouvement(terrain)); // ???
                        t.start();
                    }
                    runningGame(); // On lance la GAME (en ups constant).
                }
                // Si c'est la fin de la GAME (quelqu'un a perdu) :
                if (!isMenuDemarrer && !isClassement && !isMenu2 && !isRunningGame && !isMenuFin && endGame()) {
                    // On met à jour toutes les variables boolean.
                    isMenuDemarrer = false;
                    isClassement = false;
                    isRunningGame = false;
                    isMenuFin = true;
                    updateClassement(); // On met à jour le classement et l'historique.
                }
                // Si on a fini la GAME sans erreur :
                if (!isMenuDemarrer && !isClassement && !isRunningGame && isMenuFin) {
                    initMenuFin(); // On initialise les images du menu FIN.
                    runningMenuFin(); // On lance le menu FIN.
                }
            }
            System.exit(0); // Si on a quitté le jeu, on ferme tout le programme.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Rend ce composant affichable en le connectant à une ressource d'écran
    @Override
    public void addNotify() {
        super.addNotify();
        // Si on a toujours pas lancer le jeu
        if (this.thread == null) {
            // Créer une nouvelle instance en précisant les traitements à exécuter (run)
            // This est l'objet qui implémente Runnable (run()), contenant les traitements
            this.thread = new Thread(this);
            isQuitte = false;
            isMenuDemarrer = true; // Indique le jeu est lancé
            this.thread.start(); // Invoque la méthode run()
        }
    }

    // Gestion des boutons
    private void pause() {
        this.terrain.setPause(!this.terrain.isPause());
        this.menuPause = new JFrame();
        this.menuPause.setBounds((int) terrain.getWidth() * 3 / 2 - 50, (int) terrain.getHeight() / 2 - 60, 150, 120);
        this.menuPause.setResizable(false);
        this.menuPause.setLayout(new FlowLayout());
        this.menuPause.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JButton cont = new JButton("Continuer");
        JButton exit = new JButton("Menu principal");

        this.menuPause.add(cont);
        this.menuPause.add(exit);
        this.menuPause.setVisible(true);

        cont.addActionListener(ev -> {
            this.menuPause.dispose();
            this.terrain.setPause(!this.terrain.isPause());
        });

        exit.addActionListener(ev -> {
            this.menuPause.dispose();
            JFrame retourMenu = new App();
            retourMenu.setVisible(true);
        });
    }

    @Override
    public void keyPressed(KeyEvent e) { // On est actuellement entrain d'appuyer sur des boutons :
        if (isRunningGame) { // Si on est en pleine partie :
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
                pause();
            }
        }

        if (isMenuDemarrer) { // Si on est au niveau du menu DEMARRER :
            /// Gestion du bouton "ENTREE" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
                if (this.fleche == 0) { // La flèche pointe sur le bouton "Jouer Solo" :
                    this.nbJoueur = 1; // On initialise le nombre de joueurs.
                    isMenuDemarrer = false; // On quitte le menu DEMARRER
                    isMenu2 = true;
                }
                if (this.fleche == 1) { // La flèche pointe sur le bouton "Jouer à 2" :
                    this.nbJoueur = 2; // On initialise le nombre de joueurs.
                    isMenuDemarrer = false; // On quitte le menu DEMARRER
                    isMenu2 = true;
                }

                if (this.fleche == 2) { // La flèche pointe sur le bouton "Mode multijoueur" :
                    this.nbJoueur = 2; // On initialise le nombre de joueurs.
                    this.multijoueur = true;
                    int option = JOptionPane.showConfirmDialog(this, "Voulez-vous host la partie ?",
                            "Paramètrage multijoueur",
                            JOptionPane.YES_NO_OPTION);
                    System.out.println(option);
                    if (option == 0) {
                        this.host = true;
                        try {
                            this.serveur = new Serveur();
                            this.serveur.run();
                        } catch (IOException io) {
                            JOptionPane.showMessageDialog(null, "Aucun joueur n'a essayé de se connecter !", "Erreur !",
                                    JOptionPane.ERROR_MESSAGE); // A implementer sur l'interface
                            System.exit(-1);
                        }
                    } else {
                        this.host = false;
                        this.jconnect = new JoueurConnecte();
                        this.jconnect.connecter();
                    }
                    isMenuDemarrer = false; // On quitte le menu DEMARRER
                    isRunningGame = true;
                }

                if (this.fleche == 3) { // La flèche pointe sur le bouton "Classement" :
                    isClassement = true;
                    isMenuDemarrer = false;
                }
                if (this.fleche == 4) { // La flèche pointe sur le bouton "Quitter" :
                    System.out.println("À la prochaine !");
                    isQuitte = true; // On quitte l'application.
                    System.exit(0); // On ferme toutes les fenêtres & le programme.
                }
            }

            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.fleche = (this.fleche == 0) ? 4 : this.fleche - 1;

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.fleche = (this.fleche == 4) ? 0 : this.fleche + 1;
        }

        if (isMenuFin) { // Si on est au niveau du menu FIN :
            /// Gestion du bouton "ENTREE" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
                if (this.fleche == 0) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                    isMenuFin = false; // On quitte le menu FIN.
                    isMenuDemarrer = true; // On entre dans le menu DEMARRER.
                }
                if (this.fleche == 1) { // La flèche pointe sur le bouton "Quitter" :
                    System.out.println("À la prochaine !");
                    isQuitte = true; // On quitte l'application.
                    System.exit(0); // On ferme toutes les fenêtres & le programme.
                }
            }

            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.fleche = (this.fleche == 0) ? 1 : this.fleche - 1;

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.fleche = (this.fleche == 1) ? 0 : this.fleche + 1;
        }

        if (isClassement) { // Si on est au niveau du CLASSEMENT :
            /// Gestion du bouton "ENTREE" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
                if (this.fleche == 0) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                    isClassement = false; // On quitte le menu FIN.
                    isMenuDemarrer = true; // On entre dans le menu DEMARRER.
                }
                if (this.fleche == 1) { // La flèche pointe sur le bouton "Quitter" :
                    System.out.println("À la prochaine !");
                    isQuitte = true; // On quitte l'application.
                    System.exit(0); // On ferme toutes les fenêtres & le programme.
                }
            }
            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.fleche = (this.fleche == 0) ? 1 : this.fleche - 1;

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.fleche = (this.fleche == 1) ? 0 : this.fleche + 1;
        }

        if (isMenu2) {
            try {
                if (this.nbJoueur == 1) {
                    if (this.fleche == 0) { // La flèche pointe sur le bouton "Nom 1" :
                        nom1 = (nom1.length() < 16) ? nom1 += keyWriterNom(e) : nom1;
                        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                            nom1 = "";
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (this.fleche == 1) { // La flèche pointe sur le bouton "Jouer" :
                            createPartie(); // On crée une partie.
                            isMenu2 = false;
                            isRunningGame = true;
                        }

                        if (this.fleche == 2) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                            isMenu2 = false; // On quitte le menu 2.
                            isMenuDemarrer = true; // On entre dans le menu DEMARRER.
                        }
                        if (this.fleche == 3) { // La flèche pointe sur le bouton "Quitter" :
                            System.out.println("À la prochaine !");
                            isQuitte = true; // On quitte l'application.
                            System.exit(0); // On ferme toutes les fenêtres & le programme.
                        }
                    }

                    /// Gestion de la flèche :
                    if (e.getKeyCode() == KeyEvent.VK_UP)
                        this.fleche = (this.fleche == 0) ? 3 : this.fleche - 1;

                    if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        this.fleche = (this.fleche == 3) ? 0 : this.fleche + 1;

                } else {
                    if (this.fleche == 0) { // La flèche pointe sur le bouton "Nom 1" :
                        nom1 = (nom1.length() < 16) ? nom1 += keyWriterNom(e) : nom1;
                        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                            nom1 = "";
                    }
                    if (this.fleche == 1) { // La flèche pointe sur le bouton "Nom 2" :
                        nom2 = (nom2.length() < 16) ? nom2 += keyWriterNom(e) : nom2;
                        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                            nom2 = "";
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (this.fleche == 2) { // La flèche pointe sur le bouton "Jouer" :
                            createPartie(); // On crée une partie.
                            isMenu2 = false;
                            isRunningGame = true;
                        }

                        if (this.fleche == 3) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                            isMenu2 = false; // On quitte le menu 2.
                            isMenuDemarrer = true; // On entre dans le menu DEMARRER.
                        }
                        if (this.fleche == 4) { // La flèche pointe sur le bouton "Quitter" :
                            System.out.println("À la prochaine !");
                            isQuitte = true; // On quitte l'application.
                            System.exit(0); // On ferme toutes les fenêtres & le programme.
                        }
                    }

                    /// Gestion de la flèche :
                    if (e.getKeyCode() == KeyEvent.VK_UP)
                        this.fleche = (this.fleche == 0) ? 4 : this.fleche - 1;

                    if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        this.fleche = (this.fleche == 4) ? 0 : this.fleche + 1;

                }
            } catch (Exception exe) {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // On relâche les boutons :
        if (isRunningGame) { // Si on est en pleine partie :
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
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private String keyWriterNom(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_0 || e.getKeyCode() == KeyEvent.VK_NUMPAD0 || e.getKeyChar() == '0')
            return "0";
        if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyChar() == '1')
            return "1";
        if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyChar() == '2')
            return "2";
        if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyChar() == '3')
            return "3";
        if (e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyChar() == '4')
            return "4";
        if (e.getKeyCode() == KeyEvent.VK_5 || e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyChar() == '5')
            return "5";
        if (e.getKeyCode() == KeyEvent.VK_6 || e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyChar() == '6')
            return "6";
        if (e.getKeyCode() == KeyEvent.VK_7 || e.getKeyCode() == KeyEvent.VK_NUMPAD7 || e.getKeyChar() == '7')
            return "7";
        if (e.getKeyCode() == KeyEvent.VK_8 || e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyChar() == '8')
            return "8";
        if (e.getKeyCode() == KeyEvent.VK_9 || e.getKeyCode() == KeyEvent.VK_NUMPAD9 || e.getKeyChar() == '9')
            return "9";
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyChar() == 'à' || e.getKeyChar() == 'À')
            return "A";
        if (e.getKeyCode() == KeyEvent.VK_B)
            return "B";
        if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyChar() == 'ç')
            return "C";
        if (e.getKeyCode() == KeyEvent.VK_D)
            return "D";
        if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyChar() == 'é' || e.getKeyChar() == 'è')
            return "E";
        if (e.getKeyCode() == KeyEvent.VK_F)
            return "F";
        if (e.getKeyCode() == KeyEvent.VK_G)
            return "G";
        if (e.getKeyCode() == KeyEvent.VK_H)
            return "H";
        if (e.getKeyCode() == KeyEvent.VK_I)
            return "I";
        if (e.getKeyCode() == KeyEvent.VK_J)
            return "J";
        if (e.getKeyCode() == KeyEvent.VK_K)
            return "K";
        if (e.getKeyCode() == KeyEvent.VK_L)
            return "L";
        if (e.getKeyCode() == KeyEvent.VK_M)
            return "M";
        if (e.getKeyCode() == KeyEvent.VK_N)
            return "N";
        if (e.getKeyCode() == KeyEvent.VK_O)
            return "O";
        if (e.getKeyCode() == KeyEvent.VK_P)
            return "P";
        if (e.getKeyCode() == KeyEvent.VK_Q)
            return "Q";
        if (e.getKeyCode() == KeyEvent.VK_R)
            return "R";
        if (e.getKeyCode() == KeyEvent.VK_S)
            return "S";
        if (e.getKeyCode() == KeyEvent.VK_T)
            return "T";
        if (e.getKeyCode() == KeyEvent.VK_U)
            return "U";
        if (e.getKeyCode() == KeyEvent.VK_V)
            return "V";
        if (e.getKeyCode() == KeyEvent.VK_W)
            return "W";
        if (e.getKeyCode() == KeyEvent.VK_X)
            return "X";
        if (e.getKeyCode() == KeyEvent.VK_Y)
            return "Y";
        if (e.getKeyCode() == KeyEvent.VK_Z)
            return "Z";
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            return " ";
        return "";
    }

    public String getSkin() {
        return this.skin;
    }
}