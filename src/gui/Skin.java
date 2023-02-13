package gui;
import gameobjects.*;
import java.io.*;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;


public class Skin {
    private BufferedImage image;

    public Skin(String chemin){
        try {
            image = ImageIO.read(new File((new File("gui/images/")).getAbsolutePath() + chemin));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(String chemin) {
        try {
            this.image = ImageIO.read(new File((new File("gui/images/")).getAbsolutePath() + chemin));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
