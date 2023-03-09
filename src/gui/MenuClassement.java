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
public class MenuClassement extends Etat { // C'est donc un Etat.

    private BufferedImage backgroundClView, backgroundClView1, backgroundClView2;
    private ArrayList<ArrayList<BufferedImage>> lbView;

    public MenuClassement(Vue vue) {
        super(vue);
    }

    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
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

    // Initialise les images & les autres variables.
    @Override
    public void init() {
    }

    // Met à jour les données du CLASSEMENT
    public void updateClassement() throws IOException {
        if (this.nbJoueur == 1) { // On update le classement que s'il n'y a qu'un joueur
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

    // Update les images & autres variables.
    public void update() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x
        // Son placement en y dépend de ce qu'elle pointe
        this.yfleche = (12 * height / 100) + ((4 + lbView.size() / 3) + fleche) * sautLigne;
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
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

        /// Affichage final
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner cet état en boucle.
    public void running(Graphics g) {
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

        while (Vue.isMenuClassement) {
            this.update();
            this.affiche(g);
        }
    }

    // Gère les boutons.
    @Override
    public void keyControlPressed(KeyEvent e) {
        System.out.println("Menu CLASSEMENT - Key Pressed");
        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
            if (this.fleche == 0) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                Vue.isMenuClassement = false; // On quitte le menu FIN.
                Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
            }
            if (this.fleche == 1) { // La flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte l'application.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }
        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP)
            this.fleche = (this.fleche == 0) ? 1 : this.fleche - 1;

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            this.fleche = (this.fleche == 1) ? 0 : this.fleche + 1;
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
        System.out.println("Menu CLASSEMENT - Key Released");
    }
}