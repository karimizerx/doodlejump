package gui;

import javax.swing.*;
import java.awt.*;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.*;

import gameobjects.*;

public class App extends JFrame {

    public App() {
        // Création de la fenêtre
        super("Menu"); // Création de la fenêtre avec son titre
        // Définition de l'opération lorsqu'on ferme la fenêtre
        // Ici, revient à exécuter "this.dispose()" (ne ferme que la fenêtre actuelle )
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;      //Largeur de l'écran"
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;    //Longueur de l'écran
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(width/3, height); // Définition de la taille part défaut

        // Positionne la fenêre au centre du conteneur null
        // (la fenêtre de l'ordinateur, le bureau en quelques sortes)
        this.setLocationRelativeTo(null);

        // Empêche la fenetre d'être redimensionée
        // this.setResizable(false);
        this.setLayout(new FlowLayout());

        JPanel win = (JPanel) this.getContentPane();
        // win.setBackground(Color.BLACK);
        // win.setOpaque(true);
        JButton play = new JButton("Play");
        JButton multi = new JButton("Multiplayer");
        JButton leaderboard = new JButton("Leaderboard");
        JButton exit = new JButton("Exit");
        
        GridLayout gd = new GridLayout(4,1);   gd.setVgap(10);
        JPanel menu = new JPanel(); menu.setLayout(gd);
        
        menu.add(play); menu.add(multi); menu.add(leaderboard); menu.add(exit);
        this.add(menu);
        menu.setBounds(width/2, height/2, width/2, height/2);
        
        play.addActionListener(e -> {
			Game game = new Game();
			this.setContentPane(game);
			validate();
		});

        exit.addActionListener(e -> System.exit(0));
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