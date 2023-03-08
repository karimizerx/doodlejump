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

    BufferedImage view;

    public MenuDemarrer(Vue vue, BufferedImage view) {
        super(vue);
        this.view = view;
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
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
        while (isRunning) {
            this.update();
            this.affiche(g);
        }
    }
}