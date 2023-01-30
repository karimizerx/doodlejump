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

    public Vue() {
        this.setSize(w, h);
        System.out.println(chemin);
        try {
            bi = ImageIO.read(new File(chemin + "/" + "terrain.jpg"));
            perso = ImageIO.read(new File(chemin + "/" + "iliou.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw() {

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.drawImage(perso, this.getWidth() / 2, this.getHeight() / 2, 100, 100, null);
        g.drawImage(bi, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
