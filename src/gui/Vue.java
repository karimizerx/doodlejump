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
    private Terrain terrain; // Le terrain sur lequel on joue
    private Thread thread; // La thread reliée à ce pannel, qui lance l'exécution
    private String skin;
    private JFrame menuPause; // Le menu pause
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
                System.out.println("Vue.Run 1 :");
                System.out
                        .println("isMenuDemarrer : " + isMenuDemarrer + ", " + (this.eActuel instanceof MenuDemarrer));
                System.out.println(
                        "isMenuClassement : " + isMenuClassement + ", " + (this.eActuel instanceof MenuClassement));
                System.out.println(
                        "isMenuLancement : " + isMenuLancement + ", " + (this.eActuel instanceof MenuLancement));
                System.out.println("isRunningGame : " + isRunningGame + ", " + (this.eActuel instanceof Game));
                System.out.println("isMenuFin : " + isMenuFin + ", " + (this.eActuel instanceof MenuFin));
                System.out.println();

                // ETAPE 2 : On gère les différent statut du jeu !
                // Si on est au niveau du menu DEMARRER :
                if (isMenuDemarrer && !isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin) {
                    // this.eActuel = this.eMenuDemarrer;
                    this.eMenuDemarrer.running(getGraphics());
                }
                // Si on a cliqué sur le boutton "Classement" :
                if (!isMenuDemarrer && isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin) {
                    this.eMenuClassement = new MenuClassement(this);
                    this.eMenuClassement.init();
                    // this.eActuel = this.eMenuClassement;
                }
                // Si on a cliqué sur le boutton "Jouer solo" :
                if (!isMenuDemarrer && !isMenuClassement && isMenuLancement && !isRunningGame && !isMenuFin) {
                    this.eMenuLancement = new MenuLancement(this);
                    this.eMenuLancement.init();
                    // this.eActuel = this.eMenuLancement;
                }

                // Si on a lancé une GAME :
                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && isRunningGame && !isMenuFin) {
                    this.eGame = new Game(this);
                    this.eGame.init(); // On initialise les images de la GAME.
                    // if (terrain.multiplayer) { // Si on est en mode multijoueur
                    // Thread t = new Thread(new ThreadMouvement(terrain)); // ???
                    // t.start();
                    // }
                    // this.eActuel = this.eGame;
                }

                // Si c'est la fin de la GAME (quelqu'un a perdu) :
                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin
                        && ((Game) this.eActuel).isEndGame()) {
                    // On met à jour toutes les variables boolean.
                    isMenuDemarrer = false;
                    isMenuClassement = false;
                    isRunningGame = false;
                    isMenuFin = true;
                    // On met à jour le classement et l'historique.
                    this.eMenuClassement.updateClassement();
                }
                // Si on a fini la GAME sans erreur :
                if (!isMenuDemarrer && !isMenuClassement && !isRunningGame && isMenuFin) {
                    this.eMenuFin = new MenuFin(this);
                    this.eMenuFin.init(); // On initialise les images du menu FIN.
                    // this.eActuel = this.eMenuFin;
                }

                // On exécute les différentes actions.
                // this.eActuel.running(getGraphics());// On lance le menu DEMARRER.

                System.out.println("Vue.Run 2 :");
                System.out
                        .println("isMenuDemarrer : " + isMenuDemarrer + ", " + (this.eActuel instanceof MenuDemarrer));
                System.out.println(
                        "isMenuClassement : " + isMenuClassement + ", " + (this.eActuel instanceof MenuClassement));
                System.out.println(
                        "isMenuLancement : " + isMenuLancement + ", " + (this.eActuel instanceof MenuLancement));
                System.out.println("isRunningGame : " + isRunningGame + ", " + (this.eActuel instanceof Game));
                System.out.println("isMenuFin : " + isMenuFin + ", " + (this.eActuel instanceof MenuFin));
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
        System.out.println("isMenuDemarrer : " + this.isMenuDemarrer + ", " + (this.eActuel instanceof MenuDemarrer));
        System.out.println(
                "isMenuClassement : " + this.isMenuClassement + ", " + (this.eActuel instanceof MenuClassement));
        System.out
                .println("isMenuLancement : " + this.isMenuLancement + ", " + (this.eActuel instanceof MenuLancement));
        System.out.println("isRunningGame : " + this.isRunningGame + ", " + (this.eActuel instanceof Game));
        System.out.println("isMenuFin : " + this.isMenuFin + ", " + (this.eActuel instanceof MenuFin));
        System.out.println();
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

    public String getSkin() {
        return this.skin;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public boolean isQuitte() {
        return isQuitte;
    }

    public void setQuitte(boolean isQuitte) {
        this.isQuitte = isQuitte;
    }

    public boolean isRunningGame() {
        return isRunningGame;
    }

    public void setRunningGame(boolean isRunningGame) {
        this.isRunningGame = isRunningGame;
    }

    public boolean isMenuDemarrer() {
        return isMenuDemarrer;
    }

    public void setMenuDemarrer(boolean isMenuDemarrer) {
        this.isMenuDemarrer = isMenuDemarrer;
    }

    public boolean isMenuLancement() {
        return isMenuLancement;
    }

    public void setMenuLancement(boolean isMenuLancement) {
        this.isMenuLancement = isMenuLancement;
    }

    public boolean isMenuFin() {
        return isMenuFin;
    }

    public void setMenuFin(boolean isMenuFin) {
        this.isMenuFin = isMenuFin;
    }

    public boolean isMenuClassement() {
        return isMenuClassement;
    }

    public void setMenuClassement(boolean isMenuClassement) {
        this.isMenuClassement = isMenuClassement;
    }

    public boolean isMenuPause() {
        return isMenuPause;
    }

    public void setMenuPause(boolean isMenuPause) {
        this.isMenuPause = isMenuPause;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public JFrame getMenuPause() {
        return menuPause;
    }

    public void setMenuPause(JFrame menuPause) {
        this.menuPause = menuPause;
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
}