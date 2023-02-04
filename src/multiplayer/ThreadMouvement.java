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
        this.serveur= terrain.getHost();
        this.client=terrain.getClient();
    }
    public void run() {
            if(terrain.isHost){
                serveur.sendTerrain(terrain);
                terrain.setPlayerBmvt(serveur.getPos());//recevoir ce que le mvt du client
            }else{
                int i=terrain.getPlayerBmvt();
                client.sendPos(i);//le client envoi le mvt de son joueur
                client.receiveTerrain(terrain);    
            }

    }
    
}