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

public abstract class Etat {

    protected boolean isRunning;
    protected final int width, height; // Dimensions du panel
    // La fleche est un curseur qui indique sur quel boutton on agit actuellement
    protected int fleche, xfleche, yfleche, wfleche, hfleche, sautLigne, nbJoueur;
    protected String chemin, winchemin, nom1, nom2; // Le chemin vers le package d'images & les noms des joueurs.
    protected BufferedImage view, backgroundView, flecheView;
    protected ArrayList<BufferedImage> buttonJouer, buttonJouerSolo, button2joueur, buttonMultiJoueur, buttonLb,
            buttonQuitter, buttonRetourMenu, titreStatut;
    protected ArrayList<ArrayList<BufferedImage>> scoreFinalView, hightScoreView;
    protected Terrain terrain; // Le terrain sur lequel on joue
    protected boolean multijoueur = false, host = false;
    protected Serveur serveur;
    protected JoueurConnecte jconnect;
    protected Vue vue;

    public Etat(Vue vue) {
        this.vue = vue;
        // Taille du panel
        this.width = vue.getWidth();
        this.height = vue.getHeight();

        // Initialisation des chemins
        String skin = vue.getSkin();
        this.chemin = (new File("gui/images/" + skin + "/")).getAbsolutePath();
        this.winchemin = "src/gui/images/" + skin + "/";

    }

    /// Méthodes abstract redéfinies dans chaque sous-classe d'Etat.
    // Initialise les images qui ne changeront jamais.
    abstract void initFixe();

    // Initialise les images & les autres variables.
    abstract void init();

    // Update les images & autres variables.
    abstract void update();

    // Affiche les images.
    abstract void affiche(Graphics g);

    // Fait tourner cet état en boucle.
    abstract void running(Graphics g);

    // Gère les boutons.
    abstract void keyControlPressed(KeyEvent e);

    abstract void keyControlReleased(KeyEvent e);

    /// Méthodes générales utiles :
    // Crée une liste d'images représentant un mot
    protected ArrayList<BufferedImage> createImageOfMot(String mot) {
        // On crée une liste d'image qui va contenir toutes les lettres du mot
        ArrayList<BufferedImage> motView = new ArrayList<BufferedImage>();
        if (mot == null) {// S'il n'y a rien on retourne le mot "espace ".
            motView.add(null);
            return motView;
        }
        mot = mot.toLowerCase(); // On met toutes les lettres en minuscules pour ne pas avoir d'erreur.

        for (int i = 0; i < mot.length(); ++i) { // Pour chaque lettre du mot :
            char c = mot.charAt(i);
            try { // Double try_catch pour gérer la portabilité sur Windows.
                try {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                } catch (Exception e) {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(winchemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return motView; // Cette méthode ne gère pas les caractères spéciaux (excepté " ").
    }

    // Permet d'afficher un mot
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

    // Permet d'afficher un point/double point
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFleche() {
        return fleche;
    }

    public void setFleche(int fleche) {
        this.fleche = fleche;
    }

    public int getXfleche() {
        return xfleche;
    }

    public void setXfleche(int xfleche) {
        this.xfleche = xfleche;
    }

    public int getYfleche() {
        return yfleche;
    }

    public void setYfleche(int yfleche) {
        this.yfleche = yfleche;
    }

    public int getWfleche() {
        return wfleche;
    }

    public void setWfleche(int wfleche) {
        this.wfleche = wfleche;
    }

    public int getHfleche() {
        return hfleche;
    }

    public void setHfleche(int hfleche) {
        this.hfleche = hfleche;
    }

    public int getSautLigne() {
        return sautLigne;
    }

    public void setSautLigne(int sautLigne) {
        this.sautLigne = sautLigne;
    }

    public int getNbJoueur() {
        return nbJoueur;
    }

    public void setNbJoueur(int nbJoueur) {
        this.nbJoueur = nbJoueur;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getWinchemin() {
        return winchemin;
    }

    public void setWinchemin(String winchemin) {
        this.winchemin = winchemin;
    }

    public BufferedImage getView() {
        return view;
    }

    public void setView(BufferedImage view) {
        this.view = view;
    }

    public BufferedImage getBackgroundView() {
        return backgroundView;
    }

    public void setBackgroundView(BufferedImage backgroundView) {
        this.backgroundView = backgroundView;
    }

    public BufferedImage getFlecheView() {
        return flecheView;
    }

    public void setFlecheView(BufferedImage flecheView) {
        this.flecheView = flecheView;
    }

    public ArrayList<BufferedImage> getButtonJouer() {
        return buttonJouer;
    }

    public void setButtonJouer(ArrayList<BufferedImage> buttonJouer) {
        this.buttonJouer = buttonJouer;
    }

    public ArrayList<BufferedImage> getButtonJouerSolo() {
        return buttonJouerSolo;
    }

    public void setButtonJouerSolo(ArrayList<BufferedImage> buttonJouerSolo) {
        this.buttonJouerSolo = buttonJouerSolo;
    }

    public ArrayList<BufferedImage> getButton2joueur() {
        return button2joueur;
    }

    public void setButton2joueur(ArrayList<BufferedImage> button2joueur) {
        this.button2joueur = button2joueur;
    }

    public ArrayList<BufferedImage> getButtonMultiJoueur() {
        return buttonMultiJoueur;
    }

    public void setButtonMultiJoueur(ArrayList<BufferedImage> buttonMultiJoueur) {
        this.buttonMultiJoueur = buttonMultiJoueur;
    }

    public ArrayList<BufferedImage> getButtonLb() {
        return buttonLb;
    }

    public void setButtonLb(ArrayList<BufferedImage> buttonLb) {
        this.buttonLb = buttonLb;
    }

    public ArrayList<BufferedImage> getButtonQuitter() {
        return buttonQuitter;
    }

    public void setButtonQuitter(ArrayList<BufferedImage> buttonQuitter) {
        this.buttonQuitter = buttonQuitter;
    }

    public ArrayList<BufferedImage> getButtonRetourMenu() {
        return buttonRetourMenu;
    }

    public void setButtonRetourMenu(ArrayList<BufferedImage> buttonRetourMenu) {
        this.buttonRetourMenu = buttonRetourMenu;
    }

    public ArrayList<BufferedImage> getTitreStatut() {
        return titreStatut;
    }

    public void setTitreStatut(ArrayList<BufferedImage> titreStatut) {
        this.titreStatut = titreStatut;
    }

    public ArrayList<ArrayList<BufferedImage>> getScoreFinalView() {
        return scoreFinalView;
    }

    public void setScoreFinalView(ArrayList<ArrayList<BufferedImage>> scoreFinalView) {
        this.scoreFinalView = scoreFinalView;
    }

    public ArrayList<ArrayList<BufferedImage>> getHightScoreView() {
        return hightScoreView;
    }

    public void setHightScoreView(ArrayList<ArrayList<BufferedImage>> hightScoreView) {
        this.hightScoreView = hightScoreView;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public boolean isMultijoueur() {
        return multijoueur;
    }

    public void setMultijoueur(boolean multijoueur) {
        this.multijoueur = multijoueur;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public Serveur getServeur() {
        return serveur;
    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    public JoueurConnecte getJconnect() {
        return jconnect;
    }

    public void setJconnect(JoueurConnecte jconnect) {
        this.jconnect = jconnect;
    }

}