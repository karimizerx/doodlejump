package gui;

// Import d'autres dossiers
import multiplayer.*;

// Import de packages java
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

// Représente l'état où le jeu est au niveau du "MENU DEMARRER".
public class MenuPause extends Etat {

    public MenuPause(Vue vue) {
        super(vue);
    }

    /// Méthodes de la classe :
    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
        // Double try_catch pour gérer la différence entre windows & linux.
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

        // view est l'image qui contiendra toutes les autres.
        this.vue.setView(new BufferedImage(this.vue.getWidth(), this.vue.getHeight(), BufferedImage.TYPE_INT_RGB));

        // On initialise les boutons car ils ne changeront jamais.
        this.vue.setButtonReprendre(createImageOfMot("Reprendre"));
        this.vue.setButtonQuitter(createImageOfMot("Quitter"));
        this.vue.setButtonRetourMenu(createImageOfMot("Revenir au menu"));
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
    }

    // Update les images & autres variables.
    @Override
    public void update() {
        // Dimensions de la fleche.
        this.vue.setWfleche(30);
        this.vue.setHfleche(30);

        // La fleche a toujours la même coordonnée x.
        this.vue.setXfleche((7 * this.vue.getWidth() / 100) - this.vue.getWfleche());

        // Le placement de la fleche en y dépend de ce qu'elle pointe.
        this.vue.setYfleche((10 * vue.getHeight() / 100) + this.vue.getFleche() * this.vue.getSautLigne());
    }

    // Affiche les images.
    @Override
    public void affiche(Graphics g) { // Prend en argument le contexte graphique de la vue.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();

        // Affichage du background.
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        // Affichage des boutons.
        int x = (9 * this.vue.getWidth() / 100), y = (10 * this.vue.getHeight() / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        afficheMot(g2, this.vue.getButtonReprendre(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonQuitter(), x, y, w, h, ecart, espacement);

        // Affichage de la fleche.
        g2.drawImage(this.vue.getFlecheView(), this.vue.getXfleche(), this.vue.getYfleche(), this.vue.getWfleche(),
                this.vue.getHfleche(), null);

        // Affichage final.
        g.drawImage(this.vue.getView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        g.dispose(); // On libère les ressources.
    }

    // Fait tourner cet état.
    @Override
    public void running() {
        // Initialisation des valeurs initiales des variables avant lancement.
        this.vue.setSautLigne(50); // Distance entre 2 lignes.
        this.vue.setFleche(0); // On pointe le premier bouton.

        while (Vue.isMenuPause) { // Tant que l'on est dans le menu DEMARRER :
            this.update(); // On update.
            this.affiche(this.vue.getGraphics()); // On affiche.
        }
    }

    // Gestion des boutons.
    @Override
    public void keyControlPressed(KeyEvent e) { // KeyEvent e de la vue.
        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :

            if (this.vue.getFleche() <= 1) { // Si la flèche pointe sur le bouton "Reprendre" :
                this.vue.setNbJoueur(this.vue.getFleche() + 1); // On initialise le nombre de joueurs.
                Vue.isMenuPause = false; // On quitte le menu DEMARRER.
                this.vue.setFleche(-1); // Pour éviter une erreur avec le KeyControl de LANCEMENT (voir doc).
            }

            if (this.vue.getFleche() == 2) { // Si la flèche pointe sur le bouton "Quitter" :
            Vue.isMenuPause = false;
            System.out.println("À la prochaine !");
            Vue.isQuitte = true; // On quitte le jeu.
            System.exit(0); // On ferme toutes les fenêtres & le programme.
            }

            if (this.vue.getFleche() == 3) { // Si la flèche pointe sur le bouton "Retour Menu" :
                Vue.isMenuDemarrer = true; // On quitte le menu DEMARRER.
                Vue.isMenuPause = false; // On passe au menu CLASSEMENT.
            }
        }
            
        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP) // Si on monte avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 0) ? 4 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN) // Si on descend avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 4) ? 0 : this.vue.getFleche() + 1);
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
    }
}