package leaderboard;

// Import de packages java
import java.io.*;
import java.util.ArrayList;

// Un LeaderBoard est un objet défini pour stocker les données de score.
public abstract class LeaderBoard {

    private final String separateur = ",";
    private String entete;
    private File fichier;
    private ArrayList<String> ligneCSV;
    private ArrayList<String[]> classement;
    private String chemin = (new File("gui/")).getAbsolutePath();

    public LeaderBoard(String nomFichier) {

        // On initialise les attributs
        this.ligneCSV = new ArrayList<String>();
        this.classement = new ArrayList<String[]>();

        // Double try_catch pour gérer la portabilité sur Windows
        try {
            try {
                this.fichier = new File(chemin + "/" + nomFichier);
            } catch (Exception e) {
                this.fichier = new File("src/gui/" + nomFichier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sert à lire et enregistrer les données du fichier
    abstract void lectureFicher();

    // Classes qui trient une ArrayList
    // protected car visibles que par les classes
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

    // Range le classement par ordre décroissant
    protected void classer(ArrayList<String[]> cl) throws IOException {
        ArrayList<String[]> lb = new ArrayList<String[]>();
        while (cl.size() != 0) {
            lb.add(getMaxScore(cl));
            cl.remove(getMaxIndex(cl));
        }
        cl = lb;
    }

    // Ajout d'une ligne au classement
    abstract void ajoutClassement(String nom, String score) throws IOException;

    // Affichage dans la console
    public void afficherClassement() throws IOException {
        System.out.println("########## CLASSEMENT ##########");
        for (int i = 0; i < classement.size(); ++i) {
            String nom = classement.get(i)[0];
            String score = classement.get(i)[1];
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
}