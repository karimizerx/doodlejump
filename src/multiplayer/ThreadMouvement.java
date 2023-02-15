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

    public ThreadMouvement(Terrain court) {
        this.terrain = court;
        // this.serveur= court.host;
        // this.client=court.client;
    }

    public void run() {
        while (true) {
            if (terrain.isHost) {
                Serveur serveur = terrain.host;
                serveur.sendTerrain(terrain);
                ArrayList<Joueur> list = new ArrayList<Joueur>();
                list.add(terrain.getMyPlayer());
                for (int i = 1; i < terrain.getListeJoueurs().size(); i++) {
                    list.add(serveur.getJoueur(i));
                } // recevoir ce que le mvt des clients
                terrain.setJoueur(list);
                System.out.println("ThreadMouvement.run() done");
            } else {
                // Le client envoi le mvt de son joueur
                terrain.client.sendJoueur(terrain.getMyPlayer(), terrain.playerID);
                terrain.client.receiveTerrain(terrain);
                System.out.println("ThreadMouvement.run() done");
            }
        }
    }

}