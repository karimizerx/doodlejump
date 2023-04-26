package gui;

// Import de packages java
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;

// Classe abstraite représentant un état du jeu.
public abstract class Etat implements MouseListener {

    protected Vue vue; // Ne possède qu'un champs vue, pour gérer ses attributs.

    public Etat(Vue vue) {
        this.vue = vue;
    }

    /// Méthodes abstract redéfinies dans chaque sous-classe d'Etat :
    // Initialise les images & variables qui ne changeront jamais.
    abstract void initFixe();

    // Initialise les images & les autres variables.
    abstract void init();

    // Update les images & autres variables.
    abstract void update();

    // Affiche les images.
    abstract void affiche(Graphics g);

    // Fait tourner cet état en boucle.
    abstract void running();

    // Gestion des boutons.
    abstract void keyControlPressed(KeyEvent e);

    abstract void keyControlReleased(KeyEvent e);

    /// Méthodes générales utiles :
    // Crée une liste d'images représentant un mot.
    protected ArrayList<BufferedImage> createImageOfMot(String mot) {
        // On crée une liste d'image qui va contenir toutes les lettres du mot
        ArrayList<BufferedImage> motView = new ArrayList<BufferedImage>();
        if (mot == null) { // S'il n'y a rien on retourne le mot "espace ".
            motView.add(null);
            return motView;
        }
        mot = mot.toLowerCase(); // On met toutes les lettres en minuscules pour ne pas avoir d'erreur.

        for (int i = 0; i < mot.length(); ++i) { // Pour chaque lettre du mot :
            char c = mot.charAt(i);
            try { // Double try_catch pour gérer la portabilité sur Windows.
                try {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(this.vue.getChemin() + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                } catch (Exception e) {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(this.vue.getWinchemin() + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return motView; // Cette méthode ne gère pas les caractères spéciaux (excepté " ").
    }

    // Affiche un mot.
    protected int afficheMot(Graphics2D g2, ArrayList<BufferedImage> mot, int x, int y, int w, int h, int ecart,
            int espacement) {
        // L'écart = l'espace entre 2 lettre, l'espacement = le caractère espace
        for (int i = 0; i < mot.size(); ++i) {
            BufferedImage lettreView = mot.get(i);
            if (lettreView != null) { // Si c'est null, c'est égal à espace
                g2.drawImage(lettreView, x, y, w, h, null);
                x += ecart;
            } else
                x += espacement;
        }
        return x;
    }

    // Affichent un point/double point.
    protected int affichePoint(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(Color.BLACK);
        g2.fillOval(x, y - h, w, h);
        return x + w;
    }

    protected int afficheDoublepoint(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(Color.BLACK);
        g2.fillOval(x, y + h / 2, w, h);
        g2.fillOval(x, y + h / 2 + 2 * h, w, h);
        return x + w;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void removelistners() {
        for (MouseListener l : vue.getMouseListeners())
            vue.removeMouseListener(l);
        for (MouseMotionListener l : vue.getMouseMotionListeners())
            vue.removeMouseMotionListener(l);
    }
}