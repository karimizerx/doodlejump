package leaderboard;

// Import de packages java
import java.io.*;
import java.util.ArrayList;

// Un classement est un objet défini par une liste de tableaux de données.
public class History extends LeaderBoard {

    public History(String nomFichier) {
        super(nomFichier);
        //TODO Auto-generated constructor stub
    }

    @Override
    void lectureFicher() {
        ArrayList<String> s = new ArrayList<String>();
        super.getMaxIndex(s);
        throw new UnsupportedOperationException("Unimplemented method 'lectureFicher'");
    }

    @Override
    void ajoutClassement(String nom, String score) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ajoutClassement'");
    }
}