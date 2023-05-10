package gui;

// Import d'autres dossiers
import gameobjects.*;
import leaderboard.*;

// Import de packages java
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;

// Représente l'état où le jeu est au niveau du "MENU CLASSEMENT".
public class MenuClassement extends Etat {

    public MenuClassement(Vue vue) {
        super(vue);
    }

    /// Méthodes de la classe :
    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
        // Double try_catch pour gérer la différence entre windows & linux.
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

        // On initialise le titre du menu à afficher.
        this.vue.setTitreStatut(createImageOfMot("Classement "));
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
        // Chaque liste de cette liste représente une ligne dans le classement.
        this.vue.setLbView(new ArrayList<ArrayList<BufferedImage>>());
    }

    // Met à jour les données du CLASSEMENT.
    public void updateClassement() throws IOException {
        // on prend en compte les pieces de chaque Joueur
        for (int i = 0; i < this.vue.getTerrain().getListeJoueurs().size(); ++i) {
            Joueur j = this.vue.getTerrain().getListeJoueurs().get(i);
            j.setScore(j.getScore()+j.getMonnaie()*10000);
        }
        if (this.vue.getNbJoueur() == 1) { // On update le classement que s'il n'y a qu'un joueur.
            Joueur j = this.vue.getTerrain().getListeJoueurs().get(0);
            String score = String.valueOf(j.getScore());

            // Mise à jour dans le classement (Global).
            Classement c = new Classement();
            c.ajoutClassement(j.getId(), j.getNom(), score);

            // Mise à jour dans l'historique (Local).
            History h = new History();
            h.ajoutClassement(j.getId(), j.getNom(), score);
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
        this.vue.setYfleche((12 * this.vue.getHeight() / 100)
                + ((4 + this.vue.getLbView().size() / 3) + this.vue.getFleche()) * this.vue.getSautLigne());
    }

    // Affiche les images.
    @Override
    public void affiche(Graphics g) { // Prend en argument le contexte graphique de la vue.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();

        // Affichage du background.
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        // Affichage du titre.
        int x = (9 * this.vue.getWidth() / 100), y = (12 * this.vue.getHeight() / 100);
        int w = this.vue.getWidth() * 30 / 640, h = this.vue.getHeight() * 30 / 1026,
                espacement = this.vue.getWidth() * 15 / 640, ecart = this.vue.getWidth() * 20 / 640;
        x = afficheMot(g2, this.vue.getTitreStatut(), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);

        // Affichage du classement.
        y += this.vue.getSautLigne() * 2;
        x = (11 * this.vue.getWidth() / 100);
        this.vue.setBackgroundClView(this.vue.getBackgroundClView1());
        for (int z = 0; z < this.vue.getLbView().size(); z = z + 3) {
            // On commence par afficher le background de la ligne.
            this.vue.setBackgroundClView(
                    (z % 2 == 0) ? this.vue.getBackgroundClView1() : this.vue.getBackgroundClView2());
            g2.drawImage(this.vue.getBackgroundClView(), x * 85 / 100, y - h / 6, this.vue.getWidth(), h * 3 / 2, null);
            x = afficheMot(g2, this.vue.getLbView().get(z), x, y, w, h, ecart, espacement);
            x += espacement / 2;
            affichePoint(g2, x, y + h - this.vue.getHeight() * 7 / 1026, this.vue.getWidth() * 7 / 640,
                    this.vue.getHeight() * 7 / 1026);
            x += espacement * 2;
            x = afficheMot(g2, this.vue.getLbView().get(z + 1), x, y, w, h, ecart, espacement);
            x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
            x += espacement * 2;
            x = afficheMot(g2, this.vue.getLbView().get(z + 2), x, y, w, h, ecart, espacement);
            x = (11 * this.vue.getWidth() / 100);
            y += this.vue.getSautLigne();
        }

        // Affichage des boutons.
        y += this.vue.getSautLigne() * 2;
        x = (9 * this.vue.getWidth() / 100);
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

        // On récupère les données du classement que l'on va afficher.
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

        while (Vue.isMenuClassement) { // Tant que l'on est dans le menu CLASSEMENT :
            this.update(); // On update.
            this.affiche(this.vue.getGraphics()); // On affiche.
        }
    }

    // Gestion des boutons.
    @Override
    public void keyControlPressed(KeyEvent e) { // KeyEvent e de la vue.
        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :

            if (this.vue.getFleche() == 0) { // Si la flèche pointe sur le bouton "Retour au menu DEMARRER" :
                Vue.isMenuClassement = false; // On quitte le menu CLASSEMENT.
                Vue.isMenuDemarrer = true; // On passe dans le menu DEMARRER.
            }

            if (this.vue.getFleche() == 1) { // Si la flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte le jeu.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }
        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP) // Si on monte avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 0) ? 1 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN) // Si on descend avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 1) ? 0 : this.vue.getFleche() + 1);
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        int y = (12 * this.vue.getHeight() / 100) + (this.vue.getLbView().size() + 2) * this.vue.getSautLigne();
        int h = this.vue.getHeight() * 30 / 1026;

        if (e.getY() > y && e.getY() < y + h) {
            Vue.isMenuClassement = false; // On quitte le menu CLASSEMENT.
            Vue.isMenuDemarrer = true; // On passe dans le menu DEMARRER.
            return;
        }
        y = +this.vue.getSautLigne();
        if (e.getY() > y && e.getY() < y + h) {
            System.out.println("À la prochaine !");
            Vue.isQuitte = true; // On quitte le jeu.
            System.exit(0); // On ferme toutes les fenêtres & le programme.
            return;
        }
    }
}