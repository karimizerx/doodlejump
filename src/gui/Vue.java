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
                    this.eActuel = this.eMenuDemarrer;
                }
                // Si on a cliqué sur le boutton "Classement" :
                if (!isMenuDemarrer && isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin) {
                    this.eMenuClassement.init();
                    this.eActuel = this.eMenuClassement;
                }
                // Si on a cliqué sur le boutton "Jouer solo" :
                if (!isMenuDemarrer && !isMenuClassement && isMenuLancement && !isRunningGame && !isMenuFin) {
                    this.eMenuLancement.init();
                    this.eActuel = this.eMenuLancement;
                }

                // Si on a lancé une GAME :
                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && isRunningGame && !isMenuFin) {
                    this.eGame.init(); // On initialise les images de la GAME.
                    // if (terrain.multiplayer) { // Si on est en mode multijoueur
                    // Thread t = new Thread(new ThreadMouvement(terrain)); // ???
                    // t.start();
                    // }
                    this.eGame.running(getGraphics()); // On lance la GAME (en ups constant).
                }
                // Si c'est la fin de la GAME (quelqu'un a perdu) :
                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin
                        && ((Game) this.eGame).isEndGame()) {
                    // On met à jour toutes les variables boolean.
                    isMenuDemarrer = false;
                    isMenuClassement = false;
                    isRunningGame = false;
                    isMenuFin = true;
                    // On met à jour le classement et l'historique.
                    ((MenuClassement) this.eMenuClassement).updateClassement();
                }
                // Si on a fini la GAME sans erreur :
                if (!isMenuDemarrer && !isMenuClassement && !isRunningGame && isMenuFin) {
                    this.eMenuFin.init(); // On initialise les images du menu FIN.
                    this.eMenuFin.running(getGraphics()); // On lance le menu FIN.
                }

                // On exécute les différentes actions.
                this.eActuel.running(getGraphics());// On lance le menu DEMARRER.
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
}