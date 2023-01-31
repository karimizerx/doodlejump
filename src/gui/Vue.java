package gui;

//Import de packages

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

// Import des autres dossiers
import gameobjects.*;

public class Vue extends JPanel implements Runnable, KeyListener {

    int w, h;
    BufferedImage view, terrainView, platformeView, persoView;
    static String chemin = (new File("gui/images/")).getAbsolutePath();
    boolean isRight, isLeft;
    static boolean isRunning;
    Terrain rainT;
    Thread thread;

    public Vue(Terrain t) {
        this.rainT = t;
        w = (int) t.getWidth();
        h = (int) t.getHeight();
        this.setSize(w, h);
        this.addKeyListener(this);

    }

    private void startImage() {
        try {
            view = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            terrainView = ImageIO.read(new File(chemin + "/" + "background.png"));
            platformeView = ImageIO.read(new File(chemin + "/" + "plateforme.png"));
            persoView = ImageIO.read(new File(chemin + "/" + "doodle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        int ppx = (int) rainT.getJoueur().getPerso().getX();
        int ppy = (int) rainT.getJoueur().getPerso().getY();

        Graphics2D g2 = (Graphics2D) view.getGraphics();
        System.out.println("on essaye de dessiner le back");
        g2.drawImage(terrainView, 0, 0, this.w, this.h, null);
        System.out.println("on a dessiné le back");

        System.out.println("on va aller dans try");
        try {
            System.out.println("on est entré dans try");
            ArrayList<Plateforme> pf = rainT.getPlateformesListe();
            System.out.println("on a récupère la liste ");
            for (Plateforme p : pf) {
                System.out.println("on rentre dans la boucle");
                System.out.println("on essaye de dessinr les plat");
                g2.drawImage(platformeView, (int) p.getX(), (int) p.getY(), 60, 20, null);
                System.out.println("on a dessiné les plat");
            }
        } catch (Exception e) {
        }
        System.out.println("on est sorti du try");
        g2.drawImage(persoView, ppx, ppy, 100, 100, null);
        g2.drawRect(100, 100, 100, 100);

        Graphics g = getGraphics();
        g.drawImage(view, 0, 0, this.w, this.h, null);
        g.dispose();
    }

    public void updateView() {

        if (isRight) {
            rainT.getJoueur().getPerso().setX(rainT.getJoueur().getPerso().getX() + 5);
        } else if (isLeft) {
            rainT.getJoueur().getPerso().setX(rainT.getJoueur().getPerso().getX() - 5);
        }

        rainT.update(0.03);
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

    @Override
    public void run() {
        try {
            requestFocusInWindow();
            System.out.println(getGraphics() == null);
            System.out.println("J'appelle start");
            startImage();
            while (isRunning) {
                updateView();
                draw();
                Thread.sleep(1000 / 60);
            }
            // removeAll();
            // repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public BufferedImage getView() {
        return view;
    }

    public void setView(BufferedImage view) {
        this.view = view;
    }

    public BufferedImage getTerrainView() {
        return terrainView;
    }

    public void setTerrainView(BufferedImage terrainView) {
        this.terrainView = terrainView;
    }

    public BufferedImage getPlatformeView() {
        return platformeView;
    }

    public void setPlatformeView(BufferedImage platformeView) {
        this.platformeView = platformeView;
    }

    public BufferedImage getPersoView() {
        return persoView;
    }

    public void setPersoView(BufferedImage persoView) {
        this.persoView = persoView;
    }

    public static String getChemin() {
        return chemin;
    }

    public static void setChemin(String chemin) {
        Vue.chemin = chemin;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean isRight) {
        this.isRight = isRight;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setRunning(boolean isRunning) {
        Vue.isRunning = isRunning;
    }

    public Terrain getRainT() {
        return rainT;
    }

    public void setRainT(Terrain rainT) {
        this.rainT = rainT;
    }
}