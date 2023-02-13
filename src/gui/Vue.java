package gui;

// Import d'autres dossiers
import gameobjects.*;

// Import de packages java
import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

// S'occupe d'afficher les éléments du terrain
public class Vue extends JPanel implements Runnable, KeyListener {

    public static boolean isRunning;
    private Thread thread; // La thread reliée à ce pannel, qui lance l'exécution
    private String chemin = (new File("gui/images/packBase/")).getAbsolutePath();
    private BufferedImage view, terrainView, platformeBaseView, platformeMobileView, scoreView, scoreBackgroundView;
    private ArrayList<ArrayList<BufferedImage>> viewList;
    // isRight/Left gère les boutons appuyés, isInert gère le relâchement
    private boolean isRight, isInertRight, isLeft, isInertLeft, pause;
    private Terrain terrain;
    private JFrame menuPause;
    public double deltaTime = 5;

    public Vue(Terrain ter) {
        this.terrain = ter;
        // Taille du panel
        this.setPreferredSize(new Dimension((int) terrain.getWidth(), (int) terrain.getHeight()));
        // Gestion d'évènements boutons
        this.addKeyListener(this);
        this.pause = false;
    }

    // Méthodes de la classe

    // Initialise toutes les images du jeu
    private void init() {
        // view est l'image qui contiendra toutes les autres
        view = new BufferedImage((int) terrain.getWidth(), (int) terrain.getHeight(), BufferedImage.TYPE_INT_RGB);
        viewList = new ArrayList<ArrayList<BufferedImage>>();

        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                terrainView = ImageIO.read(new File(chemin + "/background/background.png"));
                platformeBaseView = ImageIO.read(new File(chemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(chemin + "/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/background/scoreBackground.png"));

                for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
                    Joueur joueur = terrain.getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On stock dans ListAux les images liées à chaque joueur et qui ne changent
                    // jamais, i.e le perso et le nom, contrairement au score
                    ArrayList<BufferedImage> viewListAux = new ArrayList<BufferedImage>();
                    // L'élément de rang 0 contient l'image du perso
                    viewListAux.add(ImageIO.read(new File(chemin + "/personnages/doodleNinja.png")));
                    // Les autres contiennent les lettres du nom du joueur
                    for (int j = 0; j < nom.length(); ++j) {
                        char c = (nom.charAt(j) == ' ') ? '0' : nom.charAt(j);
                        BufferedImage lv = ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                        viewListAux.add(lv);
                    }
                    viewList.add(viewListAux);
                }
            } catch (Exception e) {
                terrainView = ImageIO.read(new File("src/gui/images/background/background.png"));
                platformeBaseView = ImageIO.read(new File("src/gui/images/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File("src/gui/images/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File("src/gui/images/background/scoreBackground.png"));

                for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
                    Joueur joueur = terrain.getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On stock dans ListAux les images liées à chaque joueur et qui ne changent
                    // jamais, i.e le perso et le nom, contrairement au score
                    ArrayList<BufferedImage> viewListAux = new ArrayList<BufferedImage>();
                    // L'élément de rang 0 contient l'image du perso
                    viewListAux.add(ImageIO.read(new File("src/gui/images/personnages/doodleNinja.png")));
                    // Les autres contiennent les lettres du nom du joueur
                    for (int j = 0; j < nom.length(); ++j) {
                        BufferedImage lv = ImageIO
                                .read(new File("src/gui/images/lettres/lettre" + nom.charAt(j) + ".png"));
                        viewListAux.add(lv);
                    }
                    viewList.add(viewListAux);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Dessine toutes les images
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

        // Affichage des personnages + Nom
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            ArrayList<BufferedImage> jImData = viewList.get(i); // On récupère les données liées au joueur
            Personnage p = terrain.getListeJoueurs().get(i).getPerso();
            g2.drawImage(jImData.get(0), (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);
            int c = (int) ((15 * (jImData.size() - 1)) - p.getWidth()) / 2; // Pour placer le nom au centre du perso
            for (int j = 1; j < jImData.size(); ++j) {
                g2.drawImage(jImData.get(j), (int) (p.getX() - c + (15 * (j - 1))), (int) p.getY() - 15, 15, 15, null);
            }
        }

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);
        g.dispose(); // On libère les ressource
    }

    // Gère le cas de fin du jeu
    public boolean endGame() {
        boolean isFin = false;
        // Si un joueur à perdu, c'est fini
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            isFin = (j.getPerso().getY() + j.getPerso().getHeight() > this.getHeight()) ? true : false;
        }
        return isFin;
    }

    // Met à jour l'affichage
    public void update(double dTime) {
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            Personnage p = j.getPerso();
            // Gère les boutons flèches, avec inertie
            // Dans qu'on appuie, on set la vitesse à ± 4, et on avance de cette distance
            if (isRight) {
                p.setDx(+5);
                p.setX(p.getX() + p.getDx());
            } else if (isLeft) {
                p.setDx(-5);
                p.setX(p.getX() + p.getDx());
            } else if (isInertRight && p.getDx() > 0) { // Si on arrête d'appuyer,
                p.setDx(p.getDx() - 0.24); // la vitesse ralentie petit à petit jusqu'à devenir nulle
                p.setX(p.getX() + p.getDx());
            } else if (isInertLeft && p.getDx() < 0) {
                p.setDx(p.getDx() + 0.24);
                p.setX(p.getX() + p.getDx());
            } else {
                this.isInertLeft = false;
                this.isInertRight = false;
                p.setDx(0);
            }
        }
        terrain.update(dTime);
    }

    // Fait tourner le jeu
    // Cette méthode contient les traitements
    @Override
    public void run() {
        try {
            // Demande à ce que ce composant obtienne le focus.
            // Le focus est le fait qu'un composant soit sélectionné ou pas.
            // Le composant doit être afficheable (OK grâce à addNotify())
            this.requestFocusInWindow();
            init(); // Initialisation des images
            double cnt = 0.0; // Compteur du nombre d'update
            double acc = 0.0; // Accumulateur qui va gérer les pertes de temps
            long t0 = System.currentTimeMillis(); // Temps actuel
            while (isRunning) { // Tant que le jeu tourne
                // if (!pause) // Tant qu'on appuie pas sur pause
                long t1 = System.currentTimeMillis();
                long t = t1 - t0;
                t0 = System.currentTimeMillis();
                acc += t;
                while (t > deltaTime) {
                    update(deltaTime); // On met à jour les variables
                    // On retire 1 Δ à chaque update. Si le reste > 0 & < Δ, ça veut dire qu'on a
                    // un retard, qu'on stock pour l'ajouter à l'étape suivante.
                    // Si on a reste > Δ, on relance cette boucle
                    acc -= deltaTime;
                    cnt += deltaTime; // On accumule le nombre d'update
                }
                afficheImage(); // On affiche les images une fois les données update
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

    // Rend ce composant affichable en le connectant à une ressource d'écran
    @Override
    public void addNotify() {
        super.addNotify();
        // Si on a toujours pas lancer le jeu
        if (this.thread == null) {
            // Créer une nouvelle instance en précisant les traitements à exécuter (run)
            // This est l'objet qui implémente Runnable (run()), contenant les traitements
            this.thread = new Thread(this);
            isRunning = true; // Indique le jeu est lancé
            this.thread.start(); // Invoque la méthode run()
        }
    }

    // Gestion des boutons

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

    @Override
    public void keyPressed(KeyEvent e) { // On est actuellement entrain d'appuyer sur des boutons
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.isRight = true;
            this.isInertRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.isLeft = true;
            this.isInertLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            pause();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // On relâche les boutons
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.isRight = false;
            this.isInertRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.isLeft = false;
            this.isInertLeft = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}