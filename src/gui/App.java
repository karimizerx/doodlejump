package gui;

import java.awt.Color;
// Import de packages java
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

// Import de packages projet

// La classe App représente la fenêtre de jeu
public class App extends JFrame {
    final int width = 600;
    final int height = 933;

    public App() {
        // Création de la fenêtre
        super("Menu"); // Création de la fenêtre avec son titre
        // Définition de l'opération lorsqu'on ferme la fenêtre
        // Ici, revient à exécuter "this.dispose()" (ne ferme que la fenêtre actuelle )
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(width, height); // Définition de la taille part défaut
        // Positionne la fenêre au centre du conteneur null
        // (la fenêtre de l'ordinateur, le bureau en quelques sortes)
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout());

        ///
        this.setBackground(Color.BLACK);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Appliquer un look'n feel
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // Démarrer la fenêtre.
        App mw = new App();
        mw.setVisible(true);
    }
}