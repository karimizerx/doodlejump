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
                terrain.setJoueurB(serveur.getJoueurB());//recevoir ce que le mvt du client
                System.out.println("ThreadMouvement.run() done");
            }else{
                client.receiveTerrain(terrain);    
                client.sendJoueurB(terrain.getJoueurB());//le client envoi le mvt de son joueur
                System.out.println("ThreadMouvement.run() done");
            }
        }
    }
    
}