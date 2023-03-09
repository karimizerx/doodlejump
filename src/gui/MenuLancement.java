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

    private ArrayList<BufferedImage> messageNom, nomJ1, nomJ2;

    public MenuLancement(Vue vue) {
        super(vue);
    }

    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
        this.messageNom = createImageOfMot("Entrez un nom ");
        this.buttonJouer = createImageOfMot("Jouer");
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
        // On initialise les noms des joueurs.
        History h = new History();
        if (this.nbJoueur == 2) {
            this.nom1 = "MIZER 1";
            this.nom2 = "MIZER 2";
        } else // Si le joueur a déjà joué une partie, on prend le nom de la dernière partie.
            nom1 = (h.getLbData().size() > 1) ? h.getLbData().get(h.getLbData().size() - 1)[1] : "MIZER";

        this.nomJ1 = createImageOfMot(nom1);
        this.nomJ2 = createImageOfMot(nom2);
    }

    // Update les images & autres variables.
    public void update() {
        this.wfleche = 30;
        this.hfleche = 30;
        this.xfleche = (7 * width / 100) - this.wfleche; // La fleche se place toujours ici en x

        // Son placement en y dépend de ce qu'elle pointe
        if (this.nbJoueur == 1) {
            this.yfleche = (fleche == 0) ? ((12 * height / 100) + sautLigne)
                    : (12 * height / 100) + sautLigne * (fleche + 2);
        } else {
            this.yfleche = (fleche <= 1) ? ((12 * height / 100) + sautLigne * (2 * fleche + 1))
                    : (12 * height / 100) + sautLigne * (fleche + 3);

        }
        this.nomJ1 = createImageOfMot(nom1); // On update les noms à afficher.
        this.nomJ2 = (this.nbJoueur == 2) ? createImageOfMot(nom2) : createImageOfMot(null);
    }

    // Affiche les images.
    public void affiche(Graphics g) { // Prend en argument le contexte graphique.
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        // Affichage des boutons
        int x = (9 * width / 100), y = (12 * height / 100);
        int w = 30, h = 30, espacement = 15, ecart = 20;
        x = afficheMot(g2, messageNom, x, y, w, h, ecart, espacement);
        afficheDoublepoint(g2, x, y, 7, 7);
        x = (15 * width / 100);
        y += sautLigne;
        afficheMot(g2, nomJ1, x, y, w, h, ecart, espacement);
        if (nbJoueur == 2) {
            x = (9 * width / 100);
            y += sautLigne;
            x = afficheMot(g2, messageNom, x, y, w, h, ecart, espacement);
            afficheDoublepoint(g2, x, y, 7, 7);
            x = (15 * width / 100);
            y += sautLigne;
            afficheMot(g2, nomJ2, x, y, w, h, ecart, espacement);
        }
        x = (9 * width / 100);
        y += sautLigne * 2;
        afficheMot(g2, buttonJouer, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y += sautLigne;
        afficheMot(g2, buttonRetourMenu, x, y, w, h, ecart, espacement);
        x = (9 * width / 100);
        y += sautLigne;
        afficheMot(g2, buttonQuitter, x, y, w, h, ecart, espacement);

        // Affichage de la fleche
        g2.drawImage(flecheView, xfleche, yfleche, wfleche, hfleche, null);

        // Affichage final
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner cet état en boucle.
    public void running(Graphics g) {
        this.sautLigne = 50;
        this.fleche = 0; // On pointe le premier bouton
        while (Vue.isMenuLancement) {
            this.update();
            this.affiche(g);
        }
    }

    // Gère les boutons.
    @Override
    public void keyControlPressed(KeyEvent e) {
        System.out.println("Menu LANCEMENT - Key Pressed");
        if (this.nbJoueur == 1) {
            if (this.fleche == 0) { // La flèche pointe sur le bouton "Nom 1" :
                nom1 = (nom1.length() < 16) ? nom1 += keyWriterNom(e) : nom1;
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                    nom1 = "";
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (this.fleche == 1) { // La flèche pointe sur le bouton "Jouer" :
                    // createPartie(); // On crée une partie.
                    Vue.isMenuLancement = false;
                    Vue.isRunningGame = true;
                }

                if (this.fleche == 2) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                    Vue.isMenuLancement = false; // On quitte le menu 2.
                    Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
                }
                if (this.fleche == 3) { // La flèche pointe sur le bouton "Quitter" :
                    System.out.println("À la prochaine !");
                    Vue.isQuitte = true; // On quitte l'application.
                    System.exit(0); // On ferme toutes les fenêtres & le programme.
                }
            }

            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.fleche = (this.fleche == 0) ? 3 : this.fleche - 1;

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.fleche = (this.fleche == 3) ? 0 : this.fleche + 1;

        }

        if (this.nbJoueur == 2) {
            if (this.fleche == 0) { // La flèche pointe sur le bouton "Nom 1" :
                nom1 = (nom1.length() < 16) ? nom1 += keyWriterNom(e) : nom1;
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                    nom1 = "";
            }
            if (this.fleche == 1) { // La flèche pointe sur le bouton "Nom 2" :
                nom2 = (nom2.length() < 16) ? nom2 += keyWriterNom(e) : nom2;
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                    nom2 = "";
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (this.fleche == 2) { // La flèche pointe sur le bouton "Jouer" :
                    // createPartie(); // On crée une partie.
                    Vue.isMenuLancement = false;
                    Vue.isRunningGame = true;
                }

                if (this.fleche == 3) { // La flèche pointe sur le bouton "Retour au menu DEMARRER" :
                    Vue.isMenuLancement = false; // On quitte le menu 2.
                    Vue.isMenuDemarrer = true; // On entre dans le menu DEMARRER.
                }
                if (this.fleche == 4) { // La flèche pointe sur le bouton "Quitter" :
                    System.out.println("À la prochaine !");
                    Vue.isQuitte = true; // On quitte l'application.
                    System.exit(0); // On ferme toutes les fenêtres & le programme.
                }
            }

            /// Gestion de la flèche :
            if (e.getKeyCode() == KeyEvent.VK_UP)
                this.fleche = (this.fleche == 0) ? 4 : this.fleche - 1;

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                this.fleche = (this.fleche == 4) ? 0 : this.fleche + 1;

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
