package gui;

// Import de packages java
import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

public class MenuDemarrer extends JPanel {

    private String chemin = (new File("gui/images/packBase/")).getAbsolutePath();
    private BufferedImage view, background, platformeBaseView;
    private ArrayList<BufferedImage> buttonSolo, button2joueur, buttonMulti, buttonLeaderboard, buttonExit, buttonPlay;
    private int width = Toolkit.getDefaultToolkit().getScreenSize().width; // Largeur de l'écran
    private int height = Toolkit.getDefaultToolkit().getScreenSize().height; // Longueur de l'écran
    private int nbj; // Nombre de joueurs

    public MenuDemarrer() {
        this.setSize(new Dimension(width, height));
        createSolo();
    }

    private void createSolo() {
        this.view = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        try {
            background = ImageIO.read(new File(chemin + "/background/background.png"));
            platformeBaseView = ImageIO.read(new File(chemin + "/plateformes/plateformeBase.png"));

            String s = "Jouer solo";
            for (int j = 0; j < s.length(); ++j) {
                BufferedImage button = ImageIO.read(new File(chemin + "/lettres/lettre" + s.charAt(j) + ".png"));
                buttonSolo.add(button);
            }
        } catch (Exception e) {
        }
    }

    public void afficheImage() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        System.out.println(g == null);
        System.out.println(view == null);
        g.drawImage(view, 0, 0, this.getWidth(), this.getHeight(), null);
        g.dispose(); // On libère les ressource
    }

    public static void main(String[] args) {
        JPanel newMenu = new MenuDemarrer();
        JFrame w = new JFrame("OLIIIIIIII");
        w.setSize(new Dimension(newMenu.getWidth() / 3, (int) (newMenu.getHeight() * 0.95)));
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.setResizable(false);
        w.setLocationRelativeTo(null);
        w.add(newMenu);
        ((MenuDemarrer) newMenu).afficheImage();
        w.setVisible(true);
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}