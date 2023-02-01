package gui;

import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Personnage;
import gameobjects.Plateforme;
import gameobjects.PlateformeBase;
import gameobjects.Terrain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.image.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;

public class Vue extends JPanel implements Runnable, KeyListener {

    boolean isRunning;
    Thread thread;
    BufferedImage view, terrainView, platformeView, persoView;

    Terrain terter;
    int lll;
    boolean right, left;

    public Vue(Terrain ter) {
        this.terter = ter;
        // lll = (int) (terter.getHeight() * terter.getAdvancement());
        lll = 150;
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
        String chemin = (new File("gui/images/")).getAbsolutePath();

        try {
            view = new BufferedImage((int) terter.getWidth(), (int) terter.getHeight(), BufferedImage.TYPE_INT_RGB);

            terrainView = ImageIO.read(new File(chemin + "/" + "background.png"));
            platformeView = ImageIO.read(new File(chemin + "/" + "plateforme.png"));
            persoView = ImageIO.read(new File(chemin + "/" + "doodle.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(getGraphics() == null);
    }

    public void update() {
        Joueur j = terter.getJoueur();
        Personnage p = j.getPerso();

        if (right) {
            p.setX(p.getX() + 3);
        } else if (left) {
            p.setX(p.getX() - 3);
        }
        p.setDy(p.getDy() + 0.2);
        p.setY(p.getY() + p.getDy());
        if (p.getY() > 900) {
            isRunning = false;
        }

        if (p.getY() < lll) {
            p.setY(lll);
            for (Plateforme pf : terter.getPlateformesListe()) {
                pf.setY(pf.getY() - (int) p.getDy());
                if (pf.getY() > 933) {
                    pf.setY(0);
                    int r = new Random().nextInt(500);
                    pf.setX(r);
                }
            }
        }

        for (Plateforme pf : terter.getPlateformesListe()) {
            if ((p.getX() + 50 > pf.getX()) &&
                    (p.getX() + 20 < pf.getX() + 68) &&
                    (p.getY() + 70 > pf.getY()) &&
                    (p.getY() + 70 < pf.getY() + 14) &&
                    (p.getDy() > 0)) {
                p.setDy(p.getDy() - 10);
            }
        }
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
                update();
                draw();
                Thread.sleep(1000 / 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
    }
}