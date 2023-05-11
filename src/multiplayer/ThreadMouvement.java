package multiplayer;

// Import d'autres dossiers :
import gameobjects.*;

public class ThreadMouvement implements Runnable {
    Serveur serveur;
    JoueurConnecte client;
    Terrain terrain;

    public ThreadMouvement(Terrain court) {
        this.terrain = court;
        this.serveur = court.host;
        this.client = court.client;
    }
    
    public ThreadMouvement() {
        this.terrain =null;
        this.serveur =null;
        this.client =null;
    }

    public volatile boolean running=true; 

    public void run() {
        // while(!hasStarted){
        //     System.out.println("wait wait ");
        // }
        while (running) {
            if (terrain.isHost) {
                serveur.sendTerrain(terrain);
                serveur.getJoueurB(terrain);// recevoir ce que le mvt du client
                running=!terrain.isMultiDone();
            } else {
                client.receiveTerrain(terrain);
                client.sendJoueurB(terrain.getListeJoueurs().get(1));// le client envoi le mvt de son joueur
                running=!terrain.isMultiDone();
            }
        }
        System.out.println("ThreadMouvement.run() end");
    }

}