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
    public int id;
    public String name="";
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
    public void connecter(String name,String ServerName,int port){
        this.name=name;
        System.out.println("JoueurConnecte.connecter() before try");
        try {
            this.serveur=new Socket(ServerName,port);   
            System.out.println("JoueurConnecte.connecter() success"); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Echec de connexion","Erreur",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(-1);
        }
        // getId();
        System.out.println("JoueurConnecte.connecter()");
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
            terrain.isEsc=(boolean)in.readObject();
            terrain.isMenu=(boolean)in.readObject();
            terrain.pause=(boolean)in.readObject();
        }catch (ClassNotFoundException c){
            c.printStackTrace();
            System.out.println("classe perdu");
        } catch (IOException e) {
            System.out.println("JoueurConnecte.receiveTerrain() catch, am I connected: "+serveur.isConnected());
            e.printStackTrace();
            // System.exit(-1);
        }
    }



    public Socket getServeur(){
        return serveur;
    }

    public void deconnecter() throws IOException{
        serveur.close();
    }

    public void sendJoueur(Joueur joueurB) {
        try{
            ObjectOutputStream output= new ObjectOutputStream(serveur.getOutputStream()) ;
            output.writeObject(joueurB);
            output.writeObject(id);
            System.out.println("JoueurConnecte.sendJoueur() reussi");
            }catch(IOException e){
                // e.printStackTrace();
                System.out.println("JoueurConnecte.sendJoueur() echoue");
            }
    }

    public void sendName(){
        DataOutputStream out;
        try {
            out=new DataOutputStream(serveur.getOutputStream());
            out.writeChars(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    
}
