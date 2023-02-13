package multiplayer;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Plateforme;
import gameobjects.Terrain;

public class JoueurConnecte {
    
    Socket serveur; // Socket qui sera initialisé avec ServerName et port 
    int id;
    public JoueurConnecte(){
        this.serveur=null;
    }
    public JoueurConnecte(Socket serveur,int c){
        this.serveur=serveur;
        this.id=c;
    }

    protected void setServeur(Socket a){
        if(null==this.serveur)this.serveur=a;
    }
    /**
     * @param ServerName nom du serveur
     * @param port port au quelle le socket va se connecter
     * @true Si le joueur est connecté 
     */
    public boolean connecter(){
        try {
            String ServerName=JOptionPane.showInputDialog("Nom du Serveur");
            int port=Integer.parseInt(JOptionPane.showInputDialog( "Port"));
            
            this.serveur=new Socket(ServerName,port);  
            return true;   
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Echec de connexion","Erreur",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(-1);
            return false;
        }
    }



    /**
     * @return tableau qui contient l'etat de la partie, la position de la raquette du host, coordonnées de la balle.
     */
    public void receiveTerrain(Terrain terrain){
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(serveur.getInputStream());
            terrain.setPlateformesListe((ArrayList<Plateforme>)in.readObject());
            terrain.setJoueur((ArrayList<Joueur>)in.readObject());
            terrain.setY((double)in.readObject());                
        }catch (ClassNotFoundException c){
            c.printStackTrace();
            System.out.println("classe perdu");
        } catch (IOException e) {
            System.out.println("bug2");
            e.printStackTrace();
            System.exit(-1);
        }
    }





    protected void deconnecter() throws IOException{
        serveur.close();
    }

    public void sendJoueur(Joueur joueurB,int ID) {
        try{
            ObjectOutputStream output= new ObjectOutputStream(serveur.getOutputStream()) ;
            output.writeObject(joueurB);
            output.writeObject(ID);;
            System.out.println("JoueurConnecte.sendJoueurB() reussi");
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("JoueurConnecte.sendJoueurB() echoue");
            }
    }


    
}
