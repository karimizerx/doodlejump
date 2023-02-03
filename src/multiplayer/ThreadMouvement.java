package multiplayer;


import java.net.*;
import java.io.*;
import javax.swing.*;

import gameobjects.Terrain;

public class ThreadMouvement implements Runnable {
    Serveur serveur;
    JoueurConnecte client;
    Terrain terrain;
    BufferedReader in;
    public ThreadMouvement(Serveur serveur,JoueurConnecte client,Terrain court){
        this.serveur= serveur;
        this.client=client;
        this.terrain = court;
    }
    public void run() {
        while(true){
            if(terrain.isHost){

                terrain.setPlayerBmvt(terrain.getPlayerBmvt());
            }else{
                client.sendPos(terrain.getPlayerBmvt());
                terrain.setInfo()
                terrain=(Terrain)(new ObjectInputStream(client.client.getInputStream())).readObject();
            }
        }
    }
    
}