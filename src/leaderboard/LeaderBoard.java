package leaderboard;

// Import de packages java
import java.io.*;
import java.util.ArrayList;

// Un LeaderBoard est un objet défini pour stocker les données de score.
public abstract class LeaderBoard {

    private final String separateur;
    private String entete;
    private File fichier;
    private ArrayList<String> ligneCSV;
    private ArrayList<String[]> classement;
    private String chemin;

    public LeaderBoard(String nomFichier) {
        // On initialise les attributs
        this.separateur = ",";
        this.chemin = (new File("leaderboard/")).getAbsolutePath();
        this.ligneCSV = new ArrayList<String>();
        this.classement = new ArrayList<String[]>();

        // Double try_catch pour gérer la portabilité sur Windows
        try {
            try {
                this.fichier = new File(chemin + "/" + nomFichier);
            } catch (Exception e) {
                this.fichier = new File("src/leaderboard/" + nomFichier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sert à lire et enregistrer les données du fichier
    abstract void lectureFicher() throws IOException;

    // Ajout d'une ligne au classement
    abstract void ajoutClassement(String nom, String score) throws IOException;

    /// Protected car visibles que par les classes
    // Trient une ArrayList par ordre décroissant
    protected String[] getMaxScore(ArrayList<String[]> cl) {
        String[] max = cl.get(0);
        for (String[] tab : cl) {
            max = (Integer.valueOf(tab[1]) > Integer.valueOf(max[1])) ? tab : max;
        }
        return max;
    }

    protected int getMaxIndex(ArrayList<String[]> cl) {
        String[] max = cl.get(0);
        int index = 0;
        for (int i = 0; i < cl.size(); ++i) {
            index = (Integer.valueOf(cl.get(i)[1]) > Integer.valueOf(max[1])) ? i : index;
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
    protected void afficherClassement(ArrayList<String[]> cl) throws IOException {
        System.out.println("########## CLASSEMENT ##########");
        for (int i = 0; i < cl.size(); ++i) {
            String nom = cl.get(i)[0];
            String score = cl.get(i)[1];
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

    public ArrayList<String[]> getClassement() {
        return classement;
    }

    public void setClassement(ArrayList<String[]> classement) {
        this.classement = classement;
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
}