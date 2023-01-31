package gui;

//Import de packages

import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

// Import des autres dossiers
import gameobjects.*;

public class Vue extends JPanel implements KeyListener {

    int w = 600;
    int h = 933;
    BufferedImage view, terrainView, platformeView, persoView;
    static String chemin = (new File("gui/images/")).getAbsolutePath();
    boolean isRight, isLeft;
    Terrain rainT;

    public Vue(Terrain t) {
        this.rainT = t;
        this.setSize(w, h);
        this.addKeyListener(this);

        try {
            view = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            terrainView = ImageIO.read(new File(chemin + "/" + "background.png"));
            platformeView = ImageIO.read(new File(chemin + "/" + "plateforme.png"));
            persoView = ImageIO.read(new File(chemin + "/" + "doodle.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        int ppx = (int) rainT.getJoueur().getPerso().getX();
        int ppy = (int) rainT.getJoueur().getPerso().getY();
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.drawImage(terrainView, 0, 0, WIDTH, HEIGHT, null);
        g2.drawImage(persoView, ppx, ppy, 100, 100, null);

        ArrayList<Plateforme> pf = rainT.getPlateformesListe();
        for (Plateforme p : pf) {
            g2.drawImage(platformeView, (int) p.getX(), (int) p.getY(), 60, 20, null);
        }

        g.drawImage(view, 0, 0, this.w, this.h, null);
        g.dispose();

    }

    public void update() {

        if (isRight) {
            rainT.getJoueur().getPerso().setX(rainT.getJoueur().getPerso().getX() + 5);
        } else if (isLeft) {
            rainT.getJoueur().getPerso().setX(rainT.getJoueur().getPerso().getX() - 5);
        }

        rainT.update(0.03);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeft = true;
        }
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

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
