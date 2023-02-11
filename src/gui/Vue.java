package gui;

// Import d'autres dossiers
import gameobjects.*;

// Import de packages java
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

// S'occupe d'afficher les éléments du terrain
public class Vue extends JPanel implements Runnable, KeyListener {

    public static boolean isRunning;
    private Thread thread;
    private String chemin = (new File("gui/images/")).getAbsolutePath();
    private BufferedImage view, terrainView, platformeBaseView, platformeMobileView, persoView, scoreView,
            scoreBackgroundView, nomView;
    private boolean isRight, isLeft, pause = false;
    private Terrain terrain;
    private JFrame menuPause;

    public Vue(Terrain ter) {
        this.terrain = ter;
        // Taille du panel
        this.setPreferredSize(new Dimension((int) terrain.getWidth(), (int) terrain.getHeight()));
        // Gestion d'évènements boutons
        this.addKeyListener(this);
    }

    // Méthodes de la classe

    // Initialise toutes les images du jeu
    private void init() {
        // view est l'image qui contiendra toutes les autres
        view = new BufferedImage((int) terrain.getWidth(), (int) terrain.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                terrainView = ImageIO.read(new File(chemin + "/background/background.png"));
                platformeBaseView = ImageIO.read(new File(chemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(chemin + "/plateformes/plateformeMobile.png"));
                persoView = ImageIO.read(new File(chemin + "/personnages/doodleNinja.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/background/scoreBackground.png"));
            } catch (Exception e) {
                terrainView = ImageIO.read(new File("src/gui/images/background/background.png"));
                platformeBaseView = ImageIO.read(new File("src/gui/images/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File("src/gui/images/plateformes/plateformeMobile.png"));
                persoView = ImageIO.read(new File("src/gui/images/personnages/doodleNinja.png"));
                scoreBackgroundView = ImageIO.read(new File("src/gui/images/background/scoreBackground.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Dessine toutes les images
    public void afficheImage() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(terrainView, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);

        // Affichage des plateformes
        for (Plateforme pf : terrain.getPlateformesListe()) {
            BufferedImage pfV = (pf instanceof PlateformeBase) ? platformeBaseView : platformeMobileView;
            g2.drawImage(pfV, (int) pf.getX(), (int) pf.getY(), (int) pf.getWidth(), (int) pf.getHeight(), null);
        }

        // Affichage du Score : seulement s'il n'y a qu'un joueur
        if (terrain.getListeJoueurs().size() == 1) {
            String score = String.valueOf(terrain.getListeJoueurs().get(0).getScore());
            g2.drawImage(scoreBackgroundView, 2, 2, 60 + (30 * (score.length() - 1)), 55, null);
            for (int i = 0; i < score.length(); ++i) {
                try {
                    try {
                        scoreView = ImageIO.read(new File(chemin + "/chiffres/ch" + score.charAt(i) + ".png"));

                    } catch (Exception e) {
                        scoreView = ImageIO.read(new File("src/gui/images/chiffres/ch" + score.charAt(i) + ".png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(scoreView, 5 + (25 * i), 5, 50, 50, null);
            }
        }

        // Affichage des personnages + Nom
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur joueur = terrain.getListeJoueurs().get(i);
            Personnage p = joueur.getPerso();
            String nom = joueur.getNom().toLowerCase();
            g2.drawImage(persoView, (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);
            for (int j = 0; j < nom.length(); ++j) {
                try {
                    try {
                        nomView = ImageIO.read(new File(chemin + "/lettres/lettre" + nom.charAt(i) + ".png"));

                    } catch (Exception e) {
                        nomView = ImageIO.read(new File("src/gui/images/lettres/lettre" + nom.charAt(i) + ".png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(nomView, (int) p.getX() + (5 * i), (int) p.getY() - 5, 25, 25, null);
            }
        }

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);
        g.dispose(); // On libère les ressource
    }

    // Gère le cas de fin du jeu
    public boolean endGame() {
        boolean isFin = false;
        // Si un joueur à perdu, c'est fini
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            isFin = (j.getPerso().getY() + j.getPerso().getHeight() > this.getHeight()) ? true : false;
        }
        return isFin;
    }

    // Met à jour l'affichage
    public void update() {
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            Personnage p = j.getPerso();
            // Gère les boutons flèches
            if (isRight) {
                p.setX(p.getX() + 5);
            } else if (isLeft) {
                p.setX(p.getX() - 5);
            }
        }
        terrain.update();
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
                if (!pause) // Tant qu'on appuie pas sur pause
                    update(); // On met à jour les variables
                afficheImage(); // On affiche les images
                Thread.sleep(5); // Temps de stop de la thread, i.e d'update (en ms)
            }
            if (endGame()) { // Si c'est la fin du jeu
                if (terrain.getListeJoueurs().size() == 1) { // S'il n'y a qu'1 joueur, on affiche le score/LB
                    Joueur j = terrain.getListeJoueurs().get(0);
                    String score = String.valueOf(j.getScore());
                    System.out.println("Score à cette manche : " + j.getScore());
                    Classement c = new Classement();
                    c.ajoutClassement(j.getNom(), score);
                    c.afficherClassement();
                }
                this.removeAll(); // On retire tout
                this.repaint(); // On met à jour l'affichage
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

    // Gestion des boutons

    private void pause() {
        this.pause = !this.pause;
        this.menuPause = new JFrame();
        this.menuPause.setBounds((int) terrain.getWidth() * 3 / 2 - 50, (int) terrain.getHeight() / 2 - 60, 150, 120);
        this.menuPause.setResizable(false);
        this.menuPause.setLayout(new FlowLayout());
        this.menuPause.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JButton cont = new JButton("Continuer");
        JButton exit = new JButton("Menu principal");

        this.menuPause.add(cont);
        this.menuPause.add(exit);
        this.menuPause.setVisible(true);

        cont.addActionListener(ev -> {
            this.menuPause.dispose();
            this.pause = !this.pause;
        });

        exit.addActionListener(ev -> {
            this.menuPause.dispose();
            JFrame retourMenu = new App();
            retourMenu.setVisible(true);
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.isRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.isLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            pause();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.isRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.isLeft = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}