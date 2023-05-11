package multiplayer;

// Import de packages java :
import java.net.*;
import java.io.*;
import javax.swing.*;

// Import d'autres dossiers :
import gameobjects.*;

public class Serveur {

    private ServerSocket serveurSocket;
    private JoueurConnecte serveur;

    public Serveur() throws IOException { // initialise et ouvre un serveur au quelle on peut se cconnecter
        this.serveurSocket = new ServerSocket(0, 1);

        /**
         * 0 veut dire que le constructeur choisit automatiquement le port,
         * 1 c'est le backlog(nombre de connection autorisé), nous on veut seulement une
         * connection qui est l'autre joueur.
         * On utilisera getLocalPort() pour voir le numero du port afin de le
         * communiquer a l'autre joueur
         * Si, apres 30 secondes, personne ne se connecte,il y a un timeout error qu'on
         * attrape et le programme s'arrete.
         */
        this.serveurSocket.setSoTimeout(30000);
    }

    public void sendTerrain(Terrain terrain) {// envoyer les coordonnees du jeu
        ObjectOutputStream in;
        try {
            in = new ObjectOutputStream(serveur.client.getOutputStream());
            in.writeObject(terrain.isMultiDone());
            in.writeObject(terrain.getPlateformesListe());
            in.writeObject(terrain.getListeJoueurs().get(0));
            in.writeObject(terrain.getMontresArrayList());
            in.writeObject(terrain.getDiff_plateformes());
            in.writeObject(terrain.getDifficulty());;
            in.writeObject(terrain.getCoins());
            in.writeObject(terrain.isPause());
        } catch (Exception e) {
            e.printStackTrace();
            terrain.setMultiDone(true);
        }
    }

    public void fermerLeServeur() throws IOException {
        this.serveurSocket.close();
    }

    public void connect() {
        try {
            JOptionPane.showMessageDialog(null, "Nom du serveur :"+InetAddress.getLocalHost()+ "\n"+ "Le numero du port est :" + serveurSocket.getLocalPort(),"Important",JOptionPane.INFORMATION_MESSAGE); 
            serveur = new JoueurConnecte();
            serveur.setClient(serveurSocket.accept());
            System.out.println(serveurSocket==null);
            System.out.println(serveur==null);
            // JOptionPane.showMessageDialog(null,"Le joueur " + serveur.client.getRemoteSocketAddress() + " est connecté", "Succes",JOptionPane.DEFAULT_OPTION);
        } catch (IOException e) {
            this.serveur = null;
            System.out.println("Serveur.connect() failed");
            // JOptionPane.showMessageDialog(null, "Aucun joueur n'a essayé pas de se connecter", "Erreur",JOptionPane.ERROR_MESSAGE);// A implementer sur l'interface
        }
    }

    JoueurConnecte getServeur() {
        return serveur;
    }

    public void getJoueurB(Terrain i) {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(serveur.client.getInputStream());
            i.getListeJoueurs().set(1,(Joueur)in.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            System.out.println("classe perdu");
        }
    }
}