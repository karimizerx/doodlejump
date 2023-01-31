package gui;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import javax.swing.plaf.nimbus.*;

import gameobjects.*;

public class App extends JFrame {

    static int w = 500;
    static int h = 1000;
    Vue view;

    public App() {
        // Création de la fenêtre
        super("Menu"); // Création de la fenêtre avec son titre
        // Définition de l'opération lorsqu'on ferme la fenêtre
        // Ici, revient à exécuter "this.dispose()" (ne ferme que la fenêtre actuelle )
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(w, h); // Définition de la taille part défaut

        // Positionne la fenêre au centre du conteneur null
        // (la fenêtre de l'ordinateur, le bureau en quelques sortes)
        this.setLocationRelativeTo(null);

        // Empêche la fenetre d'être redimensionée
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        JPanel win = (JPanel) this.getContentPane();
        win.setBackground(Color.BLACK);
        win.setOpaque(true);

        // win.add(new JLabel("ALOOOOOOO"));
        view.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        win.add(view);

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
        System.out.println(mw.getHeight());
        System.out.println("Alooooooooooooooooooooo");
    }
}