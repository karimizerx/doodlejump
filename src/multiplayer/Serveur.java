package multiplayer;



import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Personnage;
import gameobjects.Terrain;

public class Serveur implements Runnable {

    private ServerSocket serveurSocket;
    private ArrayList<JoueurConnecte> clients;


    public Serveur(int i)throws IOException{ // initialise et ouvre un serveur au quelle on peut se cconnecter
        this.serveurSocket=new ServerSocket(0,1);
        
        /** 0 veut dire que le constructeur choisit automatiquement le port, 
         *  1 c'est le backlog(nombre de connection autorisé), nous on veut seulement une connection qui est l'autre joueur.
         *  On utilisera getLocalPort() pour voir le numero du port afin de le communiquer a l'autre joueur
         * Si, apres 120 secondes, personne ne se connecte,il y a un timeout error qu'on attrape et le programme s'arrete.
         */
        serveurSocket.setSoTimeout(120000);
    }
    


    public void sendTerrain(Terrain terrain){//envoyer les coordonnees du jeu
        ObjectOutputStream in;
        for(JoueurConnecte client : clients){
            try {
                in =  new ObjectOutputStream(client.serveur.getOutputStream());
                in.writeObject(terrain.getPlateformesListe());
                in.writeObject(terrain.getMyPlayer());
                in.writeObject(terrain.getY());
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }

    protected void fermerLeServeur()throws IOException{
        serveurSocket.close();
    }


    public int run(){
        try {  
            int c=0;
            while(true){   
                JOptionPane.showMessageDialog(null, "Le numero du port est :"+serveurSocket.getLocalPort()+"\n le nom du serveur est :"+ InetAddress. getLocalHost(),"Important",JOptionPane.INFORMATION_MESSAGE);
                clients=new ArrayList<JoueurConnecte>();
                JoueurConnecte a =new JoueurConnecte(serveurSocket.accept(),c);
                JOptionPane.showMessageDialog(null, "Le joueur "+a.serveur.getRemoteSocketAddress() +" est connecté, il y a "+c+" joueurs connectés","Succes",JOptionPane.DEFAULT_OPTION); 
                ++c;
            }
            return c;
        } catch (IOException e) { 
            JOptionPane.showMessageDialog(null,"Aucun joueur n'a essayé pas de se connecter","Erreur",JOptionPane.ERROR_MESSAGE);// A implementer sur l'interface
            System.exit(-1);
        }  
    }



    public Joueur getJoueur(int c){
        ObjectInputStream in;
        try {
            in =  new ObjectInputStream(clients.get(c).serveur.getInputStream());
            Joueur i=(Joueur)in.readObject();
            return  i;
        } catch (IOException e) {
            e.printStackTrace();
            return new Joueur(new Personnage(50, 50, 100, 100, -10),"Erreur");
        }catch (ClassNotFoundException classe){
            classe.printStackTrace();
        }
        System.exit(-1);
        return null;
    }

    





}