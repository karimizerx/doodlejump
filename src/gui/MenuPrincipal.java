package gui;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class MenuPrincipal extends JPanel {

    Vue view;
    JButton buttonReprendre, buttonExit;
    JPanel menu;

    public MenuPrincipal(Vue v) {
        this.setSize(new Dimension(150, 150));
        view = v;
        super.setBackground(Color.BLACK);
        super.setOpaque(true);
        menu = createMenuRetour();
        this.add(menu);
        Border bordera = BorderFactory.createLineBorder(Color.RED, 1);
        Border borderb = BorderFactory.createLineBorder(Color.BLUE, 1);
        menu.setBorder(bordera);
        this.setBorder(borderb);
    }

    public JPanel createMenuRetour() {
        // Initalisation du panel
        JPanel m = new JPanel();
        m.setLayout(new GridLayout(2, 1));
        m.setPreferredSize(new Dimension(150, 50));

        // Initialisation des élements
        buttonReprendre = new JButton("Reprendre");
        buttonReprendre.setPreferredSize(new Dimension(100, 50));
        buttonExit = new JButton("Quitter");
        buttonExit.setPreferredSize(new Dimension(100, 50));

        // AJout des élements dans le panel
        m.add(buttonReprendre);
        m.add(buttonExit);

        return m;
    }

    public void controlButton() {
        buttonExit.addActionListener(e -> {
            view.removeAll();
            view.repaint();
        });
    }
}
