package gui;

import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Personnage;
import gameobjects.Plateforme;
import gameobjects.Terrain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class Vue extends JPanel implements Runnable, KeyListener{

    public static boolean isRunning;
    Thread thread;
    BufferedImage view, terrainView, platformeView, persoView;

    Terrain terter;
    int lll;
    boolean isRight, isLeft, isMenu, isEsc;
    boolean pause = false;
    JFrame menuPause;
    JPanel menuPanel;

    public Vue(Terrain ter) {
        this.terter = ter;
        setPreferredSize(new Dimension((int) terter.getWidth(), (int) terter.getHeight()));
        // retournMenu();
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
        String chemin = (new File("gui/images/")).getAbsolutePath();

        try {
            try {
                view = new BufferedImage((int) terter.getWidth(), (int) terter.getHeight(), BufferedImage.TYPE_INT_RGB);

                terrainView = ImageIO.read(new File(chemin + "/" + "background.png"));
                platformeView = ImageIO.read(new File(chemin + "/" + "plateformeBase.png"));
                persoView = ImageIO.read(new File(chemin + "/" + "doodleNinja.png"));

            } catch (Exception e) {
                terrainView = ImageIO.read(new File("src/gui/images/background.png"));
                platformeView = ImageIO.read(new File("src/gui/images/plateforme.png"));
                persoView = ImageIO.read(new File("src/gui/images/doodleNinja.png"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    public boolean endGame() {
        isRunning = false;
        if (terter.getJoueur().getPerso().getY() + terter.getJoueur().getPerso().getHeight() > this.getHeight()) {
            return true;
        } else {
            return false;
        }
    }

    public void update() {
        Joueur j = terter.getJoueur();
        Personnage p = j.getPerso();

        // Gère les boutons flèches
        if (isRight) {
            p.setX(p.getX() + 5);
        } else if (isLeft) {
            p.setX(p.getX() - 5);
        }
        
        

        terter.update();
    }

    public void draw() {
        Joueur j = terter.getJoueur();
        Personnage p = j.getPerso();

        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.drawImage(terrainView, 0, 0, (int) terter.getWidth(), (int) terter.getHeight(), null);

        for (Plateforme pf : terter.getPlateformesListe()) {
            g2.drawImage(
                    platformeView,
                    (int) pf.getX(),
                    (int) pf.getY(),
                    (int) pf.getWidth(),
                    (int) pf.getHeight(),
                    null);
        }
        g2.drawImage(persoView, (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);

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
                if(!pause) update();
                draw();
                Thread.sleep(10);
            }
            if (endGame()) {
                removeAll();
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

    void pause(){
        isEsc = true;
            pause = !pause;
            menuPause = new JFrame();
            menuPause.setBounds((int)terter.getWidth()*3/2 -50, (int)terter.getHeight()/2 -60 , 100, 120);
            menuPause.setResizable(false);
            menuPause.setLayout(new FlowLayout());

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