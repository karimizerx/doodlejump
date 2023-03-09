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
public class MenuDemarrer extends Etat { // C'est donc un Etat.

    public MenuDemarrer(Vue vue) {
        super(vue);
    }

    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
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
        // On initialise les boutons car ils ne changeront jamais.
        this.buttonJouerSolo = createImageOfMot("Jouer en solo");
        this.button2joueur = createImageOfMot("Jouer a 2");
        this.buttonMultiJoueur = createImageOfMot("Mode multijoueurs");
        this.buttonLb = createImageOfMot("Classement");
        this.buttonQuitter = createImageOfMot("Quitter");
        this.buttonRetourMenu = createImageOfMot("Revenir au menu");
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
    }

    // Update les images & autres variables.
    public void update() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x
        // Son placement en y dépend de ce qu'elle pointe
        this.yfleche = (10 * height / 100) + fleche * sautLigne;
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
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
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner cet état en boucle.
    public void running(Graphics g) {
        this.sautLigne = 50;
        this.fleche = 0; // On pointe le premier bouton
        while (Vue.isMenuDemarrer) {
            this.update();
            this.affiche(g);
        }
    }

    // Gère les boutons.
    @Override
    public void keyControlPressed(KeyEvent e) {
        System.out.println("Menu DEMARRER - Key Pressed");

        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
            if (this.fleche == 0) { // La flèche pointe sur le bouton "Jouer Solo" :
                this.nbJoueur = 1; // On initialise le nombre de joueurs.
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER
                Vue.isMenuLancement = true;
            }
            if (this.fleche == 1) { // La flèche pointe sur le bouton "Jouer à 2" :
                this.nbJoueur = 2; // On initialise le nombre de joueurs.
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER
                Vue.isMenuLancement = true;
            }

            if (this.fleche == 2) { // La flèche pointe sur le bouton "Mode multijoueur" :
                this.nbJoueur = 2; // On initialise le nombre de joueurs.
                this.multijoueur = true;
                int option = JOptionPane.showConfirmDialog(this.vue, "Voulez-vous host la partie ?",
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
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER
                Vue.isRunningGame = true;
            }

            if (this.fleche == 3) { // La flèche pointe sur le bouton "Classement" :
                Vue.isMenuClassement = true;
                Vue.isMenuDemarrer = false;
            }
            if (this.fleche == 4) { // La flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte l'application.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }

        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP)
            this.fleche = (this.fleche == 0) ? 4 : this.fleche - 1;

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            this.fleche = (this.fleche == 4) ? 0 : this.fleche + 1;

    }

    @Override
    public void keyControlReleased(KeyEvent e) {
        System.out.println("Menu DEMARRER - Key Released");
    }
}