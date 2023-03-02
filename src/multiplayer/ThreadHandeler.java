package multiplayer;

import java.io.DataOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ThreadHandeler implements Runnable {

    JoueurConnecte client;
    int pos;

    ThreadHandeler(JoueurConnecte j,int p){
        client=j;
        pos=p;
    }

    public volatile boolean start=false;
    
    public void run(){
        ObjectOutputStream out=null;
        System.out.println("Serveur.enclosing_method() before while");
        try {
            out =  new ObjectOutputStream(client.serveur.getOutputStream());
            while(!start){
                out.writeBoolean(!start);
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Serveur.enclosing_method() after while");
        try {
            System.out.println("Serveur.enclosing_method() inside try");
            out.writeBoolean(start);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        System.out.println("ThreadHandeler.run() fin de la thread");
    }   
}
