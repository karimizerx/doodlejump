package gui;

import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Personnage;
import gameobjects.Plateforme;
import gameobjects.Terrain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.WindowConstants;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class Vue extends JPanel implements Runnable, KeyListener {

    public static boolean isRunning;
    Thread thread;
    BufferedImage view, terrainView, platformeView, persoView, scoreView, scoreBackgroundView;

    Terrain terter;
    boolean isRight, isLeft, isMenu, isEsc;
    boolean pause = false;
    JFrame menuPause;
    JPanel menuPanel;
    String chemin = (new File("gui/images/")).getAbsolutePath();

    public Vue(Terrain ter) {
        this.terter = ter;
        setPreferredSize(new Dimension((int) terter.getWidth(), (int) terter.getHeight()));
        addKeyListener(this);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
        }
    }

    public void start() {
        try {
            try {
                view = new BufferedImage((int) terter.getWidth(), (int) terter.getHeight(), BufferedImage.TYPE_INT_RGB);

                terrainView = ImageIO.read(new File(chemin + "/" + "background.png"));
                platformeView = ImageIO.read(new File(chemin + "/" + "plateformeBase.png"));
                persoView = ImageIO.read(new File(chemin + "/" + "doodleNinja.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/" + "scoreBackground.png"));

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

    public boolean endGame() {
        isRunning = false;
        boolean isFin = false;
        for (int i = 0; i < terter.getListeJoueurs().length; ++i) {
            Joueur j = terter.getListeJoueurs()[i];
            if (j.getPerso().getY() + j.getPerso().getHeight() > this.getHeight()) {
                isFin = true;
            }
        }
        return isFin;
    }

    public void update() {

        for (int i = 0; i < terter.getListeJoueurs().length; ++i) {
            Joueur j = terter.getListeJoueurs()[i];
            Personnage p = j.getPerso();
            // Gère les boutons flèches
            if (isRight) {
                p.setX(p.getX() + 5);
            } else if (isLeft) {
                p.setX(p.getX() - 5);
            }
        }

        terter.update();
    }

    public void draw() {

        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.drawImage(terrainView, 0, 0, (int) terter.getWidth(), (int) terter.getHeight(), null);

        // Affichage des plateformes
        for (Plateforme pf : terter.getPlateformesListe()) {
            g2.drawImage(
                    platformeView,
                    (int) pf.getX(),
                    (int) pf.getY(),
                    (int) pf.getWidth(),
                    (int) pf.getHeight(),
                    null);
        }

        /// Affichage du Score
        if (terter.getListeJoueurs().length == 1) {
            String score = String.valueOf(terter.getListeJoueurs()[0].getScore());
            g2.drawImage(scoreBackgroundView, 2, 2, 60 + (30 * (score.length() - 1)), 55, null);
            for (int i = 0; i < score.length(); ++i) {
                char chiffre = score.charAt(i);
                try {
                    try {
                        scoreView = ImageIO.read(new File(chemin + "/ch" + chiffre + ".png"));

                    } catch (Exception e) {
                        scoreView = ImageIO.read(new File("src/gui/images/ch" + chiffre + ".png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(scoreView, 5 + (25 * i), 5, 50, 50, null);
            }
        }

        for (int i = 0; i < terter.getListeJoueurs().length; ++i) {
            Joueur j = terter.getListeJoueurs()[i];
            Personnage p = j.getPerso();
            g2.drawImage(persoView, (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);
        }

        Graphics g = getGraphics();
        g.drawImage(view, 0, 0, (int) terter.getWidth(), (int) terter.getHeight(), null);
        g.dispose();
    }

    @Override
    public void run() {
        try {
            requestFocusInWindow();
            start();
            while (isRunning) {
                if (!pause)
                    update();
                draw();
                Thread.sleep(10);
            }
            if (endGame()) {
                removeAll();
                repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gestion des boutons
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            pause();
        }
    }

    void pause() {
        isEsc = true;
        pause = !pause;
        menuPause = new JFrame();
        menuPause.setBounds((int) terter.getWidth() * 3 / 2 - 50, (int) terter.getHeight() / 2 - 60, 100, 120);
        menuPause.setResizable(false);
        menuPause.setLayout(new FlowLayout());
        menuPause.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JButton cont = new JButton("Continue");
        JButton exit = new JButton("Exit");

        menuPause.add(cont);
        menuPause.add(exit);
        menuPause.setVisible(true);

        cont.addActionListener(ev -> {
            menuPause.dispose();
            pause = !pause;

        });

        exit.addActionListener(ev -> {
            menuPause.dispose();
            JFrame retourMenu = new App();
            retourMenu.setVisible(true);
        });
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeft = false;
        }
    }

}