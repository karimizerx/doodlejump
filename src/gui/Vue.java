package gui;

import javax.swing.*;

import gameobjects.Joueur;
import gameobjects.Personnage;
import gameobjects.Plateforme;
import gameobjects.PlateformeBase;
import gameobjects.Terrain;
import multiplayer.ThreadMouvement;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class Vue extends JPanel implements Runnable, KeyListener{

    public static boolean isRunning;
    private Thread thread;
    private String chemin = (new File("gui/images/")).getAbsolutePath();
    private BufferedImage view, terrainView, platformeBaseView, platformeMobileView, persoView, scoreView,scoreBackgroundView;

    boolean isMenu, isEsc,pause=false;
    Terrain terrain;
    JFrame menuPause;
    ThreadMouvement threadMvt=null;



    public Vue(Terrain ter) {
        this.terrain = ter;
        // Taille du panel
        this.setPreferredSize(new Dimension((int) terrain.getWidth(), (int) terrain.getHeight()));
        // Gestion d'évènements boutons
        this.addKeyListener(this);
    }

    private void init() {
        // view est l'image qui contiendra toutes les autres
        view = new BufferedImage((int) terrain.getWidth(), (int) terrain.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                terrainView = ImageIO.read(new File(chemin + "/background/background.png"));
                platformeBaseView = ImageIO.read(new File(chemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(chemin + "/plateformes/plateformeMobile.png"));
                persoView = ImageIO.read(new File(chemin + "/personnages/doodleNinja.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/background/scoreBackground.png"));
            } catch (Exception e) {
                terrainView = ImageIO.read(new File("src/gui/images/background/background.png"));
                platformeBaseView = ImageIO.read(new File("src/gui/images/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File("src/gui/images/plateformes/plateformeMobile.png"));
                persoView = ImageIO.read(new File("src/gui/images/personnages/doodleNinja.png"));
                scoreBackgroundView = ImageIO.read(new File("src/gui/images/background/scoreBackground.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    public void afficheImage() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(terrainView, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);

        // Affichage des plateformes
        for (Plateforme pf : terrain.getPlateformesListe()) {
            BufferedImage pfV = (pf instanceof PlateformeBase) ? platformeBaseView : platformeMobileView;
            g2.drawImage(pfV, (int) pf.getX(), (int) pf.getY(), (int) pf.getWidth(), (int) pf.getHeight(), null);
        }

        // Affichage du Score : seulement s'il n'y a qu'un joueur
        if (terrain.getListeJoueurs().size() == 1) {
            String score = String.valueOf(terrain.getListeJoueurs().get(0).getScore());
            g2.drawImage(scoreBackgroundView, 2, 2, 60 + (30 * (score.length() - 1)), 55, null);
            for (int i = 0; i < score.length(); ++i) {
                try {
                    try {
                        scoreView = ImageIO.read(new File(chemin + "/chiffres/ch" + score.charAt(i) + ".png"));

                    } catch (Exception e) {
                        scoreView = ImageIO.read(new File("src/gui/images/chiffres/ch" + score.charAt(i) + ".png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(scoreView, 5 + (25 * i), 5, 50, 50, null);
            }
        }

        // Affichage des personnages
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            Personnage p = j.getPerso();
            g2.drawImage(persoView, (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);
        }

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);
        g.dispose(); // On libère les ressource
    }


    public void update() {
        
        // Gère les boutons flèches
        for(Joueur j :terrain.getListeJoueurs()){
            Personnage p = j.getPerso();
            if (p.isRight) {
                p.setX(p.getX() + 5);
            } else if (p.isLeft) {
                p.setX(p.getX() - 5);
            }
        }
        terrain.update();
    }

    @Override
    public void run() {
        try {
            // Demande à ce que ce composant obtienne le focus.
            // Le focus est le fait qu'un composant soit sélectionné ou pas.
            // Le composant doit être afficheable (OK grâce à addNotify())
            this.requestFocusInWindow();
            init(); // Initialisation des images
            if(terrain.multiplayer){
                Thread t=new Thread(new ThreadMouvement(terrain));
                t.start();
            }
            while (isRunning) { // Tant que le jeu tourne
                if (!pause) // Tant qu'on appuie pas sur pause
                    update(); // On met à jour les variables
                afficheImage(); // On affiche les images
                Thread.sleep(5); // Temps de stop de la thread, i.e d'update (en ms)
            }
            if (endGame()) { // Si c'est la fin du jeu
                if (terrain.getListeJoueurs().size() == 1) { // S'il n'y a qu'1 joueur, on affiche le score/LB
                    Joueur j = terrain.getListeJoueurs().get(0);
                    String score = String.valueOf(j.getScore());
                    System.out.println("Score à cette manche : " + j.getScore());
                    Classement c = new Classement();
                    c.ajoutClassement(j.getNom(), score);
                    c.afficherClassement();
                }
                this.removeAll(); // On retire tout
                this.repaint(); // On met à jour l'affichage
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean endGame() {
        boolean isFin = false;
        // Si un joueur à perdu, c'est fini
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            isFin = (j.getPerso().getY() + j.getPerso().getHeight() > this.getHeight()) ? true : false;
        }
        return isFin;
    }

    // Gestion des boutons
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Personnage p;
        Personnage pB;
        if((!terrain.multiplayer))
        p=terrain.getListeJoueurs().get(0).getPerso();
        else p=terrain.getListeJoueurs().get(terrain.playerID).getPerso();

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                p.isRight = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                p.isLeft = true;
            }
            if(terrain.getListeJoueurs().size()==2){
                pB=terrain.getListeJoueurs().get(1).getPerso();
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    pB.isRight = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    pB.isLeft = true;
                }
            }
        

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            pause();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Personnage p;
        Personnage pB;
        if((!terrain.multiplayer))
        p=terrain.getListeJoueurs().get(0).getPerso();
        else p=terrain.getListeJoueurs().get(terrain.playerID).getPerso();

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                p.isRight = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                p.isLeft = false;
            }
            if(terrain.getListeJoueurs().size()==2){
                pB=terrain.getListeJoueurs().get(1).getPerso();
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    pB.isRight = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    pB.isLeft = false;
                }
            }
    }

    public void retournMenu() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (0 <= e.getX() && e.getX() <= 100 && 0 <= e.getY() && e.getY() <= 100) {
                    isRunning = false;
                    isMenu = true;
                }
            }
        });
    }


        
    private void pause() {
        this.pause = !this.pause;
        this.menuPause = new JFrame();
        this.menuPause.setBounds((int) terrain.getWidth() * 3 / 2 - 50, (int) terrain.getHeight() / 2 - 60, 150, 120);
        this.menuPause.setResizable(false);
        this.menuPause.setLayout(new FlowLayout());
        this.menuPause.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JButton cont = new JButton("Continuer");
        JButton exit = new JButton("Menu principal");

        this.menuPause.add(cont);
        this.menuPause.add(exit);
        this.menuPause.setVisible(true);

        cont.addActionListener(ev -> {
            this.menuPause.dispose();
            this.pause = !this.pause;
        });

        exit.addActionListener(ev -> {
            this.menuPause.dispose();
            JFrame retourMenu = new App();
            retourMenu.setVisible(true);
        });
    }



}