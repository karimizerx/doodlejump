package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.nimbus.*;

import gameobjects.*;

public class App extends JFrame {

    public App() {
        // Création de la fenêtre
        super("Menu"); // Création de la fenêtre avec son titre
        // Définition de l'opération lorsqu'on ferme la fenêtre
        // Ici, revient à exécuter "this.dispose()" (ne ferme que la fenêtre actuelle )
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000, 700); // Définition de la taille part défaut
        // Positionne la fenêre au centre du conteneur null
        // (la fenêtre de l'ordinateur, le bureau en quelques sortes)
        this.setLocationRelativeTo(null);

        this.setLayout(new FlowLayout());

        JPanel win = (JPanel) this.getContentPane();

        win.add(new JLabel("ALOOOOOOO"));
    }

    public static void main(String[] args) {
        System.out.println("Aloooo");
        // Appliquer un look'n feel
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // Démarrer la fenêtre.
        App mw = new App();
        mw.setVisible(true);
        System.out.println("Alooooooooooooooooooooo");
    }
}