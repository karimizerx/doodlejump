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

    public MenuClassement(Vue vue) {
        super(vue);
    }

    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
        try {
            try {
                this.vue.setBackgroundClView1(
                        ImageIO.read(new File(this.vue.getChemin() + "/background/backgroundClassement1.png")));
                this.vue.setBackgroundClView2(
                        ImageIO.read(new File(this.vue.getChemin() + "/background/backgroundClassement2.png")));
            } catch (Exception e) {
                this.vue.setBackgroundClView1(
                        ImageIO.read(new File(this.vue.getWinchemin() + "/background/backgroundClassement1.png")));
                this.vue.setBackgroundClView2(
                        ImageIO.read(new File(this.vue.getWinchemin() + "/background/backgroundClassement2.png")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
        this.vue.setLbView(new ArrayList<ArrayList<BufferedImage>>());
        this.vue.setTitreStatut(createImageOfMot("Classement "));
    }

    // Met à jour les données du CLASSEMENT
    public void updateClassement() throws IOException {
        if (this.vue.getNbJoueur() == 1) { // On update le classement que s'il n'y a qu'un joueur
            Joueur j = this.vue.getTerrain().getListeJoueurs().get(0);
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
        this.vue.setWfleche(30);
        this.vue.setHfleche(30);
        this.vue.setXfleche((7 * this.vue.getWidth() / 100) - this.vue.getWfleche()); // La fleche se place toujours ici
        // Son placement en y dépend de ce qu'elle pointe
        this.vue.setYfleche((12 * this.vue.getHeight() / 100)
                + ((4 + this.vue.getLbView().size() / 3) + this.vue.getFleche()) * this.vue.getSautLigne());
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();
        // Affichage terrain
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        /// Affichage du message final :
        int x = (9 * this.vue.getWidth() / 100), y = (12 * this.vue.getHeight() / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        x = afficheMot(g2, this.vue.getTitreStatut(), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, 7, 7);

        // Affichage du classement :
        y += this.vue.getSautLigne() * 2;
        x = (11 * this.vue.getWidth() / 100);
        this.vue.setBackgroundClView(this.vue.getBackgroundClView1());
        for (int z = 0; z < this.vue.getLbView().size(); z = z + 3) {
            this.vue.setBackgroundClView(
                    (z % 2 == 0) ? this.vue.getBackgroundClView1() : this.vue.getBackgroundClView2());
            g2.drawImage(this.vue.getBackgroundClView(), x * 85 / 100, y - h / 6, this.vue.getWidth(), h * 3 / 2, null);
            x = afficheMot(g2, this.vue.getLbView().get(z), x, y, w, h, ecart, espacement);
            x += espacement / 2;
            affichePoint(g2, x, y + h - 7, 7, 7);
            x += espacement * 2;
            x = afficheMot(g2, this.vue.getLbView().get(z + 1), x, y, w, h, ecart, espacement);
            x = afficheDoublepoint(g2, x, y, 7, 7);
            x += espacement * 2;
            x = afficheMot(g2, this.vue.getLbView().get(z + 2), x, y, w, h, ecart, espacement);
            x = (11 * this.vue.getWidth() / 100);
            y += this.vue.getSautLigne();
        }

        /// Affichage des boutons :
        y += this.vue.getSautLigne() * 2;
        x = (9 * this.vue.getWidth() / 100);
        afficheMot(g2, this.vue.getButtonRetourMenu(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonQuitter(), x, y, w, h, ecart, espacement);

        /// Affichage de la fleche
        g2.drawImage(this.vue.getFlecheView(), this.vue.getXfleche(), this.vue.getYfleche(), this.vue.getWfleche(),
                this.vue.getHfleche(), null);

        /// Affichage final
        g.drawImage(this.vue.getView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner cet état en boucle.
    public void running() {
        this.vue.setFleche(0);
        this.vue.setSautLigne(50);

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
            this.vue.getLbView().add(rank);
            this.vue.getLbView().add(nom);
            this.vue.getLbView().add(score);
        }

        while (Vue.isMenuClassement) {
            this.update();
            this.affiche(this.vue.getGraphics());
        }
    }

    // Gère les boutons.
    @Override
    public void keyControlPressed(KeyEvent e) {
        System.out.println("Menu CLASSEMENT - Key Pressed");
        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
            if (this.vue.getFleche() == 0) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                Vue.isMenuClassement = false; // On quitte le menu FIN.
                Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
            }
            if (this.vue.getFleche() == 1) { // La flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte l'application.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }
        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP)
            this.vue.setFleche((this.vue.getFleche() == 0) ? 1 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            this.vue.setFleche((this.vue.getFleche() == 1) ? 0 : this.vue.getFleche() + 1);
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
        System.out.println("Menu CLASSEMENT - Key Released");
    }
}
