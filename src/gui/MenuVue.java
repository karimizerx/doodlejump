package gui;

// Import d'autres dossiers
import gameobjects.*;
import leaderboard.*;
import multiplayer.*;

// Import de packages java
import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.imageio.*;

// S'occupe d'afficher les éléments du terrain
public class MenuVue extends JPanel implements Runnable, KeyListener {

    private final int width, height;
    private String chemin = (new File("gui/images/packBase/")).getAbsolutePath();
    private BufferedImage view, backgroundView;
    private ArrayList<BufferedImage> buttonJouer, button2joueur, buttonMultiJoueur, buttonLb, buttonQuitter;
    private int space = 0;
    private boolean isRunning;
    private Thread thread;
    private JFrame DoodleJumpheur;
    private App frame;

    public MenuVue(App frame) {
        // Taille du panel
        this.frame = frame;
        this.width = frame.getWidth();
        this.height = frame.getHeight();
        this.setSize(new Dimension(this.width / 3, (int) (this.height * 0.95)));
        this.isRunning = true;
        this.addKeyListener(this);
    }

    // Méthodes de la classe

    // Initialise toutes les images du jeu
    private void init() {
        // view est l'image qui contiendra toutes les autres
        this.view = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                this.backgroundView = ImageIO.read(new File(chemin + "/background/background1.png"));
            } catch (Exception e) {
                this.backgroundView = ImageIO.read(new File("src/gui/images/packBase/background/background1.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.buttonJouer = createImage("Jouer en solo");
        this.button2joueur = createImage("Jouer a 2");
        this.buttonMultiJoueur = createImage("Mode multijoueurs");
        this.buttonLb = createImage("Classement");
        this.buttonQuitter = createImage("Quitter");
    }

    private ArrayList<BufferedImage> createImage(String mot) {
        ArrayList<BufferedImage> motView = new ArrayList<BufferedImage>();
        mot = mot.toLowerCase();

        for (int i = 0; i < mot.length(); ++i) {
            char c = mot.charAt(i);
            try {
                try {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                } catch (Exception e) {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return motView;
    }

    private void update() {
    }

    // Dessine toutes les images
    public void afficheImage() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        int y = (10 * height / 100);
        int x = (10 * width / 100) - 15;
        int w = 30, h = 30;
        // Affichage des boutons
        for (int i = 0; i < buttonJouer.size(); ++i) {
            BufferedImage image = buttonJouer.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < button2joueur.size(); ++i) {
            BufferedImage image = button2joueur.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < buttonMultiJoueur.size(); ++i) {
            BufferedImage image = buttonMultiJoueur.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < buttonLb.size(); ++i) {
            BufferedImage image = buttonLb.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < buttonQuitter.size(); ++i) {
            BufferedImage image = buttonQuitter.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Fait tourner le jeu
    // Cette méthode contient les traitements
    @Override
    public void run() {
        try {
            // Demande à ce que ce composant obtienne le focus.
            // Le focus est le fait qu'un composant soit sélectionné ou pas.
            // Le composant doit être afficheable (OK grâce à addNotify())
            this.requestFocusInWindow();
            init(); // Initialisation des images
            while (isRunning) { // Tant que le jeu tourne
                update();
                afficheImage(); // On affiche les images une fois les données update
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Rend ce composant affichable en le connectant à une ressource d'écran
    @Override
    public void addNotify() {
        super.addNotify();
        // Si on a toujours pas lancer le jeu
        if (this.thread == null) {
            // Créer une nouvelle instance en précisant les traitements à exécuter (run)
            // This est l'objet qui implémente Runnable (run()), contenant les traitements
            this.thread = new Thread(this);
            isRunning = true; // Indique le jeu est lancé
            this.thread.start(); // Invoque la méthode run()
        }
    }

    private JFrame createDJ() {
        // Initalisation de la fenêtre
        JFrame DJ = new JFrame();
        DJ.setTitle("Doodle Jumpheur");
        // Définition de la taille de cette fenêtre de jeu
        int ecranw = Toolkit.getDefaultToolkit().getScreenSize().width; // Largeur de l'écran
        int ecranh = Toolkit.getDefaultToolkit().getScreenSize().height; // Longueur de l'écran
        int framew = ecranw / 3, frameh = (int) (ecranh * 0.95);
        DJ.setSize(framew, frameh);
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
        for (int i = 0; i < 1; ++i) {
            // L'image du perso doit être un carré. On prend la valeure la plus petite
            double z = ((frameh * 0.09746) > (framew * 0.15625)) ? (framew * 0.15625) : (frameh * 0.09746);
            Personnage p = new Personnage(DJ.getWidth() / 2, DJ.getHeight() - z, z, z, -(frameh * 0.0097465887));
            ljou.add(new Joueur(p, "Mizer"));

        }
        Terrain rt = new Terrain(ljou, DJ.getHeight(), DJ.getWidth(), false, false, 0);

        // Ajout des éléments à la fenêtre
        DJ.add(new Vue(rt));

        return DJ;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(this.space);
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.space == 0) {
                DoodleJumpheur = createDJ();
                DoodleJumpheur.setVisible(true);
                this.frame.dispose();
            }
            if (this.space == 1) {
                DoodleJumpheur = createDJ();
                DoodleJumpheur.setVisible(true);
                this.frame.dispose();
            }

            if (this.space == 2) {
                DoodleJumpheur = createDJ();
                DoodleJumpheur.setVisible(true);
                this.frame.dispose();
            }

            if (this.space == 3) {
                Classement c = new Classement();
                try {
                    c.afficherClassement();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (this.space == 4) {
                System.exit(0);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.space = (this.space == 4) ? 0 : this.space + 1;
            System.out.println(this.space);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}