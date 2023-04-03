package gui;

// Import d'autres dossiers
import gameobjects.*;
import leaderboard.*;

// Import de packages java
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

// Représente l'état où le jeu est au niveau du "MENU FIN".
public class MenuPause extends Etat {

    public MenuPause(Vue vue) {
        super(vue);
    }

    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
        // Représente l'affiche pour le score à cette partie ou le score du 1er joueur.
        this.vue.setScoreFinalView(new ArrayList<ArrayList<BufferedImage>>());
        // Représente l'affiche pour les meilleurs scores ou le score du 2e joueur.
        this.vue.setHightScoreView(new ArrayList<ArrayList<BufferedImage>>());

        this.vue.setTitreStatut(createImageOfMot("Pause"));
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
        this.vue.setYfleche((12 * this.vue.getHeight() / 100) + this.vue.getSautLigne() * (6 + this.vue.getFleche()));
    }

    // Affiche les images.
    @Override
    public void affiche(Graphics g) { // Prend en argument le contexte graphique de la vue.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();

        // Affichage background.
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        // Affichage du titre.
        int x = (9 * this.vue.getWidth() / 100), y = (12 * this.vue.getHeight() / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        afficheMot(g2, this.vue.getTitreStatut(), x, y, w, h, ecart, espacement);

        // Affichage des boutons.
        y += this.vue.getSautLigne() * 2;
        x = (9 * this.vue.getWidth() / 100);
        afficheMot(g2, this.vue.getButtonReprendre(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonRejouer(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonRetourMenu(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne();
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

        while (Vue.isMenuFin) { // Tant que l'on est au niveau du menu FIN :
            this.update(); // On update.
            this.affiche(this.vue.getGraphics()); // On affiche.
        }
    }

    // Gestion des boutons.
    @Override
    public void keyControlPressed(KeyEvent e) { // KeyEvent de la vue.
        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
            if (this.vue.getFleche() == 0) { // Si la flèche pointe sur le bouton "Reprendre" :
                this.vue.geteGame().pause();
                Vue.isMenuPause = false; // On sort du menu Pause.
            }
            if (this.vue.getFleche() == 1) { // Si la flèche pointe sur le bouton "Rejouer" :
                Vue.isMenuPause = false;    
                this.vue.geteGame().createPartie(); // On crée une partie.
                Vue.isRunningGame = true;
            }
            if (this.vue.getFleche() == 2) { // Si la flèche pointe sur le bouton "Retour Menu" :
                Vue.isMenuPause = false;
                Vue.isMenuDemarrer = true;
            }
            if (this.vue.getFleche() == 3) { // Si la flèche pointe sur le bouton "Quitter" :
                Vue.isQuitte = true; // On quitte l'application.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }

        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP) // Si on monte avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 0) ? 2 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN) // Si on descend avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 2) ? 0  : this.vue.getFleche() + 1);

    }

    @Override
    public void keyControlReleased(KeyEvent e) {
    }
}


