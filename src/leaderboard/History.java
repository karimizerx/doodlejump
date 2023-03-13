package leaderboard;

// Import de packages java
import java.io.*;

// Un classement est un objet défini par une liste de tableaux de données.
public class History extends LeaderBoard {

    public History() {
        // Initialisation des variables
        super("history.csv");
    }

    // Ajout d'une ligne au classement
    public void ajoutClassement(String id, String nom, String score) throws IOException {
        // On ajoute un nouveau tableau de donnée à la liste
        String[] newClassement = { id, nom, score };
        this.getLbData().add(newClassement);

        // On trie le classement

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

    // Affichage dans la console
    public void afficherClassement() throws IOException {
        System.out.println("########## HISTORIQUE DE SCORE (LOCAL) ##########");
        super.afficherLbData(this.getLbData());
    }
}