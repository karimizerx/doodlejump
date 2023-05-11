package multiplayer;

// Import de packages java :
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

// Import d'autres dossiers :
import gameobjects.*;

/* On ne calcule plus rien sur la machine du joueur qui se connecte. Le host calcule tout et passe l'information.  
* On enverra seulement l'etat de la raquette (Going_UP,Going_DOWN,IDLE)
*/

//Joueur qui se connecte vers le Serveur qui, dans notre cas, sera le joueur qui host  
public class JoueurConnecte {

    Socket client; // Socket qui sera initialisé avec ServerName et port

    public JoueurConnecte() {
        this.client = null;
    }

    protected void setClient(Socket a) {
        if (null == this.client)
            this.client = a;
    }

    /**
     * @param ServerName nom du serveur
     * @param port       port au quelle le socket va se connecter
     * @true Si le joueur est connecté
     */
    public void connecter() {
        try {
            String ServerName = JOptionPane.showInputDialog("Nom du Serveur");
            int port = Integer.parseInt(JOptionPane.showInputDialog("Port"));
            this.client = new Socket(ServerName, port);
            System.out.println(client==null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Echec de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * @return tableau qui contient l'etat de la partie, la position de la raquette
     *         du host, coordonnées de la balle.
     */
    public void receiveTerrain(Terrain terrain) {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(client.getInputStream());
            terrain.setMultiDone((boolean)in.readObject());
            terrain.setPlateformesListe((ArrayList<Plateforme>) in.readObject());
            terrain.getListeJoueurs().set(0,(Joueur)in.readObject());
            terrain.setMonstres((ArrayList<Monstre> )in.readObject());
            terrain.setDiff_plateformes((double)in.readObject());
            terrain.setDifficulty((double)in.readObject());
            terrain.setCoins((ArrayList<Coins> )in.readObject());
            terrain.setPause((boolean)in.readObject());
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            System.out.println("classe perdu");
        } catch (IOException e) {
            System.out.println("bug2");
            e.printStackTrace();
            terrain.setMultiDone(true);
        }
    }

    public void deconnecter() throws IOException {
        client.close();
    }

    public void sendJoueurB(Joueur joueurB) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(joueurB);
            // System.out.println("JoueurConnecte.sendJoueurB() reussi");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("JoueurConnecte.sendJoueurB() echoue");
        }
    }

}
