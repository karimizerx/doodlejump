package gui;

// Import d'autres dossiers
import leaderboard.*;

// Import de packages java
import java.awt.*;
import java.awt.event.*;

// Représente l'état où le jeu est au niveau du "MENU LANCEMENT".
public class MenuLancement extends Etat {

    public MenuLancement(Vue vue) {
        super(vue);
    }

    /// Méthodes de la classe :
    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
        this.vue.setMessageNom(createImageOfMot("Entrez un nom "));
        this.vue.setButtonJouer(createImageOfMot("Jouer"));
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
        // On initialise les noms des joueurs.
        History h = new History();
        if (this.vue.getNbJoueur() == 2) {
            this.vue.setNom1("MIZER 1");
            this.vue.setNom2("MIZER 2");
        } else // Si le joueur a déjà joué une partie, on prend le nom de la dernière partie.
            this.vue.setNom1((h.getLbData().size() > 1) ? h.getLbData().get(h.getLbData().size() - 1)[1] : "MIZER");

        // On intialise les images des noms des joueurs.
        this.vue.setNomJ1(createImageOfMot(this.vue.getNom1()));
        this.vue.setNomJ2(createImageOfMot(this.vue.getNom2()));
    }

    // Update les images & autres variables.
    @Override
    public void update() {
        // Dimensions de la fleche.
        this.vue.setWfleche(this.vue.getWidth() * 30 / 640);
        this.vue.setHfleche(this.vue.getHeight() * 30 / 1026);

        // La fleche a toujours la même coordonnée x.
        this.vue.setXfleche((7 * this.vue.getWidth() / 100) - this.vue.getWfleche());

        // Le placement de la fleche en y dépend de ce qu'elle pointe & du nbJoueurs.
        if (this.vue.getNbJoueur() == 1)
            this.vue.setYfleche(
                    (this.vue.getFleche() == 0) ? ((12 * this.vue.getHeight() / 100) + this.vue.getSautLigne())
                            : (12 * this.vue.getHeight() / 100) + this.vue.getSautLigne() * (this.vue.getFleche() + 2));
        else
            this.vue.setYfleche((this.vue.getFleche() <= 1)
                    ? ((12 * this.vue.getHeight() / 100) + this.vue.getSautLigne() * (2 * this.vue.getFleche() + 1))
                    : (12 * this.vue.getHeight() / 100) + this.vue.getSautLigne() * (this.vue.getFleche() + 3));

        // On update les noms à afficher.
        this.vue.setNomJ1(createImageOfMot(this.vue.getNom1()));
        this.vue.setNomJ2(
                (this.vue.getNbJoueur() == 2) ? createImageOfMot(this.vue.getNom2()) : createImageOfMot(null));
    }

    // Affiche les images.
    @Override
    public void affiche(Graphics g) { // Prend en argument le contexte graphique de la vue.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();

        // Affichage background.
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        // Affichage des boutons.
        int x = (9 * this.vue.getWidth() / 100), y = (12 * this.vue.getHeight() / 100);
        int w = this.vue.getWidth() * 30 / 640, h = this.vue.getHeight() * 30 / 1026,
                espacement = this.vue.getWidth() * 15 / 640, ecart = this.vue.getWidth() * 20 / 640;
        x = afficheMot(g2, this.vue.getMessageNom(), x, y, w, h, ecart, espacement);
        afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);

        x = (15 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne();
        afficheMot(g2, this.vue.getNomJ1(), x, y, w, h, ecart, espacement);
        if (this.vue.getNbJoueur() == 2) {
            x = (9 * this.vue.getWidth() / 100);
            y += this.vue.getSautLigne();
            x = afficheMot(g2, this.vue.getMessageNom(), x, y, w, h, ecart, espacement);
            afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
            x = (15 * this.vue.getWidth() / 100);
            y += this.vue.getSautLigne();
            afficheMot(g2, this.vue.getNomJ2(), x, y, w, h, ecart, espacement);
        }
        x = (9 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne() * 2;
        afficheMot(g2, this.vue.getButtonJouer(), x, y, w, h, ecart, espacement);
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

        while (Vue.isMenuLancement) { // Tant que l'on est dans le menu LANCEMENT :
            this.update(); // On update.
            this.affiche(this.vue.getGraphics()); // On affiche.
        }
    }

    // Gestion des boutons.
    @Override
    public void keyControlPressed(KeyEvent e) { // KeyEvent e de la vue.

        if (this.vue.getFleche() == 0) { // Si la flèche pointe sur le bouton "Nom 1" :
            this.vue.setNom1(
                    (this.vue.getNom1().length() < 16) ? this.vue.getNom1() + keyWriterNom(e) : this.vue.getNom1());
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                this.vue.setNom1("");
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.vue.getFleche() == this.vue.getNbJoueur()) { // Si la flèche pointe sur le bouton "Jouer" :
                this.vue.geteGame().createPartie(); // On crée une partie.
                Vue.isMenuLancement = false; // On quitte le menu LANCEMENT.
                Vue.isRunningGame = true; // On passe à la GAME.
            }

            // Si la flèche pointe sur le bouton "Retour au menu DEMARRER" :
            if (this.vue.getFleche() == this.vue.getNbJoueur() + 1) {
                Vue.isMenuLancement = false; // On quitte le menu LANCEMENT.
                Vue.isMenuDemarrer = true; // On passe dans le menu DEMARRER.
            }
            if (this.vue.getFleche() == this.vue.getNbJoueur() + 2) { // Si la flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte le jeu.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }

        // Sinon, les autres cas dépendent du nombre de joueurs.
        if (this.vue.getNbJoueur() == 1) {
            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.vue.setFleche((this.vue.getFleche() == 0) ? 3 : this.vue.getFleche() - 1);

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.vue.setFleche((this.vue.getFleche() == 3) ? 0 : this.vue.getFleche() + 1);

        }

        if (this.vue.getNbJoueur() == 2) {
            if (this.vue.getFleche() == 1) { // La flèche pointe sur le bouton "Nom 2" :
                this.vue.setNom2(
                        (this.vue.getNom2().length() < 16) ? this.vue.getNom2() + keyWriterNom(e) : this.vue.getNom2());
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                    this.vue.setNom2("");
            }

            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.vue.setFleche((this.vue.getFleche() == 0) ? 4 : this.vue.getFleche() - 1);

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.vue.setFleche((this.vue.getFleche() == 4) ? 0 : this.vue.getFleche() + 1);
        }
    }

    @Override
    public void keyControlReleased(KeyEvent e) {
    }

    // Gère l'écriture du nom.
    private String keyWriterNom(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_0 || e.getKeyCode() == KeyEvent.VK_NUMPAD0 || e.getKeyChar() == '0')
            return "0";
        if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyChar() == '1')
            return "1";
        if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyChar() == '2')
            return "2";
        if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyChar() == '3')
            return "3";
        if (e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyChar() == '4')
            return "4";
        if (e.getKeyCode() == KeyEvent.VK_5 || e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyChar() == '5')
            return "5";
        if (e.getKeyCode() == KeyEvent.VK_6 || e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyChar() == '6')
            return "6";
        if (e.getKeyCode() == KeyEvent.VK_7 || e.getKeyCode() == KeyEvent.VK_NUMPAD7 || e.getKeyChar() == '7')
            return "7";
        if (e.getKeyCode() == KeyEvent.VK_8 || e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyChar() == '8')
            return "8";
        if (e.getKeyCode() == KeyEvent.VK_9 || e.getKeyCode() == KeyEvent.VK_NUMPAD9 || e.getKeyChar() == '9')
            return "9";
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyChar() == 'à' || e.getKeyChar() == 'À')
            return "A";
        if (e.getKeyCode() == KeyEvent.VK_B)
            return "B";
        if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyChar() == 'ç')
            return "C";
        if (e.getKeyCode() == KeyEvent.VK_D)
            return "D";
        if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyChar() == 'é' || e.getKeyChar() == 'è')
            return "E";
        if (e.getKeyCode() == KeyEvent.VK_F)
            return "F";
        if (e.getKeyCode() == KeyEvent.VK_G)
            return "G";
        if (e.getKeyCode() == KeyEvent.VK_H)
            return "H";
        if (e.getKeyCode() == KeyEvent.VK_I)
            return "I";
        if (e.getKeyCode() == KeyEvent.VK_J)
            return "J";
        if (e.getKeyCode() == KeyEvent.VK_K)
            return "K";
        if (e.getKeyCode() == KeyEvent.VK_L)
            return "L";
        if (e.getKeyCode() == KeyEvent.VK_M)
            return "M";
        if (e.getKeyCode() == KeyEvent.VK_N)
            return "N";
        if (e.getKeyCode() == KeyEvent.VK_O)
            return "O";
        if (e.getKeyCode() == KeyEvent.VK_P)
            return "P";
        if (e.getKeyCode() == KeyEvent.VK_Q)
            return "Q";
        if (e.getKeyCode() == KeyEvent.VK_R)
            return "R";
        if (e.getKeyCode() == KeyEvent.VK_S)
            return "S";
        if (e.getKeyCode() == KeyEvent.VK_T)
            return "T";
        if (e.getKeyCode() == KeyEvent.VK_U)
            return "U";
        if (e.getKeyCode() == KeyEvent.VK_V)
            return "V";
        if (e.getKeyCode() == KeyEvent.VK_W)
            return "W";
        if (e.getKeyCode() == KeyEvent.VK_X)
            return "X";
        if (e.getKeyCode() == KeyEvent.VK_Y)
            return "Y";
        if (e.getKeyCode() == KeyEvent.VK_Z)
            return "Z";
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            return " ";
        return "";
    }

    public void mouseClicked(MouseEvent e) {
        int y = (12 * this.vue.getHeight() / 100) + 3 * this.vue.getSautLigne();
        int h = this.vue.getHeight() * 30 / 1026;

        if (this.vue.getNbJoueur() == 2) {
            y = +this.vue.getSautLigne();
        }
        if (e.getY() > y && e.getY() < y + h) {
            this.vue.geteGame().createPartie(); // On crée une partie.
            Vue.isMenuLancement = false; // On quitte le menu LANCEMENT.
            Vue.isRunningGame = true; // On passe à la GAME.
            return;
        }
        y += this.vue.getSautLigne();
        // Si la flèche pointe sur le bouton "Retour au menu DEMARRER" :
        if (e.getY() > y && e.getY() < y + h) {
            Vue.isMenuLancement = false; // On quitte le menu LANCEMENT.
            Vue.isMenuDemarrer = true; // On passe dans le menu DEMARRER.
            return;
        }
        y += this.vue.getSautLigne();
        if (e.getY() > y && e.getY() < y + h) { // Si la flèche pointe sur le bouton "Quitter" :
            System.out.println("À la prochaine !");
            Vue.isQuitte = true; // On quitte le jeu.
            System.exit(0); // On ferme toutes les fenêtres & le programme.
        }
    }
}
