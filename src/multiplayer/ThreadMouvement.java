package multiplayer;


import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Terrain;

public class ThreadMouvement implements Runnable {
    // Serveur serveur;
    // JoueurConnecte client;
    Terrain terrain;
    public ThreadMouvement(Terrain court){
        this.terrain = court;
        // this.serveur= court.host;
        // this.client=court.client;
    }
    public volatile boolean running=true;
    public void run() {
        while(running){
            if(terrain.isHost){
                Serveur serveur=terrain.host;
                serveur.sendTerrain(terrain);
                getPlayers(serveur);
                // System.out.println("ThreadMouvement.run() done");
                // serveur.printStatus();
            }else{
                terrain.client.sendJoueur(terrain.getMyPlayer());//le client envoi le mvt de son joueur
                terrain.client.receiveTerrain(terrain);    
                // System.out.println("ThreadMouvement.run() done");
            }
        }
    }


    private void getPlayers(Serveur serveur) {
        ArrayList<Joueur> list=new ArrayList<Joueur>();
        list.add(terrain.getMyPlayer());
        for(int i=1;i<terrain.getListeJoueurs().size()-1;i++){
            list.add(terrain.getHost().getJoueur(i));
        }
        terrain.setJoueur(list);
    }
    
}