package gui;

// Import d'autres dossiers
import gameobjects.*;
import multiplayer.*;

// Import de packages java
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

// JPanel s'occupant de tout l'affichage du jeu.
public class Vue extends JPanel implements Runnable, KeyListener {

    // Ces variables static boolean indique l'état actuel du panel.
    public static boolean isQuitte, isRunningGame, isMenuDemarrer, isMenuLancement,isConnecting, isMenuFin, isMenuClassement,
            isSetting;
    private final int width, height; // Dimensions du panel.
    // La fleche est un curseur qui indique sur quel boutton on agit actuellement.
    private int fleche, xfleche, yfleche, wfleche, hfleche, sautLigne, nbJoueur, niveau;
    private String chemin, winchemin, nom1, nom2, skin; // Le chemin vers le package d'images & les noms des joueurs.
    private final ArrayList<String> listPackSkin;
    protected MouseListener mouse;

    // Variables représentant différentes images.
    private BufferedImage view, backgroundView, backgroundClView, backgroundClView1, backgroundClView2, flecheView,
            terrainView, platformeBaseView, platformeMobileView, scoreBackgroundView, projectileView, monstre1View,
            monstre2View, monstre3View, coinView, fuseeView, helicoView;

    private ArrayList<BufferedImage> buttonJouer, buttonJouerSolo, button2joueur, buttonMultiJoueur, buttonLb,
            buttonQuitter, buttonRejouer, buttonRetourMenu, buttonSetting, buttonNiveau, buttonSkin, buttonInertie,
            titreStatut, messInertie, messNiveau, messSkin,
            messageNom, nomJ1, nomJ2;
    private ArrayList<ArrayList<BufferedImage>> joueurDataList, lbView, scoreFinalView, hightScoreView;

    private double deltaTime; // Le temps nécessaire pour update une GAME.
    private Terrain terrain; // Le terrain sur lequel on joue.
    private Thread thread; // La thread liée à ce pannel, qui lance l'exécution.

    // Variables liées au mode multijoueurs.
    private Serveur serveur;
    private JoueurConnecte jconnect;
    private boolean multijoueur, host, isInertie;
    private ThreadMouvement thrmvt;

    // Ces variables représentent les différents états du jeu.
    private Game eGame;
    private MenuFin eMenuFin;
    private MenuDemarrer eMenuDemarrer;
    private MenuClassement eMenuClassement;
    private MenuLancement eMenuLancement;
    private MenuSetting eMenuSetting;

    public Vue(App frame) {
        // Taille du panel.
        this.width = frame.getWidth();
        this.height = frame.getHeight();
        this.setPreferredSize(new Dimension(this.width / 3, (int) (this.height * 0.95)));

        // Liste des skins
        this.listPackSkin = new ArrayList<String>();
        this.listPackSkin.add("packBase");
        this.listPackSkin.add("packTux");

        // Quelques variables par défaut.
        this.niveau = 1;
        this.skin = listPackSkin.get(0);
        this.isInertie = true;

        // Chemins des images.
        this.chemin = (new File("gui/images/" + this.skin + "/")).getAbsolutePath();
        this.winchemin = "src/gui/images/" + this.skin + "/";

        // Gestion d'évènements boutons.
        this.addKeyListener(this);
    }

    /// Méthodes de la classe :
    // Initialise les images qui changent pas en fonction de l'état.
    private void initGENERAL() {
        this.eMenuDemarrer.initFixe();
        this.eMenuSetting.initFixe();
        this.eMenuClassement.initFixe();
        this.eMenuLancement.initFixe();
        this.eGame.initFixe();
        this.eMenuFin.initFixe();
    }

    // Fait tourner le jeu Doodle Jump au complet.
    // Cette méthode contient les traitements à exécuter.
    @Override
    public void run() {
        try {
            // Cette méthode (requestFocusInWindow) demande à ce que ce composant obtienne
            // le focus. Le focus est le fait qu'un composant soit sélectionné ou pas.
            // Le composant doit être afficheable (OK grâce à addNotify()).
            this.requestFocusInWindow();

            /// ETAPE 1 : Premières initialisations.
            // On initialise les différents états.
            this.eMenuDemarrer = new MenuDemarrer(this);
            this.eMenuSetting = new MenuSetting(this);
            this.eMenuClassement = new MenuClassement(this);
            this.eMenuLancement = new MenuLancement(this);
            this.eGame = new Game(this);
            this.eMenuFin = new MenuFin(this);

            this.initGENERAL(); // On initialise les images qui ne changent pas en fonction de l'état.

            while (!isQuitte) { // Tant qu'on a pas quitter le jeu :

                /// ETAPE 2 : On gère les différents états du jeu.
                // 1. On crée un état à partie des données actuelles.
                // 2. (On initialise les images/variables de cet état.)
                // 3. On lance l'exécution de cet état.

                if (isMenuDemarrer) { // Si on est au niveau du menu DEMARRER :
                    this.eMenuDemarrer = new MenuDemarrer(this);
                    this.eMenuDemarrer.running();
                }

                if (isSetting) { // Si on a cliqué sur le boutton "Classement" :
                    this.eMenuSetting = new MenuSetting(this);
                    this.eMenuSetting.init();
                    this.eMenuSetting.running();
                }

                if (isMenuClassement) { // Si on a cliqué sur le boutton "Classement" :
                    this.eMenuClassement = new MenuClassement(this);
                    this.eMenuClassement.init();
                    this.eMenuClassement.running();
                }

                if (isMenuLancement) { // Si on a cliqué sur le boutton "Jouer solo/à 2" :
                    this.eMenuLancement = new MenuLancement(this);
                    this.eMenuLancement.init();
                    this.eMenuLancement.running();
                }
                if(isConnecting){
                    this.eGame.createPartie(); // On crée une partie.
                    this.eGame=new Game(this);
                    this.eGame.init();
                }

                if (isRunningGame) { // Si on a lancé une GAME :
                    if (this.terrain.multiplayer) { // Si on est en mode multijoueur :
                        thrmvt=new ThreadMouvement(this.terrain);
                        new Thread(thrmvt).start();                        
                        System.out.println("diz");
                    }else{
                        this.eGame = new Game(this);
                        this.eGame.init();
                    }
                    this.eGame.running();
                }

                if (!isMenuDemarrer && !isMenuClassement && !isMenuLancement && !isRunningGame && !isMenuFin
                        && this.eGame.endGame()) { // Si c'est la fin de la GAME (quelqu'un a perdu) :
                    // On met à jour toutes les variables boolean.
                    isRunningGame = false;
                    isMenuFin = true;
                    endThrMvt();
                    this.eMenuClassement.updateClassement(); // On met à jour le classement et l'historique.
                }

                if (isMenuFin) { // Si on a fini la GAME sans erreur :
                    this.eMenuFin = new MenuFin(this);
                    this.eMenuFin.init();
                    this.eMenuFin.running();
                }
            }
            System.exit(0); // Si on a quitté le jeu, on ferme tout le programme.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Rend ce composant affichable en le connectant à une ressource d'écran.
    @Override
    public void addNotify() {
        super.addNotify();
        if (this.thread == null) { // Si on a toujours pas lancer le jeu :
            // Créer une nouvelle instance en précisant les traitements à exécuter (run).
            // This est l'objet qui implémente Runnable (run()), contenant les traitements.
            this.thread = new Thread(this);
            isQuitte = false; // Indique que l'on est dans le jeu.
            isMenuDemarrer = true; // Indique le jeu est lancé.
            this.thread.start(); // Invoque la méthode run().
        }
    }

    private void endThrMvt(){
        if(multijoueur){
            try {
                thrmvt.running=false;
                if(serveur!=null)
                    serveur.fermerLeServeur();
                if(jconnect!=null)
                    jconnect.deconnecter();
            } catch (Exception e) {
                e.printStackTrace();;
            }
        }
    }
    /// KeyListener qui gèrent les boutons.
    @Override
    public void keyPressed(KeyEvent e) { // On est actuellement entrain d'appuyer sur des boutons.
        if (isMenuDemarrer) { // Si on est au niveau du menu DEMARRER :
            this.eMenuDemarrer.keyControlPressed(e);
        }

        if (isMenuClassement) { // Si on est au niveau du CLASSEMENT :
            this.eMenuClassement.keyControlPressed(e);
        }

        if (isSetting) { // Si on est au niveau du CLASSEMENT :
            this.eMenuSetting.keyControlPressed(e);
        }

        if (isMenuLancement) { // Si on est au niveau du menu LANCEMENT :
            this.eMenuLancement.keyControlPressed(e);
        }

        if (isRunningGame) { // Si on est en cours de GAME :
            this.eGame.keyControlPressed(e);
        }

        if (isMenuFin) { // Si on est au niveau du menu FIN :
            this.eMenuFin.keyControlPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // On vient de relâcher des boutons.
        if (isRunningGame) { // Si on est en cours de GAME :
            this.eGame.keyControlReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /// Getter & setter
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

    public String getWinchemin() {
        return winchemin;
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

    public BufferedImage getBackgroundClView() {
        return backgroundClView;
    }

    public void setBackgroundClView(BufferedImage backgroundClView) {
        this.backgroundClView = backgroundClView;
    }

    public BufferedImage getBackgroundClView1() {
        return backgroundClView1;
    }

    public void setBackgroundClView1(BufferedImage backgroundClView1) {
        this.backgroundClView1 = backgroundClView1;
    }

    public BufferedImage getBackgroundClView2() {
        return backgroundClView2;
    }

    public void setBackgroundClView2(BufferedImage backgroundClView2) {
        this.backgroundClView2 = backgroundClView2;
    }

    public BufferedImage getFlecheView() {
        return flecheView;
    }

    public void setFlecheView(BufferedImage flecheView) {
        this.flecheView = flecheView;
    }

    public BufferedImage getTerrainView() {
        return terrainView;
    }

    public void setTerrainView(BufferedImage terrainView) {
        this.terrainView = terrainView;
    }

    public BufferedImage getPlatformeBaseView() {
        return platformeBaseView;
    }

    public void setPlatformeBaseView(BufferedImage platformeBaseView) {
        this.platformeBaseView = platformeBaseView;
    }

    public BufferedImage getPlatformeMobileView() {
        return platformeMobileView;
    }

    public void setPlatformeMobileView(BufferedImage platformeMobileView) {
        this.platformeMobileView = platformeMobileView;
    }

    public BufferedImage getScoreBackgroundView() {
        return scoreBackgroundView;
    }

    public BufferedImage getcoinView() {
        return coinView;
    }

    public void setcoinView(BufferedImage img) {
        this.coinView = img;
    }

    public void setScoreBackgroundView(BufferedImage scoreBackgroundView) {
        this.scoreBackgroundView = scoreBackgroundView;
    }

    public BufferedImage getProjectileView() {
        return projectileView;
    }

    public void setProjectileView(BufferedImage projectileView) {
        this.projectileView = projectileView;
    }

    public ArrayList<BufferedImage> getButtonJouer() {
        return buttonJouer;
    }

    public void setButtonJouer(ArrayList<BufferedImage> buttonJouer) {
        this.buttonJouer = buttonJouer;
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

    public ArrayList<BufferedImage> getButtonRejouer() {
        return buttonRejouer;
    }

    public void setButtonRejouer(ArrayList<BufferedImage> buttonRejouer) {
        this.buttonRejouer = buttonRejouer;
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

    public ArrayList<BufferedImage> getMessageNom() {
        return messageNom;
    }

    public void setMessageNom(ArrayList<BufferedImage> messageNom) {
        this.messageNom = messageNom;
    }

    public ArrayList<ArrayList<BufferedImage>> getJoueurDataList() {
        return joueurDataList;
    }

    public void setJoueurDataList(ArrayList<ArrayList<BufferedImage>> joueurDataList) {
        this.joueurDataList = joueurDataList;
    }

    public ArrayList<ArrayList<BufferedImage>> getLbView() {
        return lbView;
    }

    public void setLbView(ArrayList<ArrayList<BufferedImage>> lbView) {
        this.lbView = lbView;
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

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
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

    public MenuDemarrer geteMenuDemarrer() {
        return eMenuDemarrer;
    }

    public void seteMenuDemarrer(MenuDemarrer eMenuDemarrer) {
        this.eMenuDemarrer = eMenuDemarrer;
    }

    public MenuClassement geteMenuClassement() {
        return eMenuClassement;
    }

    public void seteMenuClassement(MenuClassement eMenuClassement) {
        this.eMenuClassement = eMenuClassement;
    }

    public MenuLancement geteMenuLancement() {
        return eMenuLancement;
    }

    public void seteMenuLancement(MenuLancement eMenuLancement) {
        this.eMenuLancement = eMenuLancement;
    }

    public MenuFin geteMenuFin() {
        return eMenuFin;
    }

    public void seteMenuFin(MenuFin eMenuFin) {
        this.eMenuFin = eMenuFin;
    }

    public Game geteGame() {
        return eGame;
    }

    public ArrayList<BufferedImage> getButtonJouerSolo() {
        return buttonJouerSolo;
    }

    public void setButtonJouerSolo(ArrayList<BufferedImage> buttonJouerSolo) {
        this.buttonJouerSolo = buttonJouerSolo;
    }

    public String getNom1() {
        return nom1;
    }

    public void setNom1(String nom1) {
        this.nom1 = nom1;
    }

    public String getNom2() {
        return nom2;
    }

    public void setNom2(String nom2) {
        this.nom2 = nom2;
    }

    public ArrayList<BufferedImage> getNomJ1() {
        return nomJ1;
    }

    public void setNomJ1(ArrayList<BufferedImage> nomJ1) {
        this.nomJ1 = nomJ1;
    }

    public ArrayList<BufferedImage> getNomJ2() {
        return nomJ2;
    }

    public void setNomJ2(ArrayList<BufferedImage> nomJ2) {
        this.nomJ2 = nomJ2;
    }

    public BufferedImage getFuseeView() {
        return fuseeView;
    }

    public void setFuseeView(BufferedImage fuseeView) {
        this.fuseeView = fuseeView;
    }

    public void setMonstre1View(BufferedImage monstre1View) {
        this.monstre1View = monstre1View;
    }

    public void setMonstre2View(BufferedImage monstre2View) {
        this.monstre2View = monstre2View;
    }

    public void setMonstre3View(BufferedImage monstre3View) {
        this.monstre3View = monstre3View;
    }

    public BufferedImage getMonstre1View() {
        return this.monstre1View;
    }

    public BufferedImage getMonstre2View() {
        return this.monstre2View;
    }

    public BufferedImage getMonstre3View() {
        return this.monstre3View;
    }

    public ArrayList<BufferedImage> getButtonSetting() {
        return buttonSetting;
    }

    public void setButtonSetting(ArrayList<BufferedImage> buttonSetting) {
        this.buttonSetting = buttonSetting;
    }

    public ArrayList<BufferedImage> getButtonNiveau() {
        return buttonNiveau;
    }

    public void setButtonNiveau(ArrayList<BufferedImage> buttonNiveau) {
        this.buttonNiveau = buttonNiveau;
    }

    public ArrayList<BufferedImage> getButtonSkin() {
        return buttonSkin;
    }

    public void setButtonSkin(ArrayList<BufferedImage> buttonSkin) {
        this.buttonSkin = buttonSkin;
    }

    public ArrayList<BufferedImage> getButtonInertie() {
        return buttonInertie;
    }

    public void setButtonInertie(ArrayList<BufferedImage> buttonInertie) {
        this.buttonInertie = buttonInertie;
    }

    public ArrayList<BufferedImage> getMessInertie() {
        return messInertie;
    }

    public void setMessInertie(ArrayList<BufferedImage> messInertie) {
        this.messInertie = messInertie;
    }

    public ArrayList<BufferedImage> getMessNiveau() {
        return messNiveau;
    }

    public void setMessNiveau(ArrayList<BufferedImage> messNiveau) {
        this.messNiveau = messNiveau;
    }

    public ArrayList<BufferedImage> getMessSkin() {
        return messSkin;
    }

    public void setMessSkin(ArrayList<BufferedImage> messSkin) {
        this.messSkin = messSkin;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public boolean isInertie() {
        return isInertie;
    }

    public void setInertie(boolean isInertie) {
        this.isInertie = isInertie;
    }

    public String getSkin() {
        return skin;
    }

    public BufferedImage getHelicoView() {
        return this.helicoView;
    }

    public void setHelicoView(BufferedImage helicoView) {
        this.helicoView = helicoView;
    }

    public void setSkin(String skin) {
        this.skin = skin;
        // Chemins des images.
        this.chemin = (new File("gui/images/" + this.skin + "/")).getAbsolutePath();
        this.winchemin = "src/gui/images/" + this.skin + "/";
    }

    public ArrayList<String> getListPackSkin() {
        return listPackSkin;
    }
}