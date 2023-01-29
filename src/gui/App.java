package gui;

import javax.swing.JFrame;

import gameobjects.Fusee;
//import gameobjects.Items;

public class App extends JFrame {

    public static void main(String[] args) {
        Fusee fus = new Fusee(0, 0, 0, 0, 0);

        System.out.println("Le nom de la classe Items est : " + fus.getClass().getName());
    }
}