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
        this.vue.setView(new BufferedImage(this.vue.getWidth(), this.vue.getHeight(), BufferedImage.TYPE_INT_RGB));
        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                this.vue.setBackgroundView(ImageIO.read(new File(this.vue.getChemin() + "/background/background.png")));
                this.vue.setFlecheView(ImageIO.read(new File(this.vue.getChemin() + "/icon/iconfleche.png")));

            } catch (Exception e) {
                this.vue.setBackgroundView(
                        ImageIO.read(new File(this.vue.getWinchemin() + "/background/background.png")));
                this.vue.setFlecheView(ImageIO.read(new File(this.vue.getWinchemin() + "/icon/iconfleche.png")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // On initialise les boutons car ils ne changeront jamais.
        this.vue.setButtonJouerSolo(createImageOfMot("Jouer en solo"));
        this.vue.setButton2joueur(createImageOfMot("Jouer a 2"));
        this.vue.setButtonMultiJoueur(createImageOfMot("Mode multijoueurs"));
        this.vue.setButtonLb(createImageOfMot("Classement"));
        this.vue.setButtonQuitter(createImageOfMot("Quitter"));
        this.vue.setButtonRetourMenu(createImageOfMot("Revenir au menu"));
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
    }

    // Update les images & autres variables.
    public void update() {
        this.vue.setWfleche(30);
        this.vue.setHfleche(30);
        // La fleche se place toujours ici en x
        this.vue.setXfleche((7 * this.vue.getWidth() / 100) - this.vue.getWfleche());
        // Son placement en y dépend de ce qu'elle pointe
        int a = this.vue.getYfleche();
        this.vue.setYfleche((10 * vue.getHeight() / 100) + this.vue.getFleche() * this.vue.getSautLigne());
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();
        // Affichage terrain
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        // Affichage des boutons
        int x = (9 * this.vue.getWidth() / 100), y = (10 * this.vue.getHeight() / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        afficheMot(g2, this.vue.getButtonJouerSolo(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButton2joueur(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonMultiJoueur(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonLb(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonQuitter(), x, y, w, h, ecart, espacement);

        // Affichage de la fleche
        g2.drawImage(this.vue.getFlecheView(), this.vue.getXfleche(), this.vue.getYfleche(), this.vue.getWfleche(),
                this.vue.getHfleche(), null);

        // Affichage final
        g.drawImage(this.vue.getView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner cet état en boucle.
    public void running() {
        this.vue.setSautLigne(50);
        this.vue.setFleche(0); // On pointe le premier bouton
        while (Vue.isMenuDemarrer) {
            this.update();
            this.affiche(this.vue.getGraphics());
        }
        System.out.println("on est sortie du while de running.MDEMARRER");
    }

    // Gère les boutons.
    @Override
    public void keyControlPressed(KeyEvent e) {
        System.out.println("Menu DEMARRER - Key Pressed : " + this.vue.getFleche());

        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
            if (this.vue.getFleche() == 0) { // La flèche pointe sur le bouton "Jouer Solo" :
                this.vue.setNbJoueur(1); // On initialise le nombre de joueurs.
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER
                Vue.isMenuLancement = true;
                this.vue.setFleche(-1); // Pour éviter une erreur d'initialisation.
            }
            if (this.vue.getFleche() == 1) { // La flèche pointe sur le bouton "Jouer à 2" :
                this.vue.setNbJoueur(2); // On initialise le nombre de joueurs.
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER
                Vue.isMenuLancement = true;
                this.vue.setFleche(-1); // Pour éviter une erreur d'initialisation.
            }

            if (this.vue.getFleche() == 2) { // La flèche pointe sur le bouton "Mode multijoueur" :
                this.vue.setNbJoueur(2); // On initialise le nombre de joueurs.
                this.vue.setMultijoueur(true);
                int option = JOptionPane.showConfirmDialog(this.vue, "Voulez-vous host la partie ?",
                        "Paramètrage multijoueur",
                        JOptionPane.YES_NO_OPTION);
                System.out.println(option);
                if (option == 0) {
                    this.vue.setHost(true);
                    try {
                        this.vue.setServeur(new Serveur());
                        this.vue.getServeur().run();
                    } catch (IOException io) {
                        JOptionPane.showMessageDialog(null, "Aucun joueur n'a essayé de se connecter !", "Erreur !",
                                JOptionPane.ERROR_MESSAGE); // A implementer sur l'interface
                        System.exit(-1);
                    }
                } else {
                    this.vue.setHost(false);
                    this.vue.setJconnect(new JoueurConnecte());
                    this.vue.getJconnect().connecter();
                }
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER
                Vue.isMenuLancement = true;
            }

            if (this.vue.getFleche() == 3) { // La flèche pointe sur le bouton "Classement" :
                Vue.isMenuClassement = true;
                Vue.isMenuDemarrer = false;
            }
            if (this.vue.getFleche() == 4) { // La flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte l'application.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }

        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP)
            this.vue.setFleche((this.vue.getFleche() == 0) ? 4 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            this.vue.setFleche((this.vue.getFleche() == 4) ? 0 : this.vue.getFleche() + 1);

        System.out.println("MenuDemarrer.KeyControl : fleche = " + this.vue.getFleche());
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
        System.out.println("Menu DEMARRER - Key Released");
    }
}