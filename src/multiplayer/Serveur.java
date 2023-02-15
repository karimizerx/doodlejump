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
    private ArrayList<JoueurConnecte> clients=new ArrayList<JoueurConnecte>();


    public Serveur(){ 
        serveurSocket=null;
    }
    public String[] start() throws IOException{
        // initialise et ouvre un serveur au quelle on peut se cconnecter
        this.serveurSocket=new ServerSocket(0,1);

        /** 0 veut dire que le constructeur choisit automatiquement le port, 
         *  1 c'est le backlog(nombre de connection autorisé), nous on veut seulement une connection qui est l'autre joueur.
         *  On utilisera getLocalPort() pour voir le numero du port afin de le communiquer a l'autre joueur
         * Si, apres 120 secondes, personne ne se connecte,il y a un timeout error qu'on attrape et le programme s'arrete.
         */
        serveurSocket.setSoTimeout(120000);
        System.out.println(InetAddress.getLocalHost());
        String[] tmp={ "port :"+serveurSocket.getLocalPort()," srv  :"+ InetAddress.getLocalHost()};
        return tmp;
    }

    public void accept(){
        try{
            clients.add(new JoueurConnecte(serveurSocket.accept(),clients.size()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String[] show(JoueurConnecte a){
        String[] tmp= {"Le joueur "+(a.serveur.getRemoteSocketAddress()) +" est connecté"," il y a "+clients.size()+" joueurs connectés"};
        return tmp;
    }

    public void sendTerrain(Terrain terrain){//envoyer les coordonnees du jeu
        ObjectOutputStream in;
        for(JoueurConnecte client : clients){
            try {
                in =  new ObjectOutputStream(client.serveur.getOutputStream());
                in.writeObject(terrain.getPlateformesListe());
                in.writeObject(terrain.getMyPlayer());
                in.writeObject(terrain.isEsc);
                in.writeObject(terrain.isMenu);
                in.writeObject(terrain.pause);

            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }

    protected void fermerLeServeur()throws IOException{
        serveurSocket.close();
    }

    public volatile boolean end=false; 

    @Override 
    public void run(){
        int c=1;
        try {  
            clients=new ArrayList<JoueurConnecte>();
            while (!end) {    
                JoueurConnecte a =new JoueurConnecte(serveurSocket.accept(),c++);
                clients.add(a);
                System.out.println("Serveur.run()");
                DataOutputStream out;
                try{
                    out=new DataOutputStream(a.serveur.getOutputStream());
                    out.writeInt(c);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }                
            } catch (IOException e) { 
            JOptionPane.showMessageDialog(null,"Aucun joueur n'a essayé pas de se connecter","Erreur",JOptionPane.ERROR_MESSAGE);// A implementer sur l'interface
            System.exit(-1);
        }  
    }



    public ArrayList<String> getNames(){
        ArrayList<String> l=new ArrayList<String>();
        for(JoueurConnecte j:clients){
            DataInputStream in;
            try{
                in=new DataInputStream(j.serveur.getInputStream());
                l.add(in.readUTF());
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return l;
    }

    public volatile boolean start=false;
    
    public Thread commence= new Thread(new Runnable() {
            public void run(){
                DataOutputStream in;
                while(!start){
                for(JoueurConnecte client : clients){
                    try {
                        in =  new DataOutputStream(client.serveur.getOutputStream());
                        in.writeBoolean(start);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } 
                }
            }
            for(JoueurConnecte client : clients){
                try {
                    in =  new DataOutputStream(client.serveur.getOutputStream());
                    in.writeBoolean(start);
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        }
        });
    


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