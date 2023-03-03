package leaderboard;

// Import de packages java
import java.io.*;

// Un classement est un objet défini par une liste de tableaux de données.
public class Classement extends LeaderBoard {

    public Classement() {
        // Initialisation des variables
        super("classement.csv");
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

    public int getMaxScoreGlobal() {
        int max = 0;
        for (int i = 0; i <= getMaxId(); ++i) {
            max = (max < getMaxScoreOfId(String.valueOf(i))) ? getMaxScoreOfId(String.valueOf(i)) : max;
        }
        return max;
    }

    // Affichage dans la console
    public void afficherClassement() throws IOException {
        System.out.println("########## CLASSEMENT GLOBAL ##########");
        super.afficherLbData(this.getLbData());
    }
}