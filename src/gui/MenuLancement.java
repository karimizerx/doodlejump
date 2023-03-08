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
public class MenuLancement extends Etat { // C'est donc un Etat.

    BufferedImage view;

    public MenuLancement(Vue vue, BufferedImage view) {
        super(vue);
        this.view = view;
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
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
