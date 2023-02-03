package gui;

import javax.swing.*;
import java.awt.*;

import javax.swing.plaf.nimbus.*;

import gameobjects.*;

public class App extends JFrame {
    JFrame DoodleJumpheur;
    JPanel menu;
    JButton buttonPlay, buttonMulti, buttonLeaderboard, buttonExit;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width; // Largeur de l'écran"
    int height = Toolkit.getDefaultToolkit().getScreenSize().height; // Longueur de l'écran

    public App() {
        /// Création de la fenêtre

        // Création de la fenêtre avec son titre
        super("Menu");
        // Action en cas de X : fermer de toutes les fenêtres + fin programme
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Définition de la taille
        this.setSize(new Dimension(600, 933));
        // Empêche la fenetre d'être redimensionée
        this.setResizable(false);
        // Placement de la fenêtre au centre du bureau (null)
        this.setLocationRelativeTo(null);
        // Layout null : les éléments sont placés manuellement
        this.setLayout(null);

        /// Création des éléments
        menu = createMenu();

        /// Gestion d'évenement
        gestionEvent();

        // Ajout des éléments à la fenêtre
        this.add(menu);
        int mpw = (int) menu.getPreferredSize().getWidth();
        int mph = (int) menu.getPreferredSize().getHeight();
        menu.setBounds((this.getWidth() / 2) - (mpw / 2), (this.getHeight() / 2) - mph, mpw, mph);
    }

    private JPanel createMenu() {
        // Initalisation du panel
        JPanel m = new JPanel();
        m.setLayout(new GridLayout(4, 1));
        m.setPreferredSize(new Dimension(150, 150));

        // Initialisation des élements
        buttonPlay = new JButton("Jouer Solo");
        buttonPlay.setPreferredSize(new Dimension(100, 100));
        buttonMulti = new JButton("Mode Multi-Joueurs");
        buttonMulti.setPreferredSize(new Dimension(150, 100));
        buttonLeaderboard = new JButton("Leaderboard");
        buttonLeaderboard.setPreferredSize(new Dimension(100, 100));
        buttonExit = new JButton("Quitter");
        buttonExit.setPreferredSize(new Dimension(100, 100));

        // AJout des élements dans le panel
        m.add(buttonPlay);
        m.add(buttonMulti);
        m.add(buttonLeaderboard);
        m.add(buttonExit);

        return m;
    }

    private JFrame createDJ() {
        // Initalisation de la fenêtre
        JFrame DJ = new JFrame();
        DJ.setTitle("Doodle Jumpheur");
        // Définition de la taille de cette fenêtre de jeu
        DJ.setSize(600, 933);
        // Empêche la fenetre d'être redimensionée
        DJ.setResizable(false);
        // Action à effectuer en cas de fermeture : fermer uniquement de cette fenêtre
        DJ.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Placement de la fenêtre au centre du bureau (null)
        DJ.setLocationRelativeTo(null);
        // Fenêtre pas visible tant que l'on a pas appuyé sur play
        DJ.setVisible(false);

        // Initialisation des éléments
        Personnage p = new Personnage(DJ.getWidth() / 2, DJ.getHeight() - 100, 100, 100, -10);
        Joueur j = new Joueur(p);
        Terrain rt = new Terrain(j, DJ.getHeight(), DJ.getWidth());

        // Ajout des éléments à la fenêtre
        DJ.add(new Vue(rt));

        return DJ;
    }

    private JPanel createLb() {
        // Initalisation du panel
        JPanel lb = new JPanel();
        lb.setPreferredSize(new Dimension(150, 150));

        // Initialisation des élements
        JLabel champion = new JLabel("Elyo le Roi");

        // AJout des élements dans le panel
        lb.add(champion);

        return lb;
    }

    private void gestionEvent() {
        buttonPlay.addActionListener(e -> {
            DoodleJumpheur = createDJ();
            DoodleJumpheur.setVisible(true);
            this.dispose();
        });

        buttonExit.addActionListener(e -> {
            System.exit(0);

        });

        buttonLeaderboard.addActionListener(e -> {
            JPanel lb = createLb();
            menu = createLb();
            repaint();
            validate();
            int lpw = (int) lb.getPreferredSize().getWidth();
            int lph = (int) lb.getPreferredSize().getHeight();
            lb.setBounds(300, 300, 150, 150);
        });
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