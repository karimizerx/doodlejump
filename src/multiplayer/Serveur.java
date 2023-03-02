package multiplayer;



import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.io.*;
import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Personnage;
import gameobjects.Terrain;

public class Serveur{

    private ServerSocket serveurSocket;
    public ArrayList<JoueurConnecte> clients=new ArrayList<JoueurConnecte>();

    public Serveur(){ 
        serveurSocket=null;
    }
    public String[] start() throws IOException{
        // initialise et ouvre un serveur au quelle on peut se cconnecter
        this.serveurSocket=new ServerSocket(0);
        /** 0 veut dire que le constructeur choisit automatiquement le port, 
         *  On utilisera getLocalPort() pour voir le numero du port afin de le communiquer a l'autre joueur
         * Si, apres 120 secondes, personne ne se connecte,il y a un timeout error qu'on attrape et le programme s'arrete.
         */
        serveurSocket.setSoTimeout(2000);
        System.out.println(InetAddress.getLocalHost());
        String[] tmp={ "port :"+serveurSocket.getLocalPort()," srv :"+ InetAddress.getLocalHost()};
        return tmp;
    }
/*
    public void accept(){
        try{
            JoueurConnecte a=new JoueurConnecte();
            System.out.println("Serveur.accept() avant");
            a.setServeur(serveurSocket.accept());
            a.id=clients.size();
            System.out.println("Serveur.accept() apres");
            clients.add(a);
            // clients.add(new JoueurConnecte(serveurSocket.accept(),clients.size()));
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Serveur.accept(), failed");
        }
    }
*/
    public String[] show(JoueurConnecte a){
        String[] tmp= {"Le joueur "+(a.serveur.getRemoteSocketAddress()) +" est connecté"," il y a "+clients.size()+" joueurs connectés"};
        return tmp;
    }

    public void sendTerrain(Terrain terrain){//envoyer les coordonnees du jeu
        ObjectOutputStream in;
        for(JoueurConnecte client : clients){
            try {
                in =  new ObjectOutputStream(client.serveur.getOutputStream());
                in.writeObject(terrain.isEsc);
                in.writeObject(terrain.isMenu);
                in.writeObject(terrain.pause);
                in.writeObject(terrain.getPlateformesListe());
                in.writeObject(terrain.getListeJoueurs());

            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }

    public void fermerLeServeur()throws IOException{
        serveurSocket.close();
    }

    public volatile boolean end=false; 

    public Callable<Integer> callable=new Callable<Integer>(){
        @Override
        public Integer call(){
            int c=0;
            int waiting=0;
            ArrayList<ThreadHandeler> l=new ArrayList<ThreadHandeler>();
            try {  
                clients=new ArrayList<JoueurConnecte>();
                while (!end) {    
                    System.out.println("Serveur.run() 0");
                    JoueurConnecte a;
                    System.out.println("Serveur.accept() avant");
                    Socket sock=null;
                    try{
                        System.out.println("Serveur.callable.new Callable() {...}.call() dans le try ");
                        sock=serveurSocket.accept();
                        System.out.println("Serveur.callable.new Callable() {...}.call() apres le accept() ");
                        //TODO: probleme est ici, [SOLVED]
                        a=new JoueurConnecte(sock,c++);
                        System.out.println("Serveur.accept() apres");
                        clients.add(a);
                        l.add(new ThreadHandeler(a,c));
                        new Thread(l.get(l.size()-1)).start();
                        // accept();
                        // sendInt(c, a);
                        System.out.println("clients added, c="+c);    
                    }catch(IOException io){
                        waiting+=20;
                        if(waiting >2000) throw new Exception("waited too long");
                    }
                }                
            }catch (Exception e) { 
                JOptionPane.showMessageDialog(null,"Aucun joueur n'a essayé pas de se connecter, "+e.getMessage(),"Erreur",JOptionPane.ERROR_MESSAGE);// A implementer sur l'interface
                System.exit(-1);
            } 
            for (ThreadHandeler thread : l) {
                System.out.println("thread nb " + thread.pos);
                thread.start=true;
            }
            System.out.println("le serveur est closed? :"+(serveurSocket.isClosed())); 
            return c;
        }
    };
    private void sendInt(int c, JoueurConnecte a) {
        DataOutputStream out;
        try{
            System.out.println("Serveur.run() "+c);
            out=new DataOutputStream(a.serveur.getOutputStream());
            out.writeInt(c);
        }catch(IOException e){
            e.printStackTrace();
        }
    }



    public ArrayList<String> getNames(){
        ArrayList<String> l=new ArrayList<String>();
        int i=1;
        for(JoueurConnecte j:clients){
            // DataInputStream in;
            // try{
                //TODO: fix 
                // in=new DataInputStream(j.serveur.getInputStream());
                // l.add(in.readUTF());
            // }catch(IOException e){
            //     e.printStackTrace();
            // }
                l.add("joueur "+i);i++;
        }
        return l;
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

    void printStatus(){
        System.out.println("le serveur est closed ??"+(serveurSocket.isClosed())); 
    }




}