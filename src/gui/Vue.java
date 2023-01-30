package gui;

//Import de packages
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Vue extends JPanel {

    int w = 500;
    int h = 733;
    BufferedImage bi, perso;
    static String chemin = (new File("gui/ressources/")).getAbsolutePath();
    int ppx, ppy;

    public Vue(int posPersoX, int posPersoY) {
        this.ppx = posPersoX;
        this.ppy = posPersoY;
        this.setSize(w, h);
        System.out.println(chemin);
        try {
            bi = ImageIO.read(new File(chemin + "/" + "terrain.jpg"));
            perso = ImageIO.read(new File(chemin + "/" + "iliou.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.drawImage(perso, ppx, ppy, 100, 100, null);
        g.drawImage(bi, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public Vue update() {
        System.out.println("Oe oe oe je vais update");
        System.out.println("Tout est supp");
        ppx += 50;
        ppy += 200;
        System.out.println("Jé mi à jour les valeurs");
        Vue v = new Vue(ppx, ppy);
        System.out.println("Petite vue recrée");
        repaint();
        System.out.println("Je pains comme Picasso");
        return v;
    }
}
