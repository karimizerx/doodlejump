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
public class MenuDemarrer extends Etat {

    public MenuDemarrer(Vue vue) {
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
        this.vue.setButtonJouerSolo(createImageOfMot("Jouer en solo"));
        this.vue.setButton2joueur(createImageOfMot("Jouer a 2"));
        this.vue.setButtonMultiJoueur(createImageOfMot("Mode multijoueurs"));
        this.vue.setButtonLb(createImageOfMot("Classement"));
        this.vue.setButtonQuitter(createImageOfMot("Quitter"));
        this.vue.setButtonRetourMenu(createImageOfMot("Revenir au menu"));
        this.vue.setButtonRejouer(createImageOfMot("Rejouer"));
        this.vue.setButtonSetting(createImageOfMot("Parametre"));
        this.vue.setButtonNiveau(createImageOfMot("Niveau "));
        this.vue.setButtonSkin(createImageOfMot("Skin "));
        this.vue.setButtonInertie(createImageOfMot("Inertie "));
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
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
        int w = this.vue.getWidth() * 30 / 640, h = this.vue.getHeight() * 30 / 1026,
                espacement = this.vue.getWidth() * 15 / 640, ecart = this.vue.getWidth() * 20 / 640;
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
        afficheMot(g2, this.vue.getButtonSetting(), x, y, w, h, ecart, espacement);
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
        removelistners();
        this.vue.addMouseListener(this);
        // Initialisation des valeurs initiales des variables avant lancement.
        this.vue.setSautLigne(this.vue.getHeight() * 50 / 1026); // Distance entre 2 lignes.
        this.vue.setFleche(0); // On pointe le premier bouton.

        while (Vue.isMenuDemarrer) { // Tant que l'on est dans le menu DEMARRER :
            this.update(); // On update.
            this.affiche(this.vue.getGraphics()); // On affiche.
        }
    }

    // Gestion des boutons.
    @Override
    public void keyControlPressed(KeyEvent e) { // KeyEvent e de la vue.
        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :

            if (this.vue.getFleche() <= 1) { // Si la flèche pointe sur le bouton "Jouer Solo/à 2" :
                this.vue.setNbJoueur(this.vue.getFleche() + 1); // On initialise le nombre de joueurs.
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER.
                Vue.isMenuLancement = true; // On passe au menu LANCEMENT.
                this.vue.setFleche(-1); // Pour éviter une erreur avec le KeyControl de LANCEMENT (voir doc).
            }

            if (this.vue.getFleche() == 2) { // Si la flèche pointe sur le bouton "Mode multijoueur" :
                this.vue.setNom1("Player 1");
                this.vue.setNom2("Player 2");
                this.vue.setNbJoueur(2); // On initialise le nombre de joueurs.
                this.vue.setMultijoueur(true); // On indique qu'on est en mode multijoueurs.
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER.
                Vue.isConnecting=true;


                // Boîte de dialogue pour savoir si le joueur accueille (host) la partie.
                int option = JOptionPane.showConfirmDialog(this.vue, "Voulez-vous accueillir la partie ?",
                        "Paramètrage multijoueur...", JOptionPane.YES_NO_OPTION);
                if (option == 0) { // Si oui :
                    this.vue.setHost(true); // On indique que le joueur est host.
                    try { // On tente la connexion.
                        this.vue.setServeur(new Serveur());
                        this.vue.getServeur().connect();
                        this.vue.getTerrain().setHost(this.vue.getServeur());
                    } catch (IOException io) {
                        JOptionPane.showMessageDialog(null, "Aucun joueur n'a essayé de se connecter !", "Erreur !",
                                JOptionPane.ERROR_MESSAGE); // A implementer sur l'interface
                        System.exit(-1);
                    }
                } else { // Si le joueur ne host pas la partie :
                    this.vue.setHost(false); // On indique qu'il n'est pas host.
                    this.vue.setJconnect(new JoueurConnecte()); // ???
                    this.vue.getJconnect().connecter();
                    this.vue.getTerrain().setClient(this.vue.getJconnect());
                    System.out.println(this.vue.getTerrain().client==null);
                }
                Vue.isConnecting=false;
                Vue.isRunningGame=true;

                // Vue.isMenuLancement = true; // On passe au menu LANCEMENT.
                // this.vue.setFleche(-1); // Pour éviter une erreur avec le KeyControl de LANCEMENT (voir doc).


            }

            if (this.vue.getFleche() == 3) { // Si la flèche pointe sur le bouton "Classement" :
                Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER.
                Vue.isMenuClassement = true; // On passe au menu CLASSEMENT.
            }
            if (this.vue.getFleche() == 4) {
                Vue.isMenuDemarrer = false;
                Vue.isSetting = true;
                this.vue.setFleche(-1);
            }
            if (this.vue.getFleche() == 5) { // Si la flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte le jeu.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }

        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP) // Si on monte avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 0) ? 5 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN) // Si on descend avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 5) ? 0 : this.vue.getFleche() + 1);
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = (9 * this.vue.getWidth() / 100), y = (10 * this.vue.getHeight() / 100);
        int h = this.vue.getHeight() * 30 / 1026;

        if (e.getY() > y && e.getY() < y + h) {
            this.vue.setNbJoueur(1); // On initialise le nombre de joueurs.
            Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER.
            Vue.isMenuLancement = true;
            return;
        }
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();

        if (e.getY() > y && e.getY() < y + h) {
            this.vue.setNbJoueur(2); // On initialise le nombre de joueurs.
            Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER.
            Vue.isMenuLancement = true;
            return;
        }
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();

        if (e.getY() > y && e.getY() < y + h) { // Si la flèche pointe sur le bouton "Mode multijoueur" :
        this.vue.setNom1("Player 1");
        this.vue.setNom2("Player 2");
        this.vue.setNbJoueur(2); // On initialise le nombre de joueurs.
        this.vue.setMultijoueur(true); // On indique qu'on est en mode multijoueurs.
        Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER.
        Vue.isConnecting=true;


        // Boîte de dialogue pour savoir si le joueur accueille (host) la partie.
        int option = JOptionPane.showConfirmDialog(this.vue, "Voulez-vous accueillir la partie ?",
                "Paramètrage multijoueur...", JOptionPane.YES_NO_OPTION);
        if (option == 0) { // Si oui :
            this.vue.setHost(true); // On indique que le joueur est host.
            try { // On tente la connexion.
                this.vue.setServeur(new Serveur());
                this.vue.getServeur().connect();
                this.vue.getTerrain().setHost(this.vue.getServeur());
            } catch (IOException io) {
                JOptionPane.showMessageDialog(null, "Aucun joueur n'a essayé de se connecter !", "Erreur !",
                        JOptionPane.ERROR_MESSAGE); // A implementer sur l'interface
                System.exit(-1);
            }
        } else { // Si le joueur ne host pas la partie :
            this.vue.setHost(false); // On indique qu'il n'est pas host.
            this.vue.setJconnect(new JoueurConnecte()); // ???
            this.vue.getJconnect().connecter();
            this.vue.getTerrain().setClient(this.vue.getJconnect());
            System.out.println(this.vue.getTerrain().client==null);
        }
        Vue.isConnecting=false;
        Vue.isRunningGame=true;

        // Vue.isMenuLancement = true; // On passe au menu LANCEMENT.
        // this.vue.setFleche(-1); // Pour éviter une erreur avec le KeyControl de LANCEMENT (voir doc).


    }
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();

        if (e.getY() > y && e.getY() < y + h) {
            Vue.isMenuDemarrer = false; // On quitte le menu DEMARRER.
            Vue.isMenuClassement = true; // On passe au menu CLASSEMENT.

            return;
        }
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();

        if (e.getY() > y && e.getY() < y + h) {
            System.out.println("*");
            Vue.isMenuDemarrer = false;
            Vue.isSetting = true;
            return;
        }
        x = (9 * this.vue.getWidth() / 100);
        y = y + this.vue.getSautLigne();

        if (e.getY() > y && e.getY() < y + h) {
            System.out.println("À la prochaine !");
            Vue.isQuitte = true; // On quitte le jeu.
            System.exit(0); // On ferme toutes les fenêtres & le programme.
            return;
        }
    }
}