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
public class MenuFin extends Etat { // C'est donc un Etat.

    BufferedImage view;

    public MenuFin(Vue vue, BufferedImage view) {
        super(vue);
        this.view = view;
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
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

    // Update les images & autres variables.
    public void update() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x
        // Son placement en y dépend de ce qu'elle pointe
        this.yfleche = (12 * height / 100) + sautLigne * (6 + fleche);
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
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
