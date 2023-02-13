package multiplayer;


import java.net.*;
import java.io.*;
import javax.swing.*;

import gameobjects.Terrain;

public class ThreadMouvement implements Runnable {
    Serveur serveur;
    JoueurConnecte client;
    Terrain terrain;
    public ThreadMouvement(Terrain court){
        this.terrain = court;
        this.serveur= court.host;
        this.client=court.client;
    }
    public void run() {
        while(true){
            if(terrain.isHost){
                serveur.sendTerrain(terrain);
                terrain.setJoueur(serveur.getJoueurB(),serveur.getInt());//recevoir ce que le mvt du client
                System.out.println("ThreadMouvement.run() done");
            }else{
                client.sendJoueur(terrain.getMyPlayer(),terrain.playerID);//le client envoi le mvt de son joueur
                client.receiveTerrain(terrain);    
                System.out.println("ThreadMouvement.run() done");
            }
        }
    }
    
}