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
    public static boolean isQuitte, isRunningGame, isMenuDemarrer, isMenuLancement, isMenuFin, isMenuClassement,
            isMenuPause;
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

    private MenuDemarrer eMenuDemarrer;
    private MenuClassement eMenuClassement;
    private MenuLancement eMenuLancement;
    private MenuFin eMenuFin;
    private Game eGame;
    public Etat eActuel;

    public Vue(App frame, String skin) {
        // Taille du panel
        this.width = frame.getWidth();
        this.height = frame.getHeight();
        this.setPreferredSize(new Dimension(this.width / 3, (int) (this.height * 0.95)));

        this.skin = skin;
        this.chemin = (new File("gui/images/" + skin + "/")).getAbsolutePath();
        this.winchemin = "src/gui/images/" + skin + "/";

        // Gestion d'évènements boutons
        this.addKeyListener(this);
    }

    // Méthodes de la classe

    // PARTIE GENERALE
    // On initialise toutes les images qui seront utilisées à plusieurs endroits
    private void initGENERAL() {
        this.eMenuDemarrer.initFixe();
        this.eMenuClassement.initFixe();
        this.eMenuLancement.initFixe();
        this.eGame.initFixe();
        this.eMenuFin.initFixe();
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

            this.eMenuDemarrer = new MenuDemarrer(this);
            this.eMenuClassement = new MenuClassement(this);
            this.eMenuLancement = new MenuLancement(this);
            this.eGame = new Game(this);
            this.eMenuFin = new MenuFin(this);
            this.eActuel = this.eMenuDemarrer;

            // ETAPE 1 : On initialise les images qui ne changent pas en fonction du statut
            initGENERAL();

            while (!isQuitte) { // Tant qu'on a pas quitter le jeu :

                // ETAPE 2 : On gère les différent statut du jeu !
                // Si on est au niveau du menu DEMARRER :
                if (isMenuDemarrer && !isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin) {
                    // this.eActuel = this.eMenuDemarrer;
                    this.eMenuDemarrer = new MenuDemarrer(this);
                    this.eMenuDemarrer.running();
                }
                // Si on a cliqué sur le boutton "Classement" :
                if (!isMenuDemarrer && isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin) {
                    this.eMenuClassement = new MenuClassement(this);
                    this.eMenuClassement.init();
                    this.eMenuClassement.running();
                    // this.eActuel = this.eMenuClassement;
                }
                // Si on a cliqué sur le boutton "Jouer solo" :
                if (!isMenuDemarrer && !isMenuClassement && isMenuLancement && !isRunningGame && !isMenuFin) {
                    this.eMenuLancement = new MenuLancement(this);
                    this.eMenuLancement.init();
                    this.eMenuLancement.running();
                    // this.eActuel = this.eMenuLancement;
                }

                // Si on a lancé une GAME :
                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && isRunningGame && !isMenuFin) {
                    this.eGame = new Game(this);
                    this.eGame.init(); // On initialise les images de la GAME.
                    this.eGame.running();
                    if (terrain.multiplayer) { // Si on est en mode multijoueur
                        Thread t = new Thread(new ThreadMouvement(terrain)); // ???
                        t.start();
                    }
                    // this.eActuel = this.eGame;
                }

                // Si c'est la fin de la GAME (quelqu'un a perdu) :
                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin
                        && ((Game) this.eActuel).endGame()) {
                    // On met à jour toutes les variables boolean.
                    isMenuDemarrer = false;
                    isMenuClassement = false;
                    isRunningGame = false;
                    isMenuFin = true;
                    // On met à jour le classement et l'historique.
                    this.eMenuClassement.updateClassement();
                }
                // Si on a fini la GAME sans erreur :
                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && !isRunningGame && isMenuFin) {
                    this.eMenuFin = new MenuFin(this);
                    this.eMenuFin.init(); // On initialise les images du menu FIN.
                    this.eMenuFin.running();
                    // this.eActuel = this.eMenuFin;
                }

                // On exécute les différentes actions.
                // this.eActuel.running(getGraphics());// On lance le menu DEMARRER.

                System.out.println();
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
        System.out.println("Vue.KeyPressed :");
        if (isMenuDemarrer) { // Si on est au niveau du menu DEMARRER :
            this.eMenuDemarrer.keyControlPressed(e);
        }

        if (isMenuClassement) { // Si on est au niveau du CLASSEMENT :
            this.eMenuClassement.keyControlPressed(e);
        }

        if (isMenuLancement) {
            this.eMenuLancement.keyControlPressed(e);
        }

        if (isRunningGame) { // Si on est en pleine partie :
            this.eGame.keyControlPressed(e);
        }

        if (isMenuFin) { // Si on est au niveau du menu FIN :
            this.eMenuFin.keyControlPressed(e);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) { // On relâche les boutons :
        if (isRunningGame) { // Si on est en pleine partie :
            this.eGame.keyControlReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Getter & setter
    public static boolean isQuitte() {
        return isQuitte;
    }

    public static void setQuitte(boolean isQuitte) {
        Vue.isQuitte = isQuitte;
    }

    public static boolean isRunningGame() {
        return isRunningGame;
    }

    public static void setRunningGame(boolean isRunningGame) {
        Vue.isRunningGame = isRunningGame;
    }

    public static boolean isMenuDemarrer() {
        return isMenuDemarrer;
    }

    public static void setMenuDemarrer(boolean isMenuDemarrer) {
        Vue.isMenuDemarrer = isMenuDemarrer;
    }

    public static boolean isMenuPause() {
        return isMenuPause;
    }

    public static void setMenuPause(boolean isMenuPause) {
        Vue.isMenuPause = isMenuPause;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFleche() {
        return fleche;
    }

    public void setFleche(int fleche) {
        this.fleche = fleche;
    }

    public int getXfleche() {
        return xfleche;
    }

    public void setXfleche(int xfleche) {
        this.xfleche = xfleche;
    }

    public int getYfleche() {
        return yfleche;
    }

    public void setYfleche(int yfleche) {
        this.yfleche = yfleche;
    }

    public int getWfleche() {
        return wfleche;
    }

    public void setWfleche(int wfleche) {
        this.wfleche = wfleche;
    }

    public int getHfleche() {
        return hfleche;
    }

    public void setHfleche(int hfleche) {
        this.hfleche = hfleche;
    }

    public int getSautLigne() {
        return sautLigne;
    }

    public void setSautLigne(int sautLigne) {
        this.sautLigne = sautLigne;
    }

    public int getNbJoueur() {
        return nbJoueur;
    }

    public void setNbJoueur(int nbJoueur) {
        this.nbJoueur = nbJoueur;
    }

    public JFrame getMenuPause() {
        return menuPause;
    }

    public void setMenuPause(JFrame menuPause) {
        this.menuPause = menuPause;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getWinchemin() {
        return winchemin;
    }

    public void setWinchemin(String winchemin) {
        this.winchemin = winchemin;
    }

    public BufferedImage getView() {
        return view;
    }

    public void setView(BufferedImage view) {
        this.view = view;
    }

    public BufferedImage getBackgroundView() {
        return backgroundView;
    }

    public void setBackgroundView(BufferedImage backgroundView) {
        this.backgroundView = backgroundView;
    }

    public BufferedImage getBackgroundClView() {
        return backgroundClView;
    }

    public void setBackgroundClView(BufferedImage backgroundClView) {
        this.backgroundClView = backgroundClView;
    }

    public BufferedImage getBackgroundClView1() {
        return backgroundClView1;
    }

    public void setBackgroundClView1(BufferedImage backgroundClView1) {
        this.backgroundClView1 = backgroundClView1;
    }

    public BufferedImage getBackgroundClView2() {
        return backgroundClView2;
    }

    public void setBackgroundClView2(BufferedImage backgroundClView2) {
        this.backgroundClView2 = backgroundClView2;
    }

    public BufferedImage getFlecheView() {
        return flecheView;
    }

    public void setFlecheView(BufferedImage flecheView) {
        this.flecheView = flecheView;
    }

    public BufferedImage getTerrainView() {
        return terrainView;
    }

    public void setTerrainView(BufferedImage terrainView) {
        this.terrainView = terrainView;
    }

    public BufferedImage getPlatformeBaseView() {
        return platformeBaseView;
    }

    public void setPlatformeBaseView(BufferedImage platformeBaseView) {
        this.platformeBaseView = platformeBaseView;
    }

    public BufferedImage getPlatformeMobileView() {
        return platformeMobileView;
    }

    public void setPlatformeMobileView(BufferedImage platformeMobileView) {
        this.platformeMobileView = platformeMobileView;
    }

    public BufferedImage getScoreBackgroundView() {
        return scoreBackgroundView;
    }

    public void setScoreBackgroundView(BufferedImage scoreBackgroundView) {
        this.scoreBackgroundView = scoreBackgroundView;
    }

    public BufferedImage getProjectileView() {
        return projectileView;
    }

    public void setProjectileView(BufferedImage projectileView) {
        this.projectileView = projectileView;
    }

    public ArrayList<BufferedImage> getButtonJouer() {
        return buttonJouer;
    }

    public void setButtonJouer(ArrayList<BufferedImage> buttonJouer) {
        this.buttonJouer = buttonJouer;
    }

    public ArrayList<BufferedImage> getButton2joueur() {
        return button2joueur;
    }

    public void setButton2joueur(ArrayList<BufferedImage> button2joueur) {
        this.button2joueur = button2joueur;
    }

    public ArrayList<BufferedImage> getButtonMultiJoueur() {
        return buttonMultiJoueur;
    }

    public void setButtonMultiJoueur(ArrayList<BufferedImage> buttonMultiJoueur) {
        this.buttonMultiJoueur = buttonMultiJoueur;
    }

    public ArrayList<BufferedImage> getButtonLb() {
        return buttonLb;
    }

    public void setButtonLb(ArrayList<BufferedImage> buttonLb) {
        this.buttonLb = buttonLb;
    }

    public ArrayList<BufferedImage> getButtonQuitter() {
        return buttonQuitter;
    }

    public void setButtonQuitter(ArrayList<BufferedImage> buttonQuitter) {
        this.buttonQuitter = buttonQuitter;
    }

    public ArrayList<BufferedImage> getButtonRetourMenu() {
        return buttonRetourMenu;
    }

    public void setButtonRetourMenu(ArrayList<BufferedImage> buttonRetourMenu) {
        this.buttonRetourMenu = buttonRetourMenu;
    }

    public ArrayList<BufferedImage> getTitreStatut() {
        return titreStatut;
    }

    public void setTitreStatut(ArrayList<BufferedImage> titreStatut) {
        this.titreStatut = titreStatut;
    }

    public ArrayList<BufferedImage> getMessageNom() {
        return messageNom;
    }

    public void setMessageNom(ArrayList<BufferedImage> messageNom) {
        this.messageNom = messageNom;
    }

    public ArrayList<ArrayList<BufferedImage>> getJoueurDataList() {
        return joueurDataList;
    }

    public void setJoueurDataList(ArrayList<ArrayList<BufferedImage>> joueurDataList) {
        this.joueurDataList = joueurDataList;
    }

    public ArrayList<ArrayList<BufferedImage>> getLbView() {
        return lbView;
    }

    public void setLbView(ArrayList<ArrayList<BufferedImage>> lbView) {
        this.lbView = lbView;
    }

    public ArrayList<ArrayList<BufferedImage>> getScoreFinalView() {
        return scoreFinalView;
    }

    public void setScoreFinalView(ArrayList<ArrayList<BufferedImage>> scoreFinalView) {
        this.scoreFinalView = scoreFinalView;
    }

    public ArrayList<ArrayList<BufferedImage>> getHightScoreView() {
        return hightScoreView;
    }

    public void setHightScoreView(ArrayList<ArrayList<BufferedImage>> hightScoreView) {
        this.hightScoreView = hightScoreView;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public ThreadMouvement getThreadMvt() {
        return threadMvt;
    }

    public void setThreadMvt(ThreadMouvement threadMvt) {
        this.threadMvt = threadMvt;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isMultijoueur() {
        return multijoueur;
    }

    public void setMultijoueur(boolean multijoueur) {
        this.multijoueur = multijoueur;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public Serveur getServeur() {
        return serveur;
    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    public JoueurConnecte getJconnect() {
        return jconnect;
    }

    public void setJconnect(JoueurConnecte jconnect) {
        this.jconnect = jconnect;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public MenuDemarrer geteMenuDemarrer() {
        return eMenuDemarrer;
    }

    public void seteMenuDemarrer(MenuDemarrer eMenuDemarrer) {
        this.eMenuDemarrer = eMenuDemarrer;
    }

    public MenuClassement geteMenuClassement() {
        return eMenuClassement;
    }

    public void seteMenuClassement(MenuClassement eMenuClassement) {
        this.eMenuClassement = eMenuClassement;
    }

    public MenuLancement geteMenuLancement() {
        return eMenuLancement;
    }

    public void seteMenuLancement(MenuLancement eMenuLancement) {
        this.eMenuLancement = eMenuLancement;
    }

    public MenuFin geteMenuFin() {
        return eMenuFin;
    }

    public void seteMenuFin(MenuFin eMenuFin) {
        this.eMenuFin = eMenuFin;
    }

    public Game geteGame() {
        return eGame;
    }

    public void seteGame(Game eGame) {
        this.eGame = eGame;
    }

    public Etat geteActuel() {
        return eActuel;
    }

    public void seteActuel(Etat eActuel) {
        this.eActuel = eActuel;
    }

    public ArrayList<BufferedImage> getButtonJouerSolo() {
        return buttonJouerSolo;
    }

    public void setButtonJouerSolo(ArrayList<BufferedImage> buttonJouerSolo) {
        this.buttonJouerSolo = buttonJouerSolo;
    }

    public static boolean isMenuLancement() {
        return isMenuLancement;
    }

    public static void setMenuLancement(boolean isMenuLancement) {
        Vue.isMenuLancement = isMenuLancement;
    }

    public static boolean isMenuFin() {
        return isMenuFin;
    }

    public static void setMenuFin(boolean isMenuFin) {
        Vue.isMenuFin = isMenuFin;
    }

    public static boolean isMenuClassement() {
        return isMenuClassement;
    }

    public static void setMenuClassement(boolean isMenuClassement) {
        Vue.isMenuClassement = isMenuClassement;
    }

    public String getNom1() {
        return nom1;
    }

    public void setNom1(String nom1) {
        this.nom1 = nom1;
    }

    public String getNom2() {
        return nom2;
    }

    public void setNom2(String nom2) {
        this.nom2 = nom2;
    }

    public ArrayList<BufferedImage> getNomJ1() {
        return nomJ1;
    }

    public void setNomJ1(ArrayList<BufferedImage> nomJ1) {
        this.nomJ1 = nomJ1;
    }

    public ArrayList<BufferedImage> getNomJ2() {
        return nomJ2;
    }

    public void setNomJ2(ArrayList<BufferedImage> nomJ2) {
        this.nomJ2 = nomJ2;
    }

}