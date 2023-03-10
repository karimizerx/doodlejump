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
public class MenuLancement extends Etat { // C'est donc un Etat.

    public MenuLancement(Vue vue) {
        super(vue);
    }

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

        this.vue.setNomJ1(createImageOfMot(this.vue.getNom1()));
        this.vue.setNomJ2(createImageOfMot(this.vue.getNom2()));
    }

    // Update les images & autres variables.
    public void update() {
        this.vue.setWfleche(30);
        this.vue.setHfleche(30);
        // La fleche se place toujours ici x
        this.vue.setXfleche((7 * this.vue.getWidth() / 100) - this.vue.getWfleche());

        // Son placement en y dépend de ce qu'elle pointe
        if (this.vue.getNbJoueur() == 1) {
            this.vue.setYfleche(
                    (this.vue.getFleche() == 0) ? ((12 * this.vue.getHeight() / 100) + this.vue.getSautLigne())
                            : (12 * this.vue.getHeight() / 100) + this.vue.getSautLigne() * (this.vue.getFleche() + 2));
        } else {
            this.vue.setYfleche((this.vue.getFleche() <= 1)
                    ? ((12 * this.vue.getHeight() / 100) + this.vue.getSautLigne() * (2 * this.vue.getFleche() + 1))
                    : (12 * this.vue.getHeight() / 100) + this.vue.getSautLigne() * (this.vue.getFleche() + 3));

        }
        this.vue.setNomJ1(createImageOfMot(this.vue.getNom1())); // On update les noms à afficher.
        this.vue.setNomJ2(
                (this.vue.getNbJoueur() == 2) ? createImageOfMot(this.vue.getNom2()) : createImageOfMot(null));
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();
        // Affichage terrain
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        // Affichage des boutons
        int x = (9 * this.vue.getWidth() / 100), y = (12 * this.vue.getHeight() / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        x = afficheMot(g2, this.vue.getMessageNom(), x, y, w, h, ecart, espacement);
        afficheDoublepoint(g2, x, y, 7, 7);
        x = (15 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne();
        afficheMot(g2, this.vue.getNomJ1(), x, y, w, h, ecart, espacement);
        if (this.vue.getNbJoueur() == 2) {
            x = (9 * this.vue.getWidth() / 100);
            y += this.vue.getSautLigne();
            x = afficheMot(g2, this.vue.getMessageNom(), x, y, w, h, ecart, espacement);
            afficheDoublepoint(g2, x, y, 7, 7);
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
        while (Vue.isMenuLancement) {
            this.update();
            this.affiche(this.vue.getGraphics());
        }
    }

    // Gère les boutons.
    @Override
    public void keyControlPressed(KeyEvent e) {
        System.out.println("Menu LANCEMENT - Key Pressed : " + this.vue.getFleche());
        if (this.vue.getNbJoueur() == 1) {
            if (this.vue.getFleche() == 0) { // La flèche pointe sur le bouton "Nom 1" :
                this.vue.setNom1(
                        (this.vue.getNom1().length() < 16) ? this.vue.getNom1() + keyWriterNom(e) : this.vue.getNom1());
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                    this.vue.setNom1("");
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (this.vue.getFleche() == 1) { // La flèche pointe sur le bouton "Jouer" :
                    this.vue.geteGame().createPartie(); // On crée une partie.
                    Vue.isMenuLancement = false;
                    Vue.isRunningGame = true;
                    System.out.println("demarrer : " + Vue.isMenuDemarrer);
                    System.out.println("classement : " + Vue.isMenuClassement);
                    System.out.println("lancement : " + Vue.isMenuLancement);
                    System.out.println("jeu : " + Vue.isRunningGame);
                    System.out.println("fin : " + Vue.isMenuFin);
                }

                if (this.vue.getFleche() == 2) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                    Vue.isMenuLancement = false; // On quitte le menu 2.
                    Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
                }
                if (this.vue.getFleche() == 3) { // La flèche pointe sur le bouton "Quitter" :
                    System.out.println("À la prochaine !");
                    Vue.isQuitte = true; // On quitte l'application.
                    System.exit(0); // On ferme toutes les fenêtres & le programme.
                }
            }

            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.vue.setFleche((this.vue.getFleche() == 0) ? 3 : this.vue.getFleche() - 1);

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.vue.setFleche((this.vue.getFleche() == 3) ? 0 : this.vue.getFleche() + 1);

        }

        if (this.vue.getNbJoueur() == 2) {
            if (this.vue.getFleche() == 0) { // La flèche pointe sur le bouton "Nom 1" :
                this.vue.setNom1(
                        (this.vue.getNom1().length() < 16) ? this.vue.getNom1() + keyWriterNom(e) : this.vue.getNom1());
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                    this.vue.setNom1("");
            }
            if (this.vue.getFleche() == 1) { // La flèche pointe sur le bouton "Nom 2" :
                this.vue.setNom2(
                        (this.vue.getNom2().length() < 16) ? this.vue.getNom2() + keyWriterNom(e) : this.vue.getNom2());
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                    this.vue.setNom2("");
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (this.vue.getFleche() == 2) { // La flèche pointe sur le bouton "Jouer" :
                    this.vue.geteGame().createPartie(); // On crée une partie.
                    Vue.isMenuLancement = false;
                    Vue.isRunningGame = true;
                }

                if (this.vue.getFleche() == 3) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                    Vue.isMenuLancement = false; // On quitte le menu 2.
                    Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
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

        }

    }

    @Override
    public void keyControlReleased(KeyEvent e) {
        System.out.println("Menu LANCEMENT - Key Released");
    }

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

}
