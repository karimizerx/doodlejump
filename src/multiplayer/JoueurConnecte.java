package multiplayer;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Plateforme;
import gameobjects.Terrain;

/* On ne calcule plus rien sur la machine du joueur qui se connecte. Le host calcule tout et passe l'information.  
* On enverra seulement l'etat de la raquette (Going_UP,Going_DOWN,IDLE)
*/

//Joueur qui se connecte vers le Serveur qui, dans notre cas, sera le joueur qui host  
public class JoueurConnecte {
    
    Socket client; // Socket qui sera initialisé avec ServerName et port 
    
    public JoueurConnecte(){
        this.client=null;
    }

    protected void setClient(Socket a){if(null==this.client)this.client=a;}
    /**
     * @param ServerName nom du serveur
     * @param port port au quelle le socket va se connecter
     * @true Si le joueur est connecté 
     */
    public boolean connecter(){
        try {
            String ServerName=JOptionPane.showInputDialog("Nom du Serveur");
            int port=Integer.parseInt(JOptionPane.showInputDialog( "Port"));
            
            this.client=new Socket(ServerName,port);  
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
            in = new ObjectInputStream(client.getInputStream());
            terrain.setPlateformesListe((ArrayList<Plateforme>)in.readObject());
            terrain.setJoueurA((Joueur)in.readObject());
            terrain.setJoueurB((Joueur)in.readObject());
            terrain.setY((double)in.readObject());                
        } catch (IOException e) {
            System.out.println("bug2");
            e.printStackTrace();
        }catch (ClassNotFoundException c){
            c.printStackTrace();
            System.out.println("zebi");
        }
    }



    public void sendPos(double a){
        try{
        DataOutputStream output= new DataOutputStream(client.getOutputStream()) ;
        output.writeDouble(a);
        }catch(IOException e){
            e.printStackTrace();
        }
    }



    protected void deconnecter() throws IOException{
        client.close();
    }


    
}
