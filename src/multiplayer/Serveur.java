package multiplayer;



import java.net.*;
import java.io.*;
import javax.swing.*;

import gameobjects.Terrain;

public class Serveur implements Runnable {

    private ServerSocket serveurSocket;
    private JoueurConnecte serveur;


    public Serveur()throws IOException{ // initialise et ouvre un serveur au quelle on peut se cconnecter
        this.serveurSocket=new ServerSocket(0,1);
        
        /** 0 veut dire que le constructeur choisit automatiquement le port, 
         *  1 c'est le backlog(nombre de connection autorisé), nous on veut seulement une connection qui est l'autre joueur.
         *  On utilisera getLocalPort() pour voir le numero du port afin de le communiquer a l'autre joueur
         * Si, apres 30 secondes, personne ne se connecte,il y a un timeout error qu'on attrape et le programme s'arrete.
         */
        serveurSocket.setSoTimeout(30000);
    }
    
    public int getPos(){
        DataInputStream in;
        try {
            in =  new DataInputStream(serveur.client.getInputStream());
            return  in.read();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } 
    }

    public void sendTerrain(Terrain terrain){//envoyer les coordonnees du jeu
        ObjectOutputStream in;
        try {
            in =  new ObjectOutputStream(serveur.client.getOutputStream());
            in.writeObject(terrain.getPlateformesListe());
            in.writeObject(terrain.getJoueurA());
            in.writeObject(terrain.getJoueurB());
            in.writeObject(terrain.getY());
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    protected void fermerLeServeur()throws IOException{
        serveurSocket.close();
    }


    public void run(){
        try {     
            JOptionPane.showMessageDialog(null, "Le numero du port est :"+serveurSocket.getLocalPort(),"Important",JOptionPane.INFORMATION_MESSAGE);
            serveur=new JoueurConnecte();
            serveur.setClient(serveurSocket.accept());
            JOptionPane.showMessageDialog(null, "Le joueur "+serveur.client.getRemoteSocketAddress() +" est connecté","Succes",JOptionPane.DEFAULT_OPTION); 
            System.out.println("sisi"); 
        } catch (IOException e) { 
            this.serveur=null;
            JOptionPane.showMessageDialog(null,"Aucun joueur n'a essayé pas de se connecter","Erreur",JOptionPane.ERROR_MESSAGE);// A implementer sur l'interface
        }  
    }


    JoueurConnecte getServeur(){
        return serveur;
    }

    





}