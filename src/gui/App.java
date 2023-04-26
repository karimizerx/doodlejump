package gui;

// Import de packages java :
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class App extends JFrame {

    private JPanel vue;
    private final int ecranw = Toolkit.getDefaultToolkit().getScreenSize().width; // Largeur de l'écran
    private final int ecranh = Toolkit.getDefaultToolkit().getScreenSize().height; // Longueur de l'écran
    private final int framew = ecranw / 3, frameh = (int) (ecranh * 0.95); // Dimensions de la fenêtre

    public App() {
        // Initalisation de la fenêtre
        super("Doodle Jumpheur");
        // Définition de la taille de cette fenêtre de jeu
        this.setSize(framew, frameh);
        // Empêche la fenetre d'être redimensionée
        this.setResizable(false);
        // Action à effectuer en cas de fermeture : fermer uniquement de cette fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Placement de la fenêtre au centre du bureau (null)
        this.setLocationRelativeTo(null);

        // Ajout des éléments à la fenêtre
        this.vue = new Vue(this);
        this.add(vue);
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