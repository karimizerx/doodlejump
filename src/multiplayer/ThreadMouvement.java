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

    public volatile boolean hasStarted=false; 

    public void run() {
        // while(!hasStarted){
        //     System.out.println("wait wait ");
        // }
        System.out.println("ehhhhhh");
        while (true) {
            if (terrain.isHost) {
                serveur.sendTerrain(terrain);
                serveur.getJoueurB(terrain.getListeJoueurs().get(1));// recevoir ce que le mvt du client
                // System.out.println("ThreadMouvement.run() done");
            } else {
                client.receiveTerrain(terrain);
                client.sendJoueurB(terrain.getListeJoueurs().get(1));// le client envoi le mvt de son joueur
                // System.out.println("ThreadMouvement.run() done");
            }
        }
    }

}