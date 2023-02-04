package gameobjects;

import java.io.IOException;
// Import de packages java
import java.util.ArrayList;
import java.util.Random;

// Import d'autres dossiers
import gui.Vue;
import multiplayer.JoueurConnecte;
import multiplayer.Serveur;
import multiplayer.ThreadMouvement;

public class Terrain{

    private ArrayList<Plateforme> plateformesListe;
    private Joueur joueurA;
    private Joueur joueurB=null;
    private double y = 0;// hauteur du jeu. On l'utilisera aussi pour le score

    public boolean multiplayer=true;
    public boolean isHost=false;
    private Serveur host=null;
    private JoueurConnecte client=null;

    private final double height, width;// dimensions du terrain

    /**
     * Baisse plus le score monte, affecte la densite des plateformes et la proba
     * qu'un helicoptere ou fusee apparaisse.
     * Inversement, affecte la proba d'apparition des monstres, sûrement on inverse
     * 1/difficulte
     */
    private double difficulty = 1.0;

    public Terrain(Joueur joueurA,Joueur joueurB, double height, double width) {
        this.plateformesListe = new ArrayList<Plateforme>();
        this.joueurA = joueurA;
        this.joueurB = joueurB;
        this.height = height;
        this.width = width;
        generateObstacles(20);
        if(this.multiplayer){
            if(isHost){ 
            try{
            host=new Serveur();
            host.run();
            }catch(IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
            }else{
                client=new JoueurConnecte();
                client.connecter();
            }    
        }
    }

    /**
     * Crée la liste des plateformes
     * 
     * @param nb nombres d'obstacles à générer
     * 
     */
    private void generateObstacles(int nb) {
        plateformesListe = new ArrayList<Plateforme>();

        for (int i = 0; i < (nb * difficulty); i++) {
            Plateforme p = new PlateformeBase(new Random().nextInt((int) this.width),
                    new Random().nextInt((int) this.height), 60, 20, -10);
            plateformesListe.add(p);
        }
    }

    /**
     * 
     * @param maxY indique la coordonnee maximale que peut avoir un objet en y,
     *             si supérieur, on le retire
     */
    private void removeObstacles(double maxY) {
        int c = 0;// compte le nombres d'obstacles qu'on enleve
        for (GameObject gameObject : plateformesListe) {
            if (gameObject.getY() >= maxY) {
                plateformesListe.remove(gameObject);
                ++c;
            }
        }
        // si on veut des nouveaux obstacles.
        generateObstacles(c);
    }

    private void limite(GameObject object) {
        if (object.getX() + object.getWidth() / 2 <= 0) // bord a droite, on le mets a gauche
            object.setX(width - object.getWidth() / 2);
        else if (object.getX() + object.getWidth() / 2 >= width) // bord a gauche, on le mets a gauche
            object.setX(object.getWidth() / 2);
    }

    public void update() {
        if((isHost && multiplayer)||!multiplayer){
            update(joueurA);
            if(joueurB!=null)update(joueurB);
            if(isHost && multiplayer)host.sendTerrain(this);
        }else client.receiveTerrain(this);
    }

    public void update(Joueur j){
        Personnage p = j.getPerso();

        p.setDy(p.getDy() + 0.2);
        p.setY(p.getY() + p.getDy());
        // Si on est tout en bas de la fenêtre, endGame()
        if (p.getY() + 0.7 * p.getHeight() >= this.height) {
            Vue.isRunning = false;
        }
        if (p.getY() < this.height / 2) {
            p.setY(this.height / 2);
            for (Plateforme pf : plateformesListe) {
                pf.setY(pf.getY() - (int) p.getDy());
                if (pf.getY() > this.height) {
                    pf.setY(0);
                    int r = new Random().nextInt(500);
                    pf.setX(r);
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

    public Joueur getJoueurA() {
        return joueurA;
    }

    public void setJoueurA(Joueur joueur) {
        this.joueurA = joueur;
    }

    public Joueur getJoueurB() {
        return joueurB;
    }

    public void setJoueurB(Joueur joueur) {
        this.joueurB = joueur;
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

    public int getPlayerBmvt() {
        if(joueurB.getPerso().isLeft && joueurB.getPerso().isRight ||!joueurB.getPerso().isLeft && !joueurB.getPerso().isRight) return 0;
        if(joueurB.getPerso().isRight) return 1;
        return -1;
    }

    public void setPlayerBmvt(int i) {
        switch(i){
            case -1: joueurB.getPerso().isLeft=true;joueurB.getPerso().isRight=false;break;
            case 0: joueurB.getPerso().isLeft=false;joueurB.getPerso().isRight=false;break;
            case 1: joueurB.getPerso().isLeft=false;joueurB.getPerso().isRight=true;break;
        }
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



}