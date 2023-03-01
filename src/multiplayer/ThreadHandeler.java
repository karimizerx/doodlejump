package multiplayer;

import java.io.DataOutputStream;

public class ThreadHandeler implements Runnable {

    JoueurConnecte client;
    int pos;

    ThreadHandeler(JoueurConnecte j,int p){
        client=j;
        pos=p;
    }

    public volatile boolean start=false;
    
    public void run(){
        DataOutputStream out;
        System.out.println("Serveur.enclosing_method() before while");
        while(!start){
            // System.out.println("Serveur.enclosing_method() in while");
            // System.out.println("nombre total de joueur " +clients.size());
            // System.out.println("Serveur.enclosing_method() in for");
            try {
                out =  new DataOutputStream(client.serveur.getOutputStream());
                // System.out.println("Serveur.enclosing_method() will send "+!start);
                out.writeBoolean(!start);
                // System.out.println("Serveur.enclosing_method() has sent "+!start);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            } 
    }
    System.out.println("Serveur.enclosing_method() after while");
        try {
            System.out.println("Serveur.enclosing_method() inside try");
            out =  new DataOutputStream(client.serveur.getOutputStream());
            out.writeBoolean(start);
            // out.writeInt(pos);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    System.out.println("ThreadHandeler.run() fin de la thread");
    }   
}
