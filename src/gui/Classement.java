package gui;

// Import de packages java
import java.io.*;
import java.util.ArrayList;

// Un classement est un objet défini par une liste de tableaux de données.
public class Classement {

    private String entete = "Nom,Score";
    private final String separateur = ",";
    private File fichier;
    private ArrayList<String> ligneCSV;
    private ArrayList<String[]> classement;

    public Classement() {
        String chemin = (new File("gui/")).getAbsolutePath();
        this.fichier = new File(chemin + "/classement.csv");
        this.ligneCSV = new ArrayList<String>();
        this.classement = new ArrayList<String[]>();
        try {
            lectureFicher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sert à lire et enregistrer les données du fichier
    private void lectureFicher() throws IOException {
        /// Ajout chaque ligne du fichier à la liste ligneCSV
        // La classe BufferedReader, jumelée à FileReader, permet de lire des entrées de
        // séquences de caractères
        // BufferedReader est utilisée pour sa méthode readLine
        BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
        for (String ligne = lecteur.readLine(); ligne != null; ligne = lecteur.readLine()) {
            this.ligneCSV.add(ligne);
        }
        ligneCSV.remove(0); // On retire l'en-tête

        lecteur.close(); // On libère les ressources

        /// Ajout de toutes les colonnes de chaque ligne à la liste classement
        for (String ligne : ligneCSV) { // On lit chaque ligne du fichier
            String[] ligneData = ligne.split(","); // Et on place les données de chaque colonne dans un tableau
            classement.add(ligneData);
        }
    }

    private String[] getMaxScore() {
        String[] max = classement.get(0);
        for (String[] tab : classement) {
            max = (Integer.valueOf(tab[1]) > Integer.valueOf(max[1])) ? tab : max;
        }
        return max;
    }

    private int getMaxIndex() {
        String[] max = classement.get(0);
        int index = 0;
        for (int i = 0; i < classement.size(); ++i) {
            index = (Integer.valueOf(classement.get(i)[1]) > Integer.valueOf(max[1])) ? i : index;
        }
        return index;
    }

    // Range le classement par ordre décroissant
    private void classer() throws IOException {
        ArrayList<String[]> lb = new ArrayList<String[]>();
        while (classement.size() != 0) {
            lb.add(getMaxScore());
            classement.remove(getMaxIndex());
        }
        this.classement = lb;
    }

    // Ajout d'une ligne au classement
    public void ajoutClassement(String nom, String score) throws IOException {
        // On ajoute un nouveau tableau de donnée à la liste
        String[] newClassement = { nom, score };
        classement.add(newClassement);

        classer();

        // La classe BufferedWriter, jumelée à FileWriter, permet d'écrire des séquences
        // de charactères dans le fichier
        BufferedWriter writerB = new BufferedWriter(new FileWriter(fichier));
        /// On réécrit complètement le fichier
        writerB.append(this.entete); // On rajoute l'en-tête
        // On utilise la méthode de BufferedWriter newLine()
        writerB.newLine(); // Le passage à une nouvelle ligne c'est le délimiteur
        for (String[] tab : classement) { // Pour chaque ligne dans le classement
            writerB.append(tab[0]); // On ajoute le nom du joueur
            writerB.append(separateur); // Le séparateur
            writerB.append(tab[1]); // Le Score du joueur
            writerB.newLine(); // Puis newLine();
        }

        writerB.close(); // On libère les ressources
    }

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
}