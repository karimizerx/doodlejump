package gui;

// Import d'autres dossiers
import gameobjects.*;

// Import de packages java
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;
import javax.swing.event.MouseInputListener;

// Représente l'état où le jeu est au niveau d'une "GAME".
public class Game extends Etat implements MouseInputListener {

    boolean mouseControls = false;

    public Game(Vue vue) {
        super(vue);
    }

    // Initialise les images qui ne changeront jamais.
    @Override
    public void initFixe() {
        // On initialise les variables du mode multijoueurs.
        this.vue.setMultijoueur(false);
        this.vue.setHost(false);
    }

    // Initialise les images & les autres variables.
    @Override
    public void init() {
        // Stock des listes qui elles-mêmes stockes les données d'image de chaque joueur
        // & qui ne changent jamais, i.e le perso et le nom, contrairement au score.
        this.vue.setJoueurDataList(new ArrayList<ArrayList<BufferedImage>>());

        try {
            try {
                this.vue.setTerrainView(
                        ImageIO.read(new File(this.vue.getChemin() + "/background/terrainBackground.png")));
                this.vue.setPlatformeBaseView(
                        ImageIO.read(new File(this.vue.getChemin() + "/plateformes/plateformeBase.png")));
                this.vue.setPlatformeMobileView(
                        ImageIO.read(new File(this.vue.getChemin() + "/plateformes/plateformeMobile.png")));
                this.vue.setScoreBackgroundView(
                        ImageIO.read(new File(this.vue.getChemin() + "/background/scoreBackground1.png")));
                this.vue.setProjectileView(ImageIO.read(new File(this.vue.getChemin() + "/items/projectile.png")));
                this.vue.setFuseeView(ImageIO.read(new File(this.vue.getChemin() + "/items/fusee.png")));
                this.vue.setHelicoView(ImageIO.read(new File(this.vue.getChemin() + "/items/helico.png")));
                this.vue.setProjectileView(ImageIO.read(new File(this.vue.getChemin() + "/items/projectile.png")));
                this.vue.setMonstre1View(ImageIO.read(new File(this.vue.getChemin() + "/items/monstre1.png")));
                this.vue.setMonstre2View(ImageIO.read(new File(this.vue.getChemin() + "/items/monstre2.png")));
                this.vue.setcoinView(ImageIO.read(new File(this.vue.getChemin() + "/items/coin.png")));

            } catch (Exception e) {
                this.vue.setTerrainView(
                        ImageIO.read(new File(this.vue.getWinchemin() + "/background/terrainBackground.png")));
                this.vue.setPlatformeBaseView(
                        ImageIO.read(new File(this.vue.getWinchemin() + "/plateformes/plateformeBase.png")));
                this.vue.setPlatformeMobileView(
                        ImageIO.read(new File(this.vue.getWinchemin() + "/plateformes/plateformeMobile.png")));
                this.vue.setScoreBackgroundView(
                        ImageIO.read(new File(this.vue.getWinchemin() + "/background/scoreBackground1.png")));
                this.vue.setProjectileView(ImageIO.read(new File(this.vue.getWinchemin() + "/items/projectile.png")));
                this.vue.setFuseeView(ImageIO.read(new File(this.vue.getWinchemin() + "/items/fusee.png")));
                this.vue.setHelicoView(ImageIO.read(new File(this.vue.getWinchemin() + "/items/helico.png")));
                this.vue.setProjectileView(ImageIO.read(new File(this.vue.getWinchemin() + "/items/projectile.png")));
                this.vue.setMonstre1View(ImageIO.read(new File(this.vue.getWinchemin() + "/items/monstre1.png")));
                this.vue.setMonstre2View(ImageIO.read(new File(this.vue.getWinchemin() + "/items/monstre2.png")));
                this.vue.setcoinView(ImageIO.read(new File(this.vue.getWinchemin() + "/items/coin.png")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Double try_catch pour gérer la différence entre windows & linux.
        try {
            try {

                // On remplit les données d'image de tous les joueurs.
                for (int i = 0; i < this.vue.getTerrain().getListeJoueurs().size(); ++i) {
                    Joueur joueur = this.vue.getTerrain().getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On a une liste qui ne contient que l'image du perso.
                    ArrayList<BufferedImage> persoData = new ArrayList<BufferedImage>();
                    persoData.add(ImageIO.read(new File(this.vue.getChemin() + "/personnages/persoBase.png")));
                    // Suivie d'une liste qui contient le nom du joueur (i.e toutes les lettres).
                    ArrayList<BufferedImage> nomData = createImageOfMot(nom);
                    this.vue.getJoueurDataList().add(persoData);
                    this.vue.getJoueurDataList().add(nomData);
                }
            } catch (Exception e) {
                // On remplit les données d'image de tous les joueurs.
                for (int i = 0; i < this.vue.getTerrain().getListeJoueurs().size(); ++i) {
                    Joueur joueur = this.vue.getTerrain().getListeJoueurs().get(i);
                    String nom = joueur.getNom().toLowerCase();
                    // On a une liste qui ne contient que l'image du perso.
                    ArrayList<BufferedImage> persoData = new ArrayList<BufferedImage>();
                    persoData.add(ImageIO.read(new File(this.vue.getWinchemin() + "/personnages/persoBase.png")));
                    // Suivie d'une liste qui contient le nom du joueur (i.e toutes les lettres).
                    ArrayList<BufferedImage> nomData = createImageOfMot(nom);
                    this.vue.getJoueurDataList().add(persoData);
                    this.vue.getJoueurDataList().add(nomData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update les images & autres variables.
    @Override
    public void update() {
        for (int i = 0; i < this.vue.getTerrain().getListeJoueurs().size(); ++i) {
            Personnage p = this.vue.getTerrain().getListeJoueurs().get(i).getPerso();
            // Gère les boutons flèches, avec inertie.
            // Quand on appuie, on set la vitesse à ± vitesse, et on avance de cette
            // distance.
            double vitesse = this.vue.getTerrain().getWidth() * 5 / 640;
            p.setInertie(this.vue.isInertie() ? this.vue : null);
            if (p.isRight()) {
                p.setDx(+vitesse);
                p.setX(p.getX() + p.getDx());
            } else if (p.isLeft()) {
                p.setDx(-vitesse);
                p.setX(p.getX() + p.getDx());
            } else if (p.isInertRight() && p.getDx() > 0) { // Si on arrête d'appuyer :
                p.setDx(p.getDx() - p.getRalentissement()); // la vitesse ralentie petit à petit jusqu'à devenir nulle.
                p.setX(p.getX() + p.getDx());
            } else if (p.isInertLeft() && p.getDx() < 0) {
                p.setDx(p.getDx() + p.getRalentissement());
                p.setX(p.getX() + p.getDx());
            } else {
                p.setInertRight(false);
                p.setInertLeft(false);
                p.setDx(0);
            }
            if (p.isShoot() && p.iscanShoot()) { // Si on tire, on ne tire plus.
                p.tirer(0.046875 * this.vue.getTerrain().getWidth(), 0.02923397661 * this.vue.getTerrain().getHeight(),
                        0, -this.vue.getDeltaTime());
                p.setShoot(false);
            }
        }

        this.vue.getTerrain().update(this.vue.getDeltaTime()); // On update du terrain.
    }

    // Affiche les images.
    @Override
    public void affiche(Graphics g) { // Prend en argument le contexte graphique de la vue.
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();

        int tw = (int) this.vue.getTerrain().getWidth(), th = (int) this.vue.getTerrain().getHeight();

        // Affichage terrain.
        g2.drawImage(this.vue.getTerrainView(), 0, 0, tw, th, null);

        // Affichage des plateformes.
        for (Plateforme pf : this.vue.getTerrain().getPlateformesListe()) {
            BufferedImage pfV = (pf instanceof PlateformeBase) ? this.vue.getPlatformeBaseView()
                    : this.vue.getPlatformeMobileView();
            g2.drawImage(pfV, (int) pf.getX(), (int) pf.getY(), (int) pf.getWidth(), (int) pf.getHeight(), null);
            if (pf.getItem() != null) {
                Items it = pf.getItem();
                BufferedImage itv = (it instanceof Fusee) ? this.vue.getFuseeView()
                        : this.vue.getProjectileView();
                g2.drawImage(itv, (int) it.getX(), (int) it.getY(), (int) it.getWidth(), (int) it.getHeight(), null);
            }
        }

        // Affichage des items.
        for (Plateforme pf : this.vue.getTerrain().getPlateformesListe()) {
            if (pf.getItem() != null) {
                Items it = pf.getItem();
                BufferedImage itv = (it instanceof Fusee) ? this.vue.getFuseeView()
                        : (it instanceof Helicoptere) ? this.vue.getHelicoView() : this.vue.getProjectileView();
                g2.drawImage(itv, (int) it.getX(), (int) it.getY(), (int) it.getWidth(), (int) it.getHeight(), null);
            }
        }

        for (Joueur j : this.vue.getTerrain().getListeJoueurs()) {
            Personnage p = j.getPerso();
            if (p.getItem() != null) {
                Items it = p.getItem();
                BufferedImage itv = (it instanceof Fusee) ? this.vue.getFuseeView()
                        : (it instanceof Helicoptere) ? this.vue.getHelicoView() : this.vue.getProjectileView();
                g2.drawImage(itv, (int) it.getX(), (int) it.getY(), (int) it.getWidth(), (int) it.getHeight(), null);
            }
        }

        // Affichage du Score & de la monnaie : seulement s'il n'y a qu'un joueur.
        if (this.vue.getTerrain().getListeJoueurs().size() == 1) {
            Joueur j = this.vue.getTerrain().getListeJoueurs().get(0);
            String score = String.valueOf(j.getScore());
            int sw = (int) (tw * 0.09375), sh = (int) (th * 0.0536062378);
            g2.drawImage(this.vue.getScoreBackgroundView(), 2, 2, sw + (sw / 2 * (score.length() - 1)), sh, null);
            ArrayList<BufferedImage> scoreView = createImageOfMot(score);
            int x = (int) (tw * 0.0078125); // Variable pour adapter en fonction de la résolution
            afficheMot(g2, scoreView, x, 5, x * 10, x * 10, 5 * x, 0);

            // Affichage de la monnaie.
            String argent = String.valueOf(j.getMonnaie());
            g2.drawImage(this.vue.getcoinView(), tw - 50, 2, 50, 50, null);
            ArrayList<BufferedImage> argentView = createImageOfMot(argent);
            int ecartorigine = 5 * x;
            // La position du premier chiffre dépend de la longeur de "argent".
            int xorigine = (int) (tw - 50 - 10 * x) - ecartorigine * (argent.length() - 1);
            afficheMot(g2, argentView, xorigine, 5, x * 10, x * 10, ecartorigine, 0);
        }

        // Affichage des personnages + nom.
        for (int i = 0; i < this.vue.getTerrain().getListeJoueurs().size() * 2; i = i + 2) {
            // On récupère les données liées au joueur.
            BufferedImage jPersoData = this.vue.getJoueurDataList().get(i).get(0);
            ArrayList<BufferedImage> jNomData = this.vue.getJoueurDataList().get(i + 1);
            Personnage p = this.vue.getTerrain().getListeJoueurs().get(i / 2).getPerso();
            g2.drawImage(jPersoData, (int) p.getX(), (int) p.getY(), (int) p.getWidth(), (int) p.getHeight(), null);
            int c = (int) ((20 * (jNomData.size() - 1)) - p.getWidth()) / 2; // Pour placer le nom au centre du perso
            afficheMot(g2, jNomData, (int) (p.getX() - c), (int) p.getY() - 15, 20, 20, 15, 10);
        }

        // Affichage des projectiles.
        for (Joueur j : this.vue.getTerrain().getListeJoueurs()) {
            Personnage pers = j.getPerso();
            for (Projectile pro : pers.getListProjectiles()) {
                g2.drawImage(this.vue.getProjectileView(), (int) pro.getX(), (int) pro.getY(), (int) pro.getWidth(),
                        (int) pro.getHeight(), null);
            }
        }

        // Affichage des monstres.
        for (Monstre m : this.vue.getTerrain().getMontresArrayList()) {
            switch (m.getId()) {
                case 1:
                    g2.drawImage(this.vue.getMonstre1View(), (int) m.getX(), (int) m.getY(), (int) m.getWidth(),
                            (int) m.getHeight(), null);
                    break;
                case 2:
                    g2.drawImage(this.vue.getMonstre2View(), (int) m.getX(), (int) m.getY(), (int) m.getWidth(),
                            (int) m.getHeight(), null);
                    break;
                case 3:
                    g2.drawImage(null, (int) m.getX(), (int) m.getY(), (int) m.getWidth(), (int) m.getHeight(), null);
                    break;
                default:
                    g2.drawImage(this.vue.getMonstre1View(), (int) m.getX(), (int) m.getY(), (int) m.getWidth(),
                            (int) m.getHeight(), null);
                    break;
            }
        }

        // Affichage des coins.
        for (Coins c : this.vue.getTerrain().getCoins()) {
            g2.drawImage(this.vue.getcoinView(), (int) c.getX(), (int) c.getY(), (int) c.getWidth(),
                    (int) c.getHeight(), null);
        }

        // Affichage final.
        g.drawImage(this.vue.getView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        g.dispose(); // On libère les ressources.
    }

    // Fait tourner cet état.
    @Override
    public void running() {
        removelistners();
        if (mouseControls) {
            vue.addMouseListener(this);
            vue.addMouseMotionListener(this);
        }

        // Gestion de l'ups constant
        this.vue.setDeltaTime(10); // Le temps nécessaire pour update une GAME.
        double cnt = 0.0; // Compteur du nombre d'updates.
        double acc = 0.0; // Accumulateur qui va gérer les pertes de temps.
        long t0 = System.currentTimeMillis(); // Temps actuel.
        while (Vue.isRunningGame) { // Tant que la GAME tourne :
            long t1 = System.currentTimeMillis();
            long t = t1 - t0;
            if (this.vue.getTerrain().isPause())
                t = 0;
            t0 = System.currentTimeMillis();
            acc += t;
            while (acc > this.vue.getDeltaTime()) { // Si on peut update :
                update(); // On met à jour les variables.
                // On retire 1 deltaTime à chaque update. Si le reste > 0 & < Δ, ça veut dire
                // qu'on a un retard, qu'on stock pour l'ajouter à l'étape suivante.
                // Si on a reste > Δ, on relance cette boucle
                acc -= this.vue.getDeltaTime();
                cnt += this.vue.getDeltaTime(); // On accumule le nombre d'update.
            }
            affiche(this.vue.getGraphics()); // On affiche les images une fois les données update.
            if(this.vue.getTerrain().isMultiDone()) Vue.isRunningGame=false;
        }
    }

    // Gestion des boutons.
    @Override
    public void keyControlPressed(KeyEvent e) { // KeyEvent e de la vue.
        if (mouseControls)
            return;

        /// Gestion de la pause :
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // Si on appuie sur ECHAP :
            this.pause(); // On met le jeu en pause.
        }

        Personnage p1, p2;
        if (!this.vue.getTerrain().multiplayer || (this.vue.getTerrain().multiplayer && this.vue.getTerrain().isHost)) // Si on ne joue pas en multijoueur :
            p1 = this.vue.getTerrain().getListeJoueurs().get(0).getPerso(); // On récupère le personnage du 1er joueur.
        else 
            p1 = this.vue.getTerrain().getListeJoueurs().get(1).getPerso();

        /// Gestion des déplacements horizontales des personnages :
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Si on clique sur la flèche droite :
            p1.setRight(true); // On indique qu'on avance vers la droite.
            p1.setInertRight(false); // On stop l'inertie (le ralentissement).
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Si on clique sur la flèche gauche :
            p1.setLeft(true); // On indique qu'on avance vers la gauche.
            p1.setInertLeft(false); // On stop l'inertie (le ralentissement).
        }

        // On fait la même chose avec "Q" & "D" s'il y a 2 joueurs.
        if (this.vue.getTerrain().getListeJoueurs().size() == 2 && !this.vue.getTerrain().multiplayer) {
            p2 = this.vue.getTerrain().getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_D) {
                p2.setRight(true);
                p2.setInertRight(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
                p2.setLeft(true);
                p2.setInertLeft(false);
            }
        }

        /// Gestion des tires de projectiles :
        // Si on a le droit de tirer et qu'on tire :
        if (e.getKeyCode() == KeyEvent.VK_UP && p1.iscanShoot()) {
            p1.setShoot(true); // On indique qu'on vient de tirer.
            p1.setcanShoot(false); // On a pas le droit de re-tirer.
        }

        if (this.vue.getTerrain().getListeJoueurs().size() == 2) { // On fait la même chose avec "Z" s'il y a 2 joueurs.
            p2 = this.vue.getTerrain().getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_Z && p2.iscanShoot()) {
                p2.setShoot(true); // On indique qu'on vient de tirer.
                p2.setcanShoot(false); // On a pas le droit de re-tirer.
            }
        }

    }

    @Override
    public void keyControlReleased(KeyEvent e) {
        if (mouseControls)
            return;

        Personnage p1, p2;
        if (!this.vue.getTerrain().multiplayer) // Si on ne joue pas en multijoueur :
            p1 = this.vue.getTerrain().getListeJoueurs().get(0).getPerso(); // On récupère le personnage du 1er joueur.
        else
            p1 = this.vue.getTerrain().getListeJoueurs().get(this.vue.getTerrain().playerID).getPerso();

        /// Gestion des déplacements horizontales des personnages :
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Si on relâche pendant que l'on se déplace vers la droite :
            p1.setRight(false); // On arrête de se déplacer.
            p1.setInertRight(true); // On lance le ralentissement (inertie).
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Si on relâche pendant que l'on se déplace vers la gauche :
            p1.setLeft(false); // On arrête de se déplacer.
            p1.setInertLeft(true); // On lance le ralentissement (inertie).
        }

        // On fait la même chose avec "Q" & "D" s'il y a 2 joueurs.
        if (this.vue.getTerrain().getListeJoueurs().size() == 2) {
            p2 = this.vue.getTerrain().getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_D) {
                p2.setRight(false);
                p2.setInertRight(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
                p2.setLeft(false);
                p2.setInertLeft(true);
            }
        }

        /// Gestion des tires de projectiles :
        if (e.getKeyCode() == KeyEvent.VK_UP) // On oblige le joueur à lâcher pour tirer (pour éviter le spam).
            p1.setcanShoot(true); // Dès qu'on lâche, on a de nouveau le droit de tirer.

        if (this.vue.getTerrain().getListeJoueurs().size() == 2) { // On fait la même chose avec "Z" s'il y a 2 joueurs.
            p2 = this.vue.getTerrain().getListeJoueurs().get(1).getPerso();
            if (e.getKeyCode() == KeyEvent.VK_Z) // On oblige le joueur à lâcher pour tirer (pour éviter le spam).
                p2.setcanShoot(true); // Dès qu'on lâche, on a de nouveau le droit de tirer.
        }
    }

    /// Méthodes générales utiles :
    // Crée une partie (en initialisant toutes les variables, i.e le terrain, ...)
    public void createPartie() {
        /// Initialisation des éléments :
        ArrayList<Joueur> ljou = new ArrayList<Joueur>(); // Liste des joueurs.

        /// Ajout des joueurs :
        // Variable (z) pour adapter les dimensions à la résolution d'écran.
        // L'image du perso doit être un carré. On prend la valeure la plus petite.
        double z = ((this.vue.getHeight() * 0.09746) > (this.vue.getWidth() * 0.15625))
                ? (this.vue.getWidth() * 0.15625)
                : (this.vue.getHeight() * 0.09746);
        Personnage p = new Personnage(this.vue.getWidth() / 2, this.vue.getHeight() - z, z, z,
                -(this.vue.getHeight() * 0.0097465887));
        ljou.add(new Joueur(p, this.vue.getNom1()));
        if (this.vue.getNbJoueur() == 2) { // S'il y a 2 joueurs :
            Personnage p2 = new Personnage(this.vue.getWidth() / 2, this.vue.getHeight() - z, z, z,
                    -(this.vue.getHeight() * 0.0097465887));
            ljou.add(new Joueur(p2, this.vue.getNom2()));
        }

        int i = this.vue.isMultijoueur() ? this.vue.isHost() ? 0 : 1 : 0;
        double lvl = (this.vue.getNiveau() == 1) ? 0.0001
                : (this.vue.getNiveau() == 2 ? 0.0003 : (this.vue.getNiveau() == 3 ? 0.0006 : 0.002));
        this.vue.setTerrain(new Terrain(ljou, this.vue.getHeight(), this.vue.getWidth(), this.vue.isHost(),
                this.vue.isMultijoueur(), i, lvl));
        this.vue.getTerrain().setClient(this.vue.getJconnect());
        this.vue.getTerrain().setHost(this.vue.getServeur());
    }

    // Gère le cas de fin de la GAME.
    public boolean endGame() {
        boolean isFin = false;
        if(this.vue.getTerrain()==null) return false;
        // Si un joueur a perdu, c'est fini !
        for (int i = 0; i < this.vue.getTerrain().getListeJoueurs().size(); ++i) {
            Joueur j = this.vue.getTerrain().getListeJoueurs().get(i);
            isFin = (j.getPerso().getY() + j.getPerso().getHeight() > this.vue.getHeight()) ? true : false;
        }
        return isFin;
    }

    // Met le jeu en pause.
    private void pause() {
        if (this.vue.getTerrain().isPause()) {
            this.vue.getTerrain().setPause(false);
        } else {
            this.vue.getTerrain().setPause(true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!mouseControls)
            return;
        Personnage p = this.vue.getTerrain().getMyPlayer().getPerso();
        p.setShoot(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!mouseControls)
            return;
        Personnage p = this.vue.getTerrain().getMyPlayer().getPerso();
        p.setShoot(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!mouseControls)
            return;

        Personnage p = this.vue.getTerrain().getMyPlayer().getPerso();
        if (e.getX() >= p.getX() && e.getX() <= p.getX() + p.getWidth() * 0.78) { // Si on relâche pendant que l'on se
                                                                                  // déplace vers la droite :
            p.setRight(false); // On arrête de se déplacer.
            p.setInertRight(false); // On lance le ralentissement (inertie).
            p.setLeft(false); // On arrête de se déplacer.
            p.setInertLeft(false);
        } else if (e.getX() < p.getX() + p.getWidth() * 0.43) { // Si on relâche pendant que l'on se déplace vers la
                                                                // gauche :
            p.setLeft(true); // On arrête de se déplacer.
            p.setInertLeft(false); // On lance le ralentissement (inertie).
            p.setRight(false);
            p.setInertRight(false);
        } else if (e.getX() > p.getX() + p.getWidth() * 0.43) {
            p.setRight(true); // On arrête de se déplacer.
            p.setInertRight(false); // On lance le ralentissement (inertie).
            p.setLeft(false);
            p.setInertLeft(false);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // if(!mouseControls) return;
        // Personnage p=this.vue.getTerrain().getMyPlayer().getPerso();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}