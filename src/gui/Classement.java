package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Classement {

    private File fichier;
    private ArrayList<String> ligneCSV;
    private ArrayList<String[]> Classement;

    public Classement() {
        String chemin = (new File("gui/")).getAbsolutePath();
        fichier = new File(chemin + "/classement.csv");
        ligneCSV = new ArrayList<String>();
    }

    // Ajoute chaque ligne du fichier à la liste ligneCSV
    private void lireLigne() throws IOException {
        // BufferedReader est utilisée pour sa méthode readLine
        BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
        for (String ligne = lecteur.readLine(); ligne != null; ligne = lecteur.readLine()) {
            this.ligneCSV.add(ligne);
        }
    }
}
