package leaderboard;

// Import de packages java
import java.io.*;
import java.util.*;

// Un LeaderBoard est un objet défini pour stocker les données de score.
public abstract class LeaderBoard {

    private final String separateur;
    private String entete, chemin;
    private File fichier;
    private ArrayList<String> ligneCSV;
    private ArrayList<String[]> lbData;

    public LeaderBoard(String nomFichier) {
        // On initialise les attributs
        this.separateur = ",";
        this.chemin = (new File("leaderboard/")).getAbsolutePath();
        this.ligneCSV = new ArrayList<String>();
        this.lbData = new ArrayList<String[]>();

        // Double try_catch pour gérer la portabilité sur Windows
        try {
            try {
                this.fichier = new File(chemin + "/" + nomFichier);
                lectureFicher(); // Initialise les ArrayList sur lesquelles on travail & l'en-tête
            } catch (Exception e) {
                this.fichier = new File("src/leaderboard/" + nomFichier);
                lectureFicher(); // Initialise les ArrayList sur lesquelles on travail & l'en-tête
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ajout d'une ligne au classement
    abstract void ajoutClassement(String id, String nom, String score) throws IOException;

    // Sert à lire et enregistrer les données du fichier
    protected void lectureFicher() throws IOException {
        /// Ajout chaque ligne du fichier à la liste ligneCSV
        // La classe BufferedReader, jumelée à FileReader, permet de lire
        // des entrées de séquences de caractères.
        // BufferedReader est utilisée pour sa méthode readLine.
        BufferedReader lecteur = new BufferedReader(new FileReader(this.getFichier()));
        for (String ligne = lecteur.readLine(); ligne != null; ligne = lecteur.readLine()) {
            this.getLigneCSV().add(ligne);
        }
        this.setEntete(this.getLigneCSV().get(0)); // On récupère l'en-tête, on suppose que le fichier n'est jamais vide
        this.getLigneCSV().remove(0); // On retire l'en-tête

        lecteur.close(); // On libère les ressources

        /// Ajout de toutes les colonnes de chaque ligne à la liste classement
        for (String ligne : this.getLigneCSV()) { // On lit chaque ligne du fichier
            String[] ligneData = ligne.split(","); // Et on place les données de chaque colonne dans un tableau
            this.getLbData().add(ligneData);
        }
    }

    /// Protected car visibles que par les classes
    // Trient une ArrayList par ordre décroissant
    protected String[] getMaxScore(ArrayList<String[]> cl) {
        String[] max = cl.get(0);
        for (String[] tab : cl) {
            max = (Integer.valueOf(tab[2]) > Integer.valueOf(max[2])) ? tab : max;
        }
        return max;
    }

    protected int getMaxIndex(ArrayList<String[]> cl) {
        String[] max = cl.get(0);
        int index = 0;
        for (int i = 0; i < cl.size(); ++i) {
            index = (Integer.valueOf(cl.get(i)[2]) > Integer.valueOf(max[2])) ? i : index;
        }
        return index;
    }

    protected ArrayList<String[]> classer(ArrayList<String[]> cl) throws IOException {
        ArrayList<String[]> lb = new ArrayList<String[]>();
        while (cl.size() != 0) {
            lb.add(getMaxScore(cl));
            cl.remove(getMaxIndex(cl));
        }
        return lb;
    }

    // Affichage dans la console
    protected void afficherLbData(ArrayList<String[]> cl) throws IOException {
        for (int i = 0; i < cl.size(); ++i) {
            String nom = cl.get(i)[1];
            String score = cl.get(i)[2];
            System.out.println("#" + (i + 1) + " " + nom + " : " + score);
        }
        System.out.println("################################\n");
    }

    // Getter & Setter

    public ArrayList<String> getLigneCSV() {
        return ligneCSV;
    }

    public void setLigneCSV(ArrayList<String> ligneCSV) {
        this.ligneCSV = ligneCSV;
    }

    public String getSeparateur() {
        return separateur;
    }

    public String getEntete() {
        return entete;
    }

    public void setEntete(String entete) {
        this.entete = entete;
    }

    public File getFichier() {
        return fichier;
    }

    public void setFichier(File fichier) {
        this.fichier = fichier;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public ArrayList<String[]> getLbData() {
        return lbData;
    }

    public void setLbData(ArrayList<String[]> lbData) {
        this.lbData = lbData;
    }

}