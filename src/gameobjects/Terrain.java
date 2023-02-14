package gameobjects;

import java.io.IOException;
// Import de packages java
import java.util.ArrayList;
import java.util.Random;

import javax.swing.plaf.multi.MultiInternalFrameUI;

// Import d'autres dossiers
import gui.Vue;
import multiplayer.JoueurConnecte;
import multiplayer.Serveur;
import multiplayer.ThreadMouvement;

public class Terrain{

    private ArrayList<Plateforme> plateformesListe;

    private ArrayList<Joueur> ListeJoueurs;
    private double y = 0;// hauteur du jeu. On l'utilisera aussi pour le score
    private int diff_plateformes = 40; // Différence de y entre 2 plateformes
    public boolean isMenu, isEsc,pause=false;


    public boolean multiplayer;
    public boolean isHost;
    public Serveur host=null;
    public JoueurConnecte client=null;
    public final int playerID;// si cest zero; il est host ou il est pas multijoueur
    

    private final double height, width;// dimensions du terrain

    /**
     * Baisse plus le score monte, affecte la densite des plateformes et la proba
     * qu'un helicoptere ou fusee apparaisse.
     * Inversement, affecte la proba d'apparition des monstres, sûrement on inverse
     * 1/difficulte
     */
    private double difficulty = 1.0;

    public Terrain(ArrayList<Joueur> ljoueur, double height, double width,boolean host,boolean multiplayer,int id) {
        this.plateformesListe = new ArrayList<Plateforme>();
        ListeJoueurs=ljoueur;
        this.height = height;
        this.width = width;
        this.multiplayer=multiplayer;
        this.isHost =host;
        playerID=id;
        generateObstacles();
        // if(this.multiplayer){
        //     if(isHost){ 
        //         this.host=new Serveur();
        //         this.host.run();
        //     }else{
        //         client=new JoueurConnecte();
        //         client.connecter();
        //     }    
        // }else
         if (!multiplayer)isHost=false;
    }

    /**
     * Crée la liste des plateformes
     * 
     * @param nb nombres d'obstacles à générer
     * 
     plateformesListe.add(new PlateformeBase(0,this.height-50 , width, 50, -10));
     */
    private void generateObstacles() {
        // Génère des plateformes à coord aléatoires pour la liste des plateformes
        for (int i = (int) height; i > 0; i -= diff_plateformes) {
            // On définit la largeur/hauteur des plateformes de base
            int w = 60, h = 20;
            int x = new Random().nextInt((int) this.width - w);
            int y = i;
            plateformesListe.add(new PlateformeBase(x, y, w, h, -10));
        }
        // On s'assure d'aboird toujours une solution au début
    }

    private Plateforme highestPlateforme() {
        Plateforme plateformeLaPlusHaute = plateformesListe.get(0);
        for (Plateforme p : plateformesListe) {
            if (p.getY() <= plateformeLaPlusHaute.getY()) {
                plateformeLaPlusHaute = p;
            }
        }
        return plateformeLaPlusHaute;
    }


    // Gère, pour le perso, le débordement de l'écran
    private void limite(Personnage p) {
        // 0.43 est la valeur exacte de la moitié du perso
        // Si + de la moitié du perso est sortie du côté gauche de l'écran
        // => on place la moitié du perso au côté droit de l'écran
        if (p.getX() + p.getWidth() * 0.43 <= 0)
            p.setX(this.width - (p.getWidth() * 0.43));
        else if (p.getX() + p.getWidth() * 0.43 >= width) // Et inversement
            p.setX(-(p.getWidth() * 0.43));
    }

    public void update() {
        if((isHost && multiplayer))update(ListeJoueurs.get(0));
        else if((!isHost && multiplayer)||!multiplayer){
            for(Joueur j: ListeJoueurs)
                update(j);
        }
    }

    public void update(Joueur j){
        Personnage p = j.getPerso();
        
        // Ralentissement progressif après un saut
        p.setDy(p.getDy() + 0.2);
        p.setY(p.getY() + p.getDy());
        // Si on est tout en bas de la fenêtre, endGame()
        if (p.getY() + 0.87 * p.getHeight() >= this.height) {
            Vue.isRunning = false;
        }

        if (p.getY() < this.height / 2 && (((isHost && multiplayer)||!multiplayer))) {

            
            difficulty = (difficulty > 5) ? 5 : difficulty + 0.0006;
            p.setY(this.height / 2);
            j.setScore(j.getScore() + 1); // On incrémente le score de 1
            for (Plateforme pf : plateformesListe) {
                pf.setY(pf.getY() - (int) p.getDy());
                if (pf.getY() - pf.getHeight() >= this.height * 0.95) {
                    pf.setY(highestPlateforme().getY() - (diff_plateformes * difficulty)
                            + ((new Random().nextInt(11) * (new Random().nextInt(3) - 1)) * difficulty / 2));
                }
            }
        }
        for (Plateforme pf : plateformesListe) {
            p.collides_plateforme(pf);
        }
        limite(p);
    }

    // Getter & Setter

    public ArrayList<Plateforme> getPlateformesListe() {
        return plateformesListe;
    }

    public void setPlateformesListe(ArrayList<Plateforme> plateformesListe) {
        this.plateformesListe = plateformesListe;
    }



    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }



    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }



    public boolean isMultiplayer() {
        return multiplayer;
    }

    public boolean isHost() {
        return isHost;
    }

    public Serveur getHost() {
        return host;
    }

    public JoueurConnecte getClient() {
        return client;
    }

    public  ArrayList<Joueur> getListeJoueurs(){
        return ListeJoueurs;
    }

    public Joueur getMyPlayer(){
        return this.ListeJoueurs.get(playerID);
    }
    public void setHost(Serveur host) {
        this.host = host;
    }
    public void setJoueurConnecte(JoueurConnecte j){
        client=j;
    }

    public void setJoueur(ArrayList<Joueur> l) {
        ListeJoueurs=l;
    }



}