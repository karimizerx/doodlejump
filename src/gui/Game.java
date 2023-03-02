package gui;

// Import de packages java :
import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Game extends JFrame {

    private JPanel vue;
    private final int ecranw = Toolkit.getDefaultToolkit().getScreenSize().width; // Largeur de l'écran
    private final int ecranh = Toolkit.getDefaultToolkit().getScreenSize().height; // Longueur de l'écran
    private final int framew = ecranw / 3, frameh = (int) (ecranh * 0.95);

    public Game() {
        // Initalisation de la fenêtre
        super("Doodle Jumpheur");
        // Définition de la taille de cette fenêtre de jeu

        this.setSize(framew, frameh);
        // Empêche la fenetre d'être redimensionée
        this.setResizable(false);
        // Action à effectuer en cas de fermeture : fermer uniquement de cette fenêtre
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Placement de la fenêtre au centre du bureau (null)
        this.setLocationRelativeTo(null);
        // Fenêtre pas visible tant que l'on a pas appuyé sur play
        this.setVisible(false);

        // Ajout des éléments à la fenêtre
        this.vue = new Vue(this, "packBase");
        this.add(vue);
    }

    public void closeWindow() {
        this.dispose();
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
            Game mw = new Game();
            mw.setVisible(true);
        });
    }

    public JPanel getVue() {
        return vue;
    }

    public void setVue(JPanel vue) {
        this.vue = vue;
    }
}