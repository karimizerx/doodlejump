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
public class MenuFin extends Etat {

    public MenuFin(Vue vue) {
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

        if (this.vue.getNbJoueur() == 1) { // S'il n'y a qu'1 joueur :
            this.vue.setTitreStatut(createImageOfMot("Game Over"));
            // Ce qu'on va afficher pour le score en fin de partie :
            Joueur j = this.vue.getTerrain().getListeJoueurs().get(0);
            String s = String.valueOf(j.getScore());
            ArrayList<BufferedImage> phrase = createImageOfMot("Votre score ");
            ArrayList<BufferedImage> score = createImageOfMot(s);
            this.vue.getScoreFinalView().add(phrase);
            this.vue.getScoreFinalView().add(score);

            // Ce qu'on va afficher pour le meilleur score en fin de partie :
            String hs = String.valueOf((new Classement()).getMaxScoreOfId(j.getId()));
            ArrayList<BufferedImage> phrase1 = createImageOfMot("Votre meilleur score ");
            ArrayList<BufferedImage> hscore = createImageOfMot(hs);
            String hs2 = String.valueOf((new Classement()).getMaxScoreGlobal());
            ArrayList<BufferedImage> phrase2 = createImageOfMot("Meilleur score global ");
            ArrayList<BufferedImage> hscore2 = createImageOfMot(hs2);
            this.vue.getHightScoreView().add(phrase1);
            this.vue.getHightScoreView().add(hscore);
            this.vue.getHightScoreView().add(phrase2);
            this.vue.getHightScoreView().add(hscore2);
        }

        if (this.vue.getNbJoueur() == 2) { // S'il y a 2 joueurs :
            this.vue.setTitreStatut(createImageOfMot("Fin de la course"));
            // On adapte cet init() de sorte à ce que la fonction d'affichage ne change pas.
            Joueur j0 = this.vue.getTerrain().getListeJoueurs().get(0),
                    j1 = this.vue.getTerrain().getListeJoueurs().get(1);
            int sc0 = j0.getScore(), sc1 = j1.getScore();
            String s0 = String.valueOf(sc0), s1 = String.valueOf(sc1);
            ArrayList<BufferedImage> phrase = createImageOfMot("Score de " + j0.getNom() + " ");
            ArrayList<BufferedImage> score = createImageOfMot(s0);
            this.vue.getScoreFinalView().add(phrase);
            this.vue.getScoreFinalView().add(score);

            ArrayList<BufferedImage> phrase1 = createImageOfMot("Score de " + j1.getNom() + " ");
            ArrayList<BufferedImage> hscore = createImageOfMot(s1);
            String winner = (sc0 == sc1) ? "Aucun" : (sc0 > sc1) ? j0.getNom() : j1.getNom();
            ArrayList<BufferedImage> phrase2 = createImageOfMot("Vainqueur ");
            ArrayList<BufferedImage> hscore2 = createImageOfMot(winner);
            this.vue.getHightScoreView().add(phrase1);
            this.vue.getHightScoreView().add(hscore);
            this.vue.getHightScoreView().add(phrase2);
            this.vue.getHightScoreView().add(hscore2);
        }
    }

    // Update les images & autres variables.
    @Override
    public void update() {
        // Dimensions de la fleche.
        this.vue.setWfleche(this.vue.getWidth() * 30 / 640);
        this.vue.setHfleche(this.vue.getHeight() * 30 / 1026);

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
        int w = this.vue.getWidth() * 30 / 640, h = this.vue.getHeight() * 30 / 1026,
                espacement = this.vue.getWidth() * 15 / 640, ecart = this.vue.getWidth() * 20 / 640;
        afficheMot(g2, this.vue.getTitreStatut(), x, y, w, h, ecart, espacement);

        // Affichage des scores (entre () l'adaptation pour les parties à 2 joueurs):
        // Affichage du score à cette partie (score du joueur 1).
        y += this.vue.getSautLigne() * 2;
        x = afficheMot(g2, this.vue.getScoreFinalView().get(0), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
        x += espacement;
        x = afficheMot(g2, this.vue.getScoreFinalView().get(1), x, y, w, h, ecart, espacement);

        // Affichage du meilleur score local (score du joueur 2).
        y += this.vue.getSautLigne();
        x = (9 * this.vue.getWidth() / 100);
        x = afficheMot(g2, this.vue.getHightScoreView().get(0), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
        x += espacement;
        x = afficheMot(g2, this.vue.getHightScoreView().get(1), x, y, w, h, ecart, espacement);

        // Affichage du meilleur score global (vainqueur).
        y += this.vue.getSautLigne();
        x = (9 * this.vue.getWidth() / 100);
        x = afficheMot(g2, this.vue.getHightScoreView().get(2), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
        x += espacement;
        x = afficheMot(g2, this.vue.getHightScoreView().get(3), x, y, w, h, ecart, espacement);

        // Affichage des boutons.
        y += this.vue.getSautLigne() * 2;
        x = (9 * this.vue.getWidth() / 100);
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
        removelistners();
        this.vue.addMouseListener(this);
        // Initialisation des valeurs initiales des variables avant lancement.
        this.vue.setSautLigne(this.vue.getHeight() * 50 / 1026); // Distance entre 2 lignes.
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
            if (this.vue.getFleche() == 0) { // Si la flèche pointe sur le bouton "Retour au menu DEMARRER" :
                Vue.isMenuFin = false;
                this.vue.geteGame().createPartie(); // On crée une partie.
                Vue.isRunningGame = true;
            }
            if (this.vue.getFleche() == 1) { // Si la flèche pointe sur le bouton "Quitter" :
                Vue.isMenuFin = false; // On quitte le menu FIN.
                Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
            }
            if (this.vue.getFleche() == 2) { // Si la flèche pointe sur le bouton "Quitter" :

                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte l'application.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }

        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP) // Si on monte avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 0) ? 2 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN) // Si on descend avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 2) ? 0 : this.vue.getFleche() + 1);

    }

    @Override
    public void keyControlReleased(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        int y = (12 * this.vue.getHeight() / 100) + 7 * this.vue.getSautLigne();
        int h = this.vue.getHeight() * 30 / 1026;

        if (e.getY() > y && e.getY() < y + h) { // Si la flèche pointe sur le bouton "Retour au menu DEMARRER" :
            Vue.isMenuFin = false; // On quitte le menu FIN.
            Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
            return;
        }
        y += this.vue.getSautLigne();
        if (e.getY() > y && e.getY() < y + h) { // Si la flèche pointe sur le bouton "Quitter" :
            System.out.println("À la prochaine !");
            Vue.isQuitte = true; // On quitte l'application.
            System.exit(0); // On ferme toutes les fenêtres & le programme.
        }
        y += this.vue.getSautLigne();
        if (e.getY() > y && e.getY() < y + h) { // Si la flèche pointe sur le bouton "Quitter" :
            Vue.isMenuFin = false;
            this.vue.geteGame().createPartie(); // On crée une partie.
            Vue.isRunningGame = true;
            return;
        }
    }
}
