package gui;

import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Personnage;
import gameobjects.Plateforme;
import gameobjects.Terrain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class Vue extends JPanel implements Runnable, KeyListener{

    public static boolean isRunning;
    Thread thread;
    BufferedImage view, terrainView, platformeView, persoView;

    Terrain terter;
    int lll;
    boolean isMenu, isEsc;
    boolean pause = false;
    JFrame menuPause;

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
            try{
            view = new BufferedImage((int) terter.getWidth(), (int) terter.getHeight(), BufferedImage.TYPE_INT_RGB);

                terrainView = ImageIO.read(new File(chemin + "/" + "background.png"));
                platformeView = ImageIO.read(new File(chemin + "/" + "plateformeBase.png"));
                persoView = ImageIO.read(new File(chemin + "/" + "doodleNinja.png"));

            } catch (Exception e) {
                terrainView = ImageIO.read(new File("src/gui/images/background.png"));
                platformeView = ImageIO.read(new File("src/gui/images/plateforme.png"));
                persoView = ImageIO.read(new File("src/gui/images/doodleNinja.png"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(getGraphics() == null);
    }

    public boolean endGame() {
        isRunning = false;
        if (terter.getJoueurA().getPerso().getY() > 900) {
            return true;
        } else {
            return false;
        }
    }

    public void update() {
        Joueur j = terter.getJoueurA();
        Personnage p = j.getPerso();

        // Gère les boutons flèches
        if (p.isRight) {
            p.setX(p.getX() + 5);
        } else if (p.isLeft) {
            p.setX(p.getX() - 5);
        }
        j = terter.getJoueurB();
        if(j!=null){
            p = j.getPerso();
            if (p.isRight) {
                p.setX(p.getX() + 5);
            } else if (p.isLeft) {
                p.setX(p.getX() - 5);
            }
        }
        terter.update();
    }

    public void draw() {
        Personnage pA = terter.getJoueurA().getPerso();

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
        g2.drawImage(persoView, (int) pA.getX(), (int) pA.getY(), (int) pA.getWidth(), (int) pA.getHeight(), null);
        if(terter.getJoueurB()!=null){
            Personnage pB = terter.getJoueurB().getPerso();
            g2.drawImage(persoView, (int) pB.getX(), (int) pB.getY(), (int) pB.getWidth(), (int) pB.getHeight(), null);
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
                if(!pause) update();
                draw();
                Thread.sleep(10);
            }
            /*
             * if (isMenu) {
             * JPanel j = new MenuPrincipal(this);
             * // j.setSize(0, 0);
             * this.add(j);
             * // j.setLocation(this.getWidth() / 2, this.getHeight() / 2);
             * j.setBounds(0, 0, 0, 0);
             * 
             * }
             */
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
        if((terter.isHost && terter.multiplayer)||!terter.multiplayer){
            Personnage pA=terter.getJoueurA().getPerso();
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pA.isRight = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pA.isLeft = true;
            }
            if(!terter.multiplayer){
                Personnage pB=terter.getJoueurB().getPerso();
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    pB.isRight = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    pB.isLeft = true;
                }
                terter.getClient().sendPos(terter.getPlayerBmvt());
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            isEsc = true;
            pause = !pause;
            menuPause = new JFrame();
            menuPause.setLayout(new FlowLayout());
            menuPause.setBounds((int)terter.getWidth()/2, (int)terter.getHeight()/2, 200, 200);
            JButton cont = new JButton("Continue");
            JButton exit = new JButton("Exit");
            menuPause.add(cont, exit);
            menuPause.setVisible(pause);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if((terter.isHost && terter.multiplayer)||!terter.multiplayer){
            Personnage pA=terter.getJoueurA().getPerso();
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pA.isRight = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pA.isLeft = false;
            }
            if(!terter.multiplayer){
                Personnage pB=terter.getJoueurB().getPerso();
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    pB.isRight = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    pB.isLeft = false;
                }
                terter.getClient().sendPos(terter.getPlayerBmvt());
            }
        }
    }

    public void retournMenu() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (0 <= e.getX() && e.getX() <= 100 && 0 <= e.getY() && e.getY() <= 100) {
                    isRunning = false;
                    isMenu = true;
                }
            }
        });
    }
}