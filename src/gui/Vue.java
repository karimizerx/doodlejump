package gui;

// Import d'autres dossiers
import gameobjects.*;
import leaderboard.Classement;
import leaderboard.History;
import multiplayer.*;

// Import de packages java
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

// S'occupe d'afficher les éléments du terrain
public class Vue extends JPanel implements Runnable, KeyListener {

    public static boolean isQuitte, isRunning, isMenuDemarrer, isMenuFin;
    private final int width, height;
    private ThreadMouvement threadMvt = null;
    private Thread thread; // La thread reliée à ce pannel, qui lance l'exécution
    private String chemin = (new File("gui/images/packBase/")).getAbsolutePath();
    private BufferedImage view, backgroundView, flecheView, terrainView, platformeBaseView, platformeMobileView,
            scoreView, scoreBackgroundView,
            projectileView;
    private ArrayList<ArrayList<BufferedImage>> viewList, lbView;
    private ArrayList<BufferedImage> buttonJouer, button2joueur, buttonMultiJoueur, buttonLb, buttonQuitter,
            buttonRetourMenu;
    private int space = 0, xfleche, yfleche;
    // isRight/Left gère les boutons appuyés, isInert gère le relâchement
    private Terrain terrain;
    private JFrame menuPause;
    public double deltaTime = 10;
    private JFrame frame;

    public Vue(Game frame) {
        this.frame = frame;
        this.width = frame.getWidth();
        this.height = frame.getHeight();
        this.setPreferredSize(new Dimension(this.width / 3, (int) (this.height * 0.95)));
        // Taille du panel
        // Gestion d'évènements boutons
        this.addKeyListener(this);
    }

    // Méthodes de la classe

    private void createPartie() {

        // Initialisation des éléments
        ArrayList<Joueur> ljou = new ArrayList<Joueur>();
        for (int i = 0; i < 1; ++i) {
            // L'image du perso doit être un carré. On prend la valeure la plus petite
            double z = ((height * 0.09746) > (width * 0.15625)) ? (width * 0.15625) : (height * 0.09746);
            Personnage p = new Personnage(width / 2, height - z, z, z, -(height * 0.0097465887));
            String nomjoueur = "Mizer";
            ljou.add(new Joueur(p, nomjoueur));

        }
        this.terrain = new Terrain(ljou, height, width, false, false, 0);

    }

    /// PARTIE MENU DEMARRER / PAUSE / FIN :

    private ArrayList<BufferedImage> createImage(String mot) {
        ArrayList<BufferedImage> motView = new ArrayList<BufferedImage>();
        mot = mot.toLowerCase();

        for (int i = 0; i < mot.length(); ++i) {
            char c = mot.charAt(i);
            try {
                try {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                } catch (Exception e) {
                    BufferedImage lv = (c == ' ') ? null
                            : ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                    motView.add(lv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return motView;
    }

    // Initialise toutes les images du menu
    private void initMenuDemarrer() {
        // view est l'image qui contiendra toutes les autres
        this.view = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                this.backgroundView = ImageIO.read(new File(chemin + "/background/background1.png"));
                this.flecheView = ImageIO.read(new File(chemin + "/icon/iconfleche.png"));
            } catch (Exception e) {
                this.backgroundView = ImageIO.read(new File("src/gui/images/packBase/background/background1.png"));
                this.flecheView = ImageIO.read(new File("src/gui/images/packBase/icon/iconfleche.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.buttonJouer = createImage("Jouer en solo");
        this.button2joueur = createImage("Jouer a 2");
        this.buttonMultiJoueur = createImage("Mode multijoueurs");
        this.buttonLb = createImage("Classement");
        this.buttonQuitter = createImage("Quitter");
    }

    // Met à jour les images du menu
    private void updateMenuDemarrer() {
        this.xfleche = (10 * width / 100) - 50;
        if (space == 0)
            yfleche = (10 * height / 100);
        if (space == 1)
            yfleche = (10 * height / 100) + 50;
        if (space == 2)
            yfleche = (10 * height / 100) + 100;
        if (space == 3)
            yfleche = (10 * height / 100) + 150;
        if (space == 4)
            yfleche = (10 * height / 100) + 200;
    }

    // Dessine toutes les images
    public void afficheMenuDemarrer() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        int y = (10 * height / 100);
        int x = (10 * width / 100) - 15;
        int w = 30, h = 30;
        // Affichage des boutons
        for (int i = 0; i < buttonJouer.size(); ++i) {
            BufferedImage image = buttonJouer.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < button2joueur.size(); ++i) {
            BufferedImage image = button2joueur.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < buttonMultiJoueur.size(); ++i) {
            BufferedImage image = buttonMultiJoueur.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < buttonLb.size(); ++i) {
            BufferedImage image = buttonLb.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < buttonQuitter.size(); ++i) {
            BufferedImage image = buttonQuitter.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }

        // Affichage de la fleche
        g2.drawImage(flecheView, xfleche, yfleche, w, h, null);

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Lance le menu
    private void runningMenuDemarrer() {
        this.space = 0;
        while (isMenuDemarrer) {
            updateMenuDemarrer();
            afficheMenuDemarrer();
        }
    }

    // Initialise toutes les images du menu
    private void initMenuFin() {
        // view est l'image qui contiendra toutes les autres
        this.view = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        // Double try_catch pour gérer la différence entre windows & linux
        this.buttonRetourMenu = createImage("Revenir au menu");
        this.lbView = new ArrayList<ArrayList<BufferedImage>>();
    }

    // Met à jour les images du menu
    private void updateMenuFin() {
        this.xfleche = (10 * width / 100) - 50;
        if (space == 5)
            yfleche = (80 * height / 100);
        if (space == 6)
            yfleche = (80 * height / 100) + 50;
    }

    // Crée l'affichage à jour du classement Global
    private void updateClassementVue() {
        Classement c = new Classement();
        ArrayList<String[]> cl = c.getLbData();

        int imax = (cl.size() > 10) ? 10 : cl.size();
        for (int i = 0; i < imax; ++i) {
            String n = cl.get(i)[1];
            String s = cl.get(i)[2];

            ArrayList<BufferedImage> rank = createImage(String.valueOf(i + 1));
            ArrayList<BufferedImage> name = createImage(n);
            ArrayList<BufferedImage> score = createImage(s);
            ArrayList<BufferedImage> espace = createImage(" ");

            lbView.add(rank);
            lbView.add(espace);
            lbView.add(name);
            lbView.add(espace);
            lbView.add(score);
            lbView.add(espace);
        }
    }

    // Dessine toutes les images
    public void afficheFin() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        // Affichage terrain
        g2.drawImage(backgroundView, 0, 0, this.width, this.height, null);

        // Affichage du classement
        int y1 = (10 * height / 100);
        int x1 = (10 * width / 100) - 15;
        int w1 = 30, h1 = 30;

        for (int z = 0; z < 10; ++z) {
            System.out.println("Z = " + z);
            for (int i = 0; i < 6; ++i) {
                System.out.println("z;i : " + z + "," + i);
                ArrayList<BufferedImage> ligne = lbView.get(z + i);
                for (int j = 0; j < ligne.size(); ++j) {
                    BufferedImage image = ligne.get(j);
                    if (image != null) {
                        g2.drawImage(image, x1, y1, w1, h1, null);
                        x1 += 20;
                    } else {
                        x1 += 15;
                    }
                }
            }
            y1 = y1 + 50;
            x1 = (10 * width / 100) - 15;
        }

        // Affichage des boutons
        int y = (80 * height / 100);
        int x = (10 * width / 100) - 15;
        int w = 30, h = 30;
        for (int i = 0; i < buttonRetourMenu.size(); ++i) {
            BufferedImage image = buttonRetourMenu.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        y = y + 50;
        x = (10 * width / 100) - 15;
        for (int i = 0; i < buttonQuitter.size(); ++i) {
            BufferedImage image = buttonQuitter.get(i);
            if (image != null) {
                g2.drawImage(image, x, y, w, h, null);
                x += 20;
            } else {
                x += 15;
            }
        }
        // Affichage de la fleche
        g2.drawImage(flecheView, xfleche, yfleche, w, h, null);

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, this.width, this.height, null);
        g.dispose(); // On libère les ressource
    }

    // Lance le menu
    private void runningMenuFin() {
        this.space = 5;
        updateClassementVue();
        while (isMenuFin) {
            updateMenuFin();
            afficheFin();
        }
    }

    /// PARTIE JEU :

    // Initialise toutes les images du menu
    private void initGame() {
        // view est l'image qui contiendra toutes les autres
        view = new BufferedImage((int) terrain.getWidth(), (int) terrain.getHeight(), BufferedImage.TYPE_INT_RGB);
        viewList = new ArrayList<ArrayList<BufferedImage>>();

        // Double try_catch pour gérer la différence entre windows & linux
        try {
            try {
                terrainView = ImageIO.read(new File(chemin + "/background/background1.png"));
                platformeBaseView = ImageIO.read(new File(chemin + "/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO.read(new File(chemin + "/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File(chemin + "/background/scoreBackground1.png"));
                projectileView = ImageIO.read(new File(chemin + "/projectile.png"));

                for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
                    Joueur joueur = terrain.getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On stock dans ListAux les images liées à chaque joueur et qui ne changent
                    // jamais, i.e le perso et le nom, contrairement au score
                    ArrayList<BufferedImage> viewListAux = new ArrayList<BufferedImage>();
                    // L'élément de rang 0 contient l'image du perso
                    viewListAux.add(ImageIO.read(new File(chemin + "/personnages/persoBase.png")));
                    // Les autres contiennent les lettres du nom du joueur
                    for (int j = 0; j < nom.length(); ++j) {
                        char c = (nom.charAt(j) == ' ') ? '0' : nom.charAt(j);
                        BufferedImage lv = ImageIO.read(new File(chemin + "/lettres/lettre" + c + ".png"));
                        viewListAux.add(lv);
                    }
                    viewList.add(viewListAux);
                }
            } catch (Exception e) {
                terrainView = ImageIO.read(new File("src/gui/images/packBase/background/background1.png"));
                platformeBaseView = ImageIO.read(new File("src/gui/images/packBase/plateformes/plateformeBase.png"));
                platformeMobileView = ImageIO
                        .read(new File("src/gui/images/packBase/plateformes/plateformeMobile.png"));
                scoreBackgroundView = ImageIO.read(new File("src/gui/images/packBase/background/scoreBackground1.png"));
                projectileView = ImageIO.read(new File(chemin + "/projectile.png"));

                for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
                    Joueur joueur = terrain.getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On stock dans ListAux les images liées à chaque joueur et qui ne changent
                    // jamais, i.e le perso et le nom, contrairement au score
                    ArrayList<BufferedImage> viewListAux = new ArrayList<BufferedImage>();
                    // L'élément de rang 0 contient l'image du perso
                    viewListAux.add(ImageIO.read(new File("src/gui/images/packBase/personnages/persoBase.png")));
                    // Les autres contiennent les lettres du nom du joueur
                    for (int j = 0; j < nom.length(); ++j) {
                        char c = (nom.charAt(j) == ' ') ? '0' : nom.charAt(j);
                        BufferedImage lv = ImageIO
                                .read(new File("src/gui/images/packBase/lettres/lettre" + c + ".png"));
                        viewListAux.add(lv);
                    }
                    viewList.add(viewListAux);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Met à jour l'affichage
    private void updateGame(double dTime) {
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            Personnage p = j.getPerso();
            // Gère les boutons flèches, avec inertie
            // Dans qu'on appuie, on set la vitesse à ± 4, et on avance de cette distance
            double vitesse = 0.0078125 * terrain.getWidth();
            double ralentissement = 0.000375 * terrain.getWidth();
            if (p.isRight()) {
                p.setDx(+vitesse);
                p.setX(p.getX() + p.getDx());
            } else if (p.isLeft()) {
                p.setDx(-vitesse);
                p.setX(p.getX() + p.getDx());
            } else if (p.isInertRight() && p.getDx() > 0) { // Si on arrête d'appuyer,
                p.setDx(p.getDx() - ralentissement); // la vitesse ralentie petit à petit jusqu'à devenir nulle
                p.setX(p.getX() + p.getDx());
            } else if (p.isInertLeft() && p.getDx() < 0) {
                p.setDx(p.getDx() + ralentissement);
                p.setX(p.getX() + p.getDx());
            } else {
                p.setInertRight(false);
                p.setInertLeft(false);
                p.setDx(0);
            }
            if (p.isSpace() && p.isTirPossible()) { // Si on tire, on ne tire plus
                p.tirer(0.046875 * terrain.getWidth(), 0.02923397661 * terrain.getHeight(), 0, -deltaTime);
                p.setSpace(false);
            }
        }
        terrain.update(dTime);
    }

    // Dessine toutes les images
    public void afficheGame() {
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
            int sw = (int) (terrain.getWidth() * 0.09375), sh = (int) (terrain.getHeight() * 0.0536062378);
            g2.drawImage(scoreBackgroundView, 2, 2, sw + (sw / 2 * (score.length() - 1)), sh, null);
            for (int i = 0; i < score.length(); ++i) {
                try {
                    try {
                        scoreView = ImageIO.read(new File(chemin + "/chiffres/ch" + score.charAt(i) + ".png"));

                    } catch (Exception e) {
                        scoreView = ImageIO
                                .read(new File(chemin + "/chiffres/ch" + score.charAt(i) + ".png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int x = (int) (terrain.getWidth() * 0.0078125); // Variable pour adapter en fonction de la résolution
                g2.drawImage(scoreView, x + (5 * x * i), 5, x * 10, x * 10, null);
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
        for (Joueur j : terrain.getListeJoueurs()) {
            Personnage pers = j.getPerso();
            for (Projectile pro : pers.getListProjectiles()) {
                g2.drawImage(projectileView, (int) pro.getX(), (int) pro.getY(), (int) pro.getWidth(),
                        (int) pro.getHeight(), null);
            }
        }

        // Affichage final
        Graphics g = getGraphics(); // Contexte graphique
        g.drawImage(view, 0, 0, (int) terrain.getWidth(), (int) terrain.getHeight(), null);
        g.dispose(); // On libère les ressource
    }

    // Gère le cas de fin du jeu
    private boolean endGame() {
        boolean isFin = false;
        // Si un joueur à perdu, c'est fini
        for (int i = 0; i < terrain.getListeJoueurs().size(); ++i) {
            Joueur j = terrain.getListeJoueurs().get(i);
            isFin = (j.getPerso().getY() + j.getPerso().getHeight() > this.getHeight()) ? true : false;
        }
        return isFin;
    }

    private void updateClassement() throws IOException {
        Joueur j = terrain.getListeJoueurs().get(0);
        String score = String.valueOf(j.getScore());
        System.out.println("Score à cette manche : " + j.getScore());

        // Mise à jour dans le Classement Global
        Classement c = new Classement();
        c.ajoutClassement(j.getId(), j.getNom(), score);
        c.afficherClassement();

        // Mise à jour dans le Classement Local
        History h = new History();
        h.ajoutClassement(j.getId(), j.getNom(), score);
        h.afficherClassement();
    }

    private void runningGame() {
        // Gestion de l'ups constant
        double cnt = 0.0; // Compteur du nombre d'update
        double acc = 0.0; // Accumulateur qui va gérer les pertes de temps
        long t0 = System.currentTimeMillis(); // Temps actuel
        while (isRunning) { // Tant que le jeu tourne
            if (!terrain.isPause()) { // Tant qu'on appuie pas sur pause
                long t1 = System.currentTimeMillis();
                long t = t1 - t0;
                t0 = System.currentTimeMillis();
                acc += t;
                while (acc > deltaTime) { // Si on peut update
                    updateGame(deltaTime); // On met à jour les variables
                    // On retire 1 Δ à chaque update. Si le reste > 0 & < Δ, ça veut dire qu'on a
                    // un retard, qu'on stock pour l'ajouter à l'étape suivante.
                    // Si on a reste > Δ, on relance cette boucle
                    acc -= deltaTime;
                    cnt += deltaTime; // On accumule le nombre d'update
                }
            }
            afficheGame(); // On affiche les images une fois les données update
        }
    }

    // Fait tourner le jeu
    // Cette méthode contient les traitements
    @Override
    public void run() {
        try {
            while (!isQuitte) {

                // Demande à ce que ce composant obtienne le focus.
                // Le focus est le fait qu'un composant soit sélectionné ou pas.
                // Le composant doit être afficheable (OK grâce à addNotify())
                this.requestFocusInWindow();

                // Initialisation des images
                initMenuDemarrer();
                initMenuFin();
                runningMenuDemarrer();

                createPartie();
                initGame();
                if (terrain.multiplayer) { // Si on est en mode multijoueur
                    Thread t = new Thread(new ThreadMouvement(terrain)); // ???
                    t.start();
                }

                runningGame();

                if (endGame()) { // Si c'est la fin du jeu
                    if (terrain.getListeJoueurs().size() == 1) // S'il n'y a qu'1 joueur, on affiche le score/LB
                        updateClassement();
                    isRunning = false;
                    isMenuFin = true;
                    runningMenuFin();
                }
                System.out.println(isQuitte);
            }
            System.exit(0);
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
            isQuitte = false;
            isMenuDemarrer = true; // Indique le jeu est lancé
            this.thread.start(); // Invoque la méthode run()
        }
    }

    // Gestion des boutons

    private void pause() {
        this.terrain.pause = !this.terrain.pause;
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
            this.terrain.pause = !this.terrain.pause;
        });

        exit.addActionListener(ev -> {
            this.menuPause.dispose();
            JFrame retourMenu = new App();
            retourMenu.setVisible(true);
        });
    }

    @Override
    public void keyPressed(KeyEvent e) { // On est actuellement entrain d'appuyer sur des boutons repaint();

        if (isRunning) {
            Personnage p1;
            Personnage p2;
            if ((!terrain.multiplayer))
                p1 = terrain.getListeJoueurs().get(0).getPerso();
            else
                p1 = terrain.getListeJoueurs().get(terrain.playerID).getPerso();

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                p1.setRight(true);
                p1.setInertRight(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                p1.setLeft(true);
                p1.setInertLeft(false);
            }
            if (terrain.getListeJoueurs().size() == 2) {
                p2 = terrain.getListeJoueurs().get(1).getPerso();
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    p2.setRight(true);
                    p2.setInertRight(false);
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    p2.setLeft(true);
                    p2.setInertLeft(false);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE && p1.isTirPossible()) {
                p1.setSpace(true);// Si on a le droit de tirer et qu'on tire
                p1.setTirPossible(false);// On a pas le droit de re-tirer
                // On indique qu'on vient de tirer
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                pause();
            }
        }

        if (isMenuDemarrer) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                System.out.println(this.space);
                if (this.space == 0) {
                    isMenuDemarrer = false;
                    isRunning = true;
                    repaint();
                }
                if (this.space == 1) {
                }

                if (this.space == 2) {
                }

                if (this.space == 3) {
                    Classement c = new Classement();
                    try {
                        c.afficherClassement();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (this.space == 4) {
                    isQuitte = true;
                    System.exit(0);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.space = (this.space == 0) ? 4 : this.space - 1;
                System.out.println(this.space);
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                this.space = (this.space == 4) ? 0 : this.space + 1;
                System.out.println(this.space);
            }
        }

        if (isMenuFin) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                System.out.println(this.space);
                if (this.space == 5) {
                    isRunning = false;
                    isMenuFin = false;
                    isMenuDemarrer = true;
                    repaint();
                }
                if (this.space == 6) {
                    isQuitte = true;
                    System.exit(0);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.space = (this.space == 5) ? 6 : this.space - 1;
                System.out.println(this.space);
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                this.space = (this.space == 6) ? 5 : this.space + 1;
                System.out.println(this.space);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // On relâche les boutons
        if (isRunning) {
            Personnage p1;
            Personnage p2;
            if ((!terrain.multiplayer))
                p1 = terrain.getListeJoueurs().get(0).getPerso();
            else
                p1 = terrain.getListeJoueurs().get(terrain.playerID).getPerso();

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                p1.setRight(false);
                p1.setInertRight(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                p1.setLeft(false);
                p1.setInertLeft(true);
            }
            if (terrain.getListeJoueurs().size() == 2) {
                p2 = terrain.getListeJoueurs().get(1).getPerso();
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    p2.setRight(false);
                    p2.setInertRight(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    p2.setLeft(false);
                    p2.setInertLeft(true);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                p1.setTirPossible(true);// Dès qu'on lâche, on a de nouveau le droit de tirer.
                // On oblige donc le joueur à lâcher pour tirer
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}