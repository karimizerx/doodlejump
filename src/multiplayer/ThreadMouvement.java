package multiplayer;

import java.util.ArrayList;
import java.io.*;
import java.lang.reflect.Executable;
import java.net.SocketException;

import gameobjects.Joueur;
import gameobjects.Plateforme;
import gameobjects.Terrain;

public class ThreadMouvement implements Runnable {
    JoueurConnecte client;
    Terrain terrain;
    boolean host;
    
    public ThreadMouvement(Terrain court, JoueurConnecte client){
        this.terrain = court;
        this.client=client;
        host=true;
    }

    public volatile boolean running=true;
    public void run() {
        if(terrain.isHost){
            while(running){
                getPlayers(terrain.host);
                terrain.host.sendTerrain(terrain);
            }
        }else{
            ObjectInputStream in=null;
            ObjectOutputStream output=null;
            try{
                client.serveur.getOutputStream().flush();
                output= new ObjectOutputStream(client.serveur.getOutputStream()) ;
                in = new ObjectInputStream(client.serveur.getInputStream());
            }catch(java.io.StreamCorruptedException c){
                c.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            while(running){            
                // terrain.client.sendJoueur(terrain.getMyPlayer());//le client envoi le mvt de son joueur
                try{
                    output.writeObject(terrain.getMyPlayer());
                    output.writeObject(client.id);
                    // System.out.println("JoueurConnecte.sendJoueur() reussi");
                    }catch(IOException e){
                        // e.printStackTrace();
                        System.out.println("JoueurConnecte.sendJoueur() echoue");
                    }
                try {
                    terrain.isEsc=(boolean)in.readObject();
                    terrain.isMenu=(boolean)in.readObject();
                    terrain.pause=(boolean)in.readObject();
                    terrain.setPlateformesListe((ArrayList<Plateforme>)in.readObject());
                    terrain.setJoueur((ArrayList<Joueur>)in.readObject());
                    in.reset();
                }catch (StreamCorruptedException s) {
                    System.out.println(s.getMessage());
                    s.printStackTrace();
                    
                }catch (ClassNotFoundException c){
                    c.printStackTrace();
                    System.out.println("classe perdu");
                }catch(SocketException e){
                    System.out.println("JoueurConnecte.receiveTerrain()");
                    System.out.println("JoueurConnecte.receiveTerrain() catch, am I connected: "+client.serveur.isConnected()+" am i closed: "+client.serveur.isClosed()+" am i isInputShutdown:"+client.serveur.isInputShutdown());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                terrain.client.receiveTerrain(terrain);        
            }
        }
    }


    void getPlayers(Serveur serveur) {
        ArrayList<Joueur> list=new ArrayList<Joueur>();
        list.add(terrain.getMyPlayer());
        for(int i=1;i<terrain.getListeJoueurs().size()-1;i++){
            list.add(terrain.getHost().getJoueur(i));
        }
        terrain.setJoueur(list);
    }
    
}