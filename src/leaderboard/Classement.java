package leaderboard;

// Import de packages java
import java.io.*;

// Un classement est un objet défini par une liste de tableaux de données.
public class Classement extends LeaderBoard {

    public Classement() {
        super("classement.csv");
        try { // Initialise les ArrayList sur lesquelles on travail & l'en-tête
            lectureFicher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sert à lire et enregistrer les données du fichier
    @Override
    void lectureFicher() throws IOException {
        /// Ajout chaque ligne du fichier à la liste ligneCSV
        // La classe BufferedReader, jumelée à FileReader, permet de lire
        // des entrées deséquences de caractères.
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

    // Ajout d'une ligne au classement
    public void ajoutClassement(String id, String nom, String score) throws IOException {
        // On ajoute un nouveau tableau de donnée à la liste
        String[] newClassement = { id, nom, score };
        this.getLbData().add(newClassement);

        // On trie le classement
        this.setLbData(super.classer(this.getLbData()));

        // La classe BufferedWriter, jumelée à FileWriter, permet d'écrire
        // des séquences de caractères dans le fichier.
        BufferedWriter writerB = new BufferedWriter(new FileWriter(this.getFichier()));
        /// On réécrit complètement le fichier
        writerB.append(this.getEntete()); // On rajoute l'en-tête
        // On utilise la méthode de BufferedWriter newLine()
        writerB.newLine(); // Le passage à une nouvelle ligne c'est le délimiteur
        for (String[] tab : this.getLbData()) { // Pour chaque ligne dans le classement
            writerB.append(tab[0]); // On ajoute l'identifiant du joueur
            writerB.append(this.getSeparateur()); // Le séparateur
            writerB.append(tab[1]); // On ajoute le nom du joueur
            writerB.append(this.getSeparateur()); // Le séparateur
            writerB.append(tab[2]); // Le Score du joueur
            writerB.newLine(); // Puis newLine();
        }

        writerB.close(); // On libère les ressources
    }

    public int getMaxId() {
        int maxId = Integer.valueOf(this.getLbData().get(0)[0]);
        for (String[] tabData : this.getLbData()) {
            maxId = (maxId < Integer.valueOf(tabData[0])) ? Integer.valueOf(tabData[0]) : maxId;
        }
        return maxId;
    }

    public int getMaxScoreOfId(String id) {
        int identifiant = Integer.valueOf(id);
        int maxScore = 0;
        if (identifiant > getMaxId()) {
            return -1;
        }
        for (String[] tabData : this.getLbData()) {
            if (Integer.valueOf(tabData[0]) == identifiant) {
                if (maxScore < Integer.valueOf(tabData[2])) {
                    maxScore = Integer.valueOf(tabData[2]);
                }
            }
        }
        return maxScore;
    }

    // Affichage dans la console
    public void afficherClassement() throws IOException {
        System.out.println("########## CLASSEMENT GLOBAL ##########");
        super.afficherClassement(this.getLbData());
    }
}