package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.nimbus.*;

import gameobjects.*;

public class Game extends JFrame{


    Vue view;
    
    public Game(){
        Personnage p = new Personnage(250, 300, 100, 100, -10);
        Joueur j = new Joueur(p);
        Terrain rt = new Terrain(j, 933, 600);
        JFrame w = new JFrame("Doodle Jump de J'espère");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Vue(rt));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
