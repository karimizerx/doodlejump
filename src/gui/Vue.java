package gui;

// Import d'autres dossiers
import gameobjects.*;

// Import de packages java
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.imageio.*;

// S'occupe d'afficher les éléments du terrain
public class Vue extends JPanel implements Runnable, KeyListener {

    public static boolean isRunning;
    private Thread thread;
    private String chemin = (new File("gui/images/")).getAbsolutePath();
    private BufferedImage view, terrainView, platformeView, persoView, scoreView, scoreBackgroundView;
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
                terrainView = ImageIO.read(new File(chemin + "/background.png"));
                platformeView = ImageIO.read(new File(chemin + "/plateformeBase.png"));
                persoView = ImageIO.read(new File(chemin + "/doodleNinja.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/scoreBackground.png"));
            } catch (Exception e) {
                terrainView = ImageIO.read(new File("src/gui/images/background.png"));
                platformeView = ImageIO.read(new File("src/gui/images/plateforme.png"));
                persoView = ImageIO.read(new File("src/gui/images/doodleNinja.png"));
                scoreBackgroundView = ImageIO.read(new File("src/gui/images/scoreBackground.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Dessine toutes les images
    public void draw() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.drawImage(terrainView, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null); // Affichage terrain

        // Affichage des plateformes
        for (Plateforme pf : terrain.getPlateformesListe()) {
            g2.drawImage(
                    platformeView,
                    (int) pf.getX(),
                    (int) pf.getY(),
                    (int) pf.getWidth(),
                    (int) pf.getHeight(),
                    null);
        }

        // Affichage du Score
        // Seulement s'il n'y a qu'un joueur
        if (terrain.getListeJoueurs().length == 1) {
            String score = String.valueOf(terrain.getListeJoueurs()[0].getScore());
            g2.drawImage(scoreBackgroundView, 2, 2, 60 + (30 * (score.length() - 1)), 55, null);
            for (int i = 0; i < score.length(); ++i) {
                try {
                    try {
                        scoreView = ImageIO.read(new File(chemin + "/ch" + score.charAt(i) + ".png"));

                    } catch (Exception e) {
                        scoreView = ImageIO.read(new File("src/gui/images/ch" + score.charAt(i) + ".png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(scoreView, 5 + (25 * i), 5, 50, 50, null);
            }
        }

        // Affichage des personnages
        for (int i = 0; i < terrain.getListeJoueurs().length; ++i) {
            Joueur j = terrain.getListeJoueurs()[i];
            Personnage p = j.getPerso();
            g2.drawImage(persoView, (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);
        }

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);
        g.dispose();
    }

    // Gère le cas de fin du jeu
    public boolean endGame() {
        isRunning = false;
        boolean isFin = false;
        // Si un joueur à perdu, c'est fini
        for (int i = 0; i < terrain.getListeJoueurs().length; ++i) {
            Joueur j = terrain.getListeJoueurs()[i];
            if (j.getPerso().getY() + j.getPerso().getHeight() > this.getHeight()) {
                isFin = true;
            }
        }
        return isFin;
    }

    // Met à jour l'affichage
    public void update() {
        for (int i = 0; i < terrain.getListeJoueurs().length; ++i) {
            Joueur j = terrain.getListeJoueurs()[i];
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
    @Override
    public void run() {
        try {
            requestFocusInWindow();
            init();
            while (isRunning) {
                if (!pause)
                    update();
                draw();
                Thread.sleep(5); // Temps de stop d'update, en ms
            }
            if (endGame()) { // Si c'est la fin du jeu, on retire tout
                if (terrain.getListeJoueurs().length == 1) {
                    Joueur j = terrain.getListeJoueurs()[0];
                    String score = String.valueOf(j.getScore());
                    Classement c = new Classement();
                    c.ajoutClassement(j.getNom(), score);
                    c.afficherClassement();
                }
                removeAll();
                repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ????
    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
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