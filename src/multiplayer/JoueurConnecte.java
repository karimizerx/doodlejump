package multiplayer;

// Import de packages java
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

// Import d'autres dossiers
import gameobjects.*;

public class JoueurConnecte {

    Socket serveur; // Socket qui sera initialisé avec ServerName et port
    int id;
    String name = "";

    public JoueurConnecte() {
        this.serveur = null;
    }

    public JoueurConnecte(Socket serveur, int c) {
        this.serveur = serveur;
        this.id = c;
    }

    protected void setServeur(Socket a) {
        if (null == this.serveur)
            this.serveur = a;
    }

    /**
     * @param ServerName nom du serveur
     * @param port       port au quelle le socket va se connecter
     * @true Si le joueur est connecté
     */
    public void connecter(String name, String ServerName, int port) {
        this.name = name;
        try {
            this.serveur = new Socket(ServerName, port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Echec de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(-1);
        }
        DataInputStream in;
        try {
            in = new DataInputStream(serveur.getInputStream());
            id = in.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return tableau qui contient l'etat de la partie, la position de la raquette
     *         du host, coordonnées de la balle.
     */
    public void receiveTerrain(Terrain terrain) {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(serveur.getInputStream());
            terrain.setPlateformesListe((ArrayList<Plateforme>) in.readObject());
            terrain.setListeJoueurs((ArrayList<Joueur>) in.readObject());
            terrain.setPause((boolean) in.readObject());
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            System.out.println("classe perdu");
        } catch (IOException e) {
            System.out.println("bug2");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public Socket getServeur() {
        return serveur;
    }

    protected void deconnecter() throws IOException {
        serveur.close();
    }

    public void sendJoueur(Joueur joueurB, int ID) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(serveur.getOutputStream());
            output.writeObject(joueurB);
            // output.writeObject(ID);;
            System.out.println("JoueurConnecte.sendJoueurB() reussi");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("JoueurConnecte.sendJoueurB() echoue");
        }
    }

    public void sendName() {
        DataOutputStream out;
        try {
            out = new DataOutputStream(serveur.getOutputStream());
            out.writeChars(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
