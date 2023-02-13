package gui;

// Import de packages java
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.nimbus.*;
import java.io.IOException;
import java.util.ArrayList;

// Import des autres dossiers
import gameobjects.*;

// App est la fenêtre du menu démarrer
public class App extends JFrame {
    JFrame DoodleJumpheur; // La fenêtre de jeu
    JPanel menu, menu2; // Menu démarrer
    JButton buttonSolo, button2joueur, buttonMulti, buttonLeaderboard, buttonExit, buttonPlay;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width; // Largeur de l'écran
    int height = Toolkit.getDefaultToolkit().getScreenSize().height; // Longueur de l'écran
    int nbj; // Nombre de joueurs

    public App() {
        /// Création de la fenêtre

        // Création de la fenêtre avec son titre
        super("Menu");
        // Action en cas de X : fermer de toutes les fenêtres + fin programme
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Définition de la taille
        this.setSize(new Dimension(width / 3, (int) (height * 0.95)));
        // Empêche la fenetre d'être redimensionée
        this.setResizable(false);
        // Placement de la fenêtre au centre du bureau (null)
        this.setLocationRelativeTo(null);
        // Layout null : les éléments sont placés manuellement
        this.setLayout(null);

        /// Création des éléments
        menu = createMenu(); // Choix de l'action
        menu2 = createMenu2(); // Choix des joueurs
        buttonPlay = new JButton("Jouer"); // Bouton jouer
        buttonPlay.setPreferredSize(new Dimension(100, 100));

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
        m.setLayout(new GridLayout(5, 1));
        m.setPreferredSize(new Dimension(150, 150));

        // Initialisation des élements
        buttonSolo = new JButton("Jouer Solo");
        buttonSolo.setPreferredSize(new Dimension(100, 100));
        button2joueur = new JButton("Jouer à 2");
        button2joueur.setPreferredSize(new Dimension(100, 100));
        buttonMulti = new JButton("Mode Multi-Joueurs");
        buttonMulti.setPreferredSize(new Dimension(150, 100));
        buttonLeaderboard = new JButton("Classement");
        buttonLeaderboard.setPreferredSize(new Dimension(100, 100));
        buttonExit = new JButton("Quitter");
        buttonExit.setPreferredSize(new Dimension(100, 100));

        // AJout des élements dans le panel
        m.add(buttonSolo);
        m.add(button2joueur);
        m.add(buttonMulti);
        m.add(buttonLeaderboard);
        m.add(buttonExit);

        return m;
    }

    private JPanel createMenu2() {
        // Initalisation du panel
        JPanel m = new JPanel();
        m.setLayout(new GridLayout(nbj + 1, 0)); // +1 pour le bouton Play
        m.setPreferredSize(new Dimension(170, 50 * nbj));

        // Initialisation & ajout des élements dans le panel
        for (int i = 0; i < nbj; ++i) { // Si possible, on prend par défaut le nom du meilleur joueur
            Classement c = new Classement();
            String n;
            if (c.getClassement().size() > 1) {
                n = c.getClassement().get(0)[0];
            } else {
                n = "Entrez votre nom";
            }
            JTextArea nomjoueur = new JTextArea(n);
            nomjoueur.setPreferredSize(new Dimension(100, 100));
            m.add(nomjoueur);
        }

        return m;
    }

    private JFrame createDJ() {
        // Initalisation de la fenêtre
        JFrame DJ = new JFrame();
        DJ.setTitle("Doodle Jumpheur");
        // Définition de la taille de cette fenêtre de jeu
        DJ.setSize(width / 3, (int) (height * 0.95));
        // Empêche la fenetre d'être redimensionée
        DJ.setResizable(false);
        // Action à effectuer en cas de fermeture : fermer uniquement de cette fenêtre
        DJ.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Placement de la fenêtre au centre du bureau (null)
        DJ.setLocationRelativeTo(null);
        // Fenêtre pas visible tant que l'on a pas appuyé sur play
        DJ.setVisible(false);

        // Initialisation des éléments
        ArrayList<Joueur> ljou = new ArrayList<Joueur>();
        for (int i = 0; i < nbj; ++i) {
            Personnage p = new Personnage(DJ.getWidth() / 2, DJ.getHeight() - 100, 100, 100, -10);
            JTextArea jtxt = (JTextArea) menu2.getComponent(i);
            String nomjoueur = (jtxt.getText().equals("Entrez votre nom")) ? "Mizer" : jtxt.getText();
            ljou.add(new Joueur(p, nomjoueur, new Skin("/personnages/doodleNinja.png")));
        }
        Terrain rt = new Terrain(ljou, DJ.getHeight(), DJ.getWidth(), new Skin("/background/background.png"));

        // Ajout des éléments à la fenêtre
        DJ.add(new Vue(rt));

        return DJ;
    }

    private void gestionEvent() {
        buttonSolo.addActionListener(e -> {
            this.nbj = 1;
            this.menu.setVisible(false);
            this.menu2 = createMenu2();
            this.menu2.add(this.buttonPlay);
            this.add(this.menu2);
            int mpw2 = (int) this.menu2.getPreferredSize().getWidth();
            int mph2 = (int) this.menu2.getPreferredSize().getHeight();
            this.menu2.setBounds((this.getWidth() / 2) - (mpw2 / 2), (this.getHeight() / 2) - mph2, mpw2, mph2);
        });

        button2joueur.addActionListener(e -> {
            /*
             * FARES C'EST POUR TOI
             * this.nbj = 2;
             * menu.setVisible(false);
             * this.menu2 = createMenu2();
             * menu2.add(buttonPlay);
             * this.add(this.menu2);
             * int mpw2 = (int) menu2.getPreferredSize().getWidth();
             * int mph2 = (int) menu2.getPreferredSize().getHeight();
             * menu2.setBounds((this.getWidth() / 2) - (mpw2 / 2), (this.getHeight() / 2) -
             * mph2, mpw2, mph2);
             */
        });

        buttonExit.addActionListener(e -> {
            System.exit(0);
        });

        buttonLeaderboard.addActionListener(e -> {
            Classement c = new Classement();
            try {
                c.afficherClassement();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        buttonPlay.addActionListener(e -> {
            // On crée une nouvelle fenêtre de jeu
            DoodleJumpheur = createDJ();
            DoodleJumpheur.setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
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

    }
}