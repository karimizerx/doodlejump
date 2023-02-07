package gui;

import javax.swing.*;

import gameobjects.*;

import java.awt.*;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Transparency;

public class MenuVue extends JPanel implements Runnable {

    public static boolean isRunning;
    Thread thread;
    BufferedImage view, terrainView, platformeView, persoView, MenuDemarrerView;

    ArrayList<Plateforme> pfList;
    Personnage perso;
    int width, height;

    public MenuVue(int w, int h) {
        this.width = w;
        this.height = h;
        this.setPreferredSize(new Dimension(width, height));

        Plateforme pf1 = new PlateformeBase(width * 0.1, height * 0.70, 60, 20, 0);
        Plateforme pf2 = new PlateformeBase(width * 0.4, height * 0.10, 60, 20, 0);
        Plateforme pf3 = new PlateformeBase(width * 0.2, height * 0.30, 60, 20, 0);
        Plateforme pf4 = new PlateformeBase(width * 0.70, height * 0.90, 60, 20, 0);
        this.pfList = new ArrayList<Plateforme>();
        this.pfList.add(pf1);
        this.pfList.add(pf2);
        this.pfList.add(pf3);
        this.pfList.add(pf4);

        this.perso = new Personnage((width * 0.1), height + 100,
                100, 100, -15);

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
                view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                terrainView = ImageIO.read(new File(chemin + "/" + "background.png"));
                platformeView = ImageIO.read(new File(chemin + "/" + "plateformeBase.png"));
                persoView = ImageIO.read(new File(chemin + "/" + "doodleNinja.png"));
                MenuDemarrerView = ImageIO.read(new File(chemin + "/" + "MenuDemarrerBackground.png"));

            } catch (Exception e) {
                view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                terrainView = ImageIO.read(new File("src/gui/images/background.png"));
                platformeView = ImageIO.read(new File("src/gui/images/plateforme.png"));
                persoView = ImageIO.read(new File("src/gui/images/doodleNinja.png"));
                MenuDemarrerView = ImageIO.read(new File("src/gui/images/MenuDemarrerBackground.png"));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void update() {
        this.perso.setDy(this.perso.getDy() + 0.2);
        this.perso.setY(this.perso.getY() + this.perso.getDy());
        for (Plateforme pf : pfList) {
            this.perso.collides_plateforme(pf);
        }
    }

    public void draw() {

        Graphics2D g2 = (Graphics2D) view.getGraphics();

        g2.drawImage(terrainView, 0, 0, width, height, null);
        // Affichage des plateformes
        for (Plateforme pf : pfList) {
            g2.drawImage(
                    platformeView,
                    (int) pf.getX(),
                    (int) pf.getY(),
                    (int) pf.getWidth(),
                    (int) pf.getHeight(),
                    null);
        }

        g2.drawImage(persoView, (int) this.perso.getX(), (int) this.perso.getY(), (int) this.perso.getWidth(),
                (int) this.perso.getHeight(), null);

        // Affichage menu
        g2.setColor(new Color(0, 0, 0, 0));
        g2.fillRect((int) (width * 0.5), (int) (height * 0.5), (int) (width * 0.5), (int) (height / 3));

        Graphics g = getGraphics();
        g.drawImage(view, 0, 0, width, height, null);
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
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}