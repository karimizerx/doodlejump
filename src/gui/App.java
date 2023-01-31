package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.nimbus.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

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
        // this.setResizable(false);
        this.setLayout(new FlowLayout());

        JPanel win = (JPanel) this.getContentPane();
        // win.setBackground(Color.BLACK);
        // win.setOpaque(true);

        // win.add(new JLabel("ALOOOOOOO"));
        String chemin = (new File("gui/images/")).getAbsolutePath();
        try {
            BufferedImage persoView = ImageIO.read(new File(chemin + "/" + "doodle.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Personnage doodle = new Personnage(100, 100, 100, 100, 0);
        Joueur j = new Joueur(doodle);
        Terrain leRainT = new Terrain(j, h, w);

        JPanel lavue = new Vue(leRainT);
        lavue.setPreferredSize(new Dimension(w, h));
        win.add(lavue);
    }

    public static void main(String[] args) {
        System.out.println("Aloooo");
        EventQueue.invokeLater(() -> {
            // Appliquer un look'n feel
            try {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            // Démarrer la fenêtre.
            App mw = new App();
            mw.setVisible(true);
        });
        System.out.println("Alooooooooooooooooooooo");
    }
}