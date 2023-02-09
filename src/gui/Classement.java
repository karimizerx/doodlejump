package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Classement {

    private File fichier;
    private ArrayList<String> ligneCSV;
    private ArrayList<String[]> classement;

    public Classement() {
        String chemin = (new File("gui/")).getAbsolutePath();
        this.fichier = new File(chemin + "/classement.csv");
        this.ligneCSV = new ArrayList<String>();
        this.classement = new ArrayList<String[]>();
    }

    public void lectureFicher() throws IOException {
        /// Ajout chaque ligne du fichier à la liste ligneCSV
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
        afficherClassement();
        afficherClassement();
        afficherClassement();
    }

    private void afficherClassement() {
        System.out.println("########## CLASSEMENT ##########");
        System.out.println("Nom : Score");
        for (String[] tab : classement) {
            String nom = tab[0];
            String score = tab[1];
            System.out.println(nom + " : " + score);
        }
        System.out.println("################################\n");
    }

    public void ecritureFichier() {
    }

    public File getFichier() {
        return fichier;
    }

    public void setFichier(File fichier) {
        this.fichier = fichier;
    }

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

    public static void main(String[] args) throws IOException {
        Classement lb = new Classement();
        lb.lectureFicher();
    }
}