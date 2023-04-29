package gui;

// Import de packages java
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuSetting extends Etat {

    private int pack;

    public MenuSetting(Vue vue) {
        super(vue);
        this.pack = 0;
    }

    @Override
    public void initFixe() {
        this.vue.setMessNiveau(createImageOfMot(String.valueOf(this.vue.getNiveau())));
        this.vue.setMessSkin(createImageOfMot(this.vue.getSkin()));
        this.vue.setMessInertie(createImageOfMot("OUI"));
    }

    @Override
    public void init() {
    }

    @Override
    void update() {
        // Dimensions de la fleche.
        this.vue.setWfleche(this.vue.getWidth() * 30 / 640);
        this.vue.setHfleche(this.vue.getHeight() * 30 / 1026);

        // La fleche a toujours la même coordonnée x.
        this.vue.setXfleche((7 * this.vue.getWidth() / 100) - this.vue.getWfleche());

        // Le placement de la fleche en y dépend de ce qu'elle pointe.
        this.vue.setYfleche((10 * vue.getHeight() / 100) + this.vue.getFleche() * this.vue.getSautLigne());
    }

    @Override
    void affiche(Graphics g) {
        Graphics2D g2 = (Graphics2D) this.vue.getView().getGraphics();

        // Affichage du background.
        g2.drawImage(this.vue.getBackgroundView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        // Affichage des boutons.
        int x = (9 * this.vue.getWidth() / 100), y = (10 * this.vue.getHeight() / 100);

        int w = this.vue.getWidth() * 30 / 640, h = this.vue.getHeight() * 30 / 1026,
                espacement = this.vue.getWidth() * 15 / 640, ecart = this.vue.getWidth() * 20 / 640;

        // Affichage des scores (entre () l'adaptation pour les parties à 2 joueurs):
        // Affichage du score à cette partie (score du joueur 1).
        x = afficheMot(g2, this.vue.getButtonNiveau(), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
        x += espacement;
        x = afficheMot(g2, this.vue.getMessNiveau(), x, y, w, h, ecart, espacement);

        // Affichage du meilleur score local (score du joueur 2).
        y += this.vue.getSautLigne();
        x = (9 * this.vue.getWidth() / 100);
        x = afficheMot(g2, this.vue.getButtonSkin(), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
        x += espacement;
        x = afficheMot(g2, this.vue.getMessSkin(), x, y, w, h, ecart, espacement);

        // Affichage du meilleur score global (vainqueur).
        y += this.vue.getSautLigne();
        x = (9 * this.vue.getWidth() / 100);
        x = afficheMot(g2, this.vue.getButtonInertie(), x, y, w, h, ecart, espacement);
        x = afficheDoublepoint(g2, x, y, this.vue.getWidth() * 7 / 640, this.vue.getHeight() * 7 / 1026);
        x += espacement;
        x = afficheMot(g2, this.vue.getMessInertie(), x, y, w, h, ecart, espacement);

        // Affichage des boutons.
        // y += this.vue.getSautLigne() * 2;
        y += this.vue.getSautLigne();
        x = (9 * this.vue.getWidth() / 100);
        afficheMot(g2, this.vue.getButtonRetourMenu(), x, y, w, h, ecart, espacement);
        x = (9 * this.vue.getWidth() / 100);
        y += this.vue.getSautLigne();
        afficheMot(g2, this.vue.getButtonQuitter(), x, y, w, h, ecart, espacement);

        // Affichage de la fleche.
        g2.drawImage(this.vue.getFlecheView(), this.vue.getXfleche(), this.vue.getYfleche(), this.vue.getWfleche(),
                this.vue.getHfleche(), null);

        // Affichage final.
        g.drawImage(this.vue.getView(), 0, 0, this.vue.getWidth(), this.vue.getHeight(), null);

        g.dispose(); // On libère les ressources.
    }

    @Override
    void running() {
        removelistners();
        this.vue.addMouseListener(this);
        // Initialisation des valeurs initiales des variables avant lancement.
        this.vue.setSautLigne(this.vue.getHeight() * 50 / 1026); // Distance entre 2 lignes.
        this.vue.setFleche(0); // On pointe le premier bouton.

        while (Vue.isSetting) { // Tant que l'on est dans le menu DEMARRER :
            this.update(); // On update.
            this.affiche(this.vue.getGraphics()); // On affiche.
        }
    }

    @Override
    void keyControlPressed(KeyEvent e) {
        /// Gestion du bouton "ENTREE" :
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // L'action du bouton "ENTREE" dépend de ce que l'on pointe :
            if (this.vue.getFleche() == 3) {
                Vue.isSetting = false;
                Vue.isMenuDemarrer = true;
                this.vue.setFleche(-1);
            }
            if (this.vue.getFleche() == 4) { // Si la flèche pointe sur le bouton "Quitter" :
                System.out.println("À la prochaine !");
                Vue.isQuitte = true; // On quitte le jeu.
                System.exit(0); // On ferme toutes les fenêtres & le programme.
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (this.vue.getFleche() == 0) {
                this.vue.setNiveau((this.vue.getNiveau() == 4) ? 1 : this.vue.getNiveau() + 1);
                this.vue.setMessNiveau(createImageOfMot(String.valueOf(this.vue.getNiveau())));
            }
            if (this.vue.getFleche() == 1) {
                this.pack = (this.pack == this.vue.getListPackSkin().size() - 1) ? 0 : this.pack + 1;
                this.vue.setSkin(this.vue.getListPackSkin().get(this.pack));
                this.vue.setMessSkin(createImageOfMot(this.vue.getSkin()));
            }
            if (this.vue.getFleche() == 2) {
                String iner = (this.vue.isInertie()) ? "NON" : "OUI";
                this.vue.setInertie(!this.vue.isInertie());
                this.vue.setMessInertie(createImageOfMot(String.valueOf(iner)));
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (this.vue.getFleche() == 0) {
                this.vue.setNiveau((this.vue.getNiveau() == 1) ? 4 : this.vue.getNiveau() - 1);
                this.vue.setMessNiveau(createImageOfMot(String.valueOf(this.vue.getNiveau())));
            }
            if (this.vue.getFleche() == 1) {
                this.pack = (this.pack == 0) ? this.vue.getListPackSkin().size() - 1 : this.pack - 1;
                this.vue.setSkin(this.vue.getListPackSkin().get(this.pack));
                this.vue.setMessSkin(createImageOfMot(this.vue.getSkin()));
            }
            if (this.vue.getFleche() == 2) {
                String iner = (this.vue.isInertie()) ? "NON" : "OUI";
                this.vue.setInertie(!this.vue.isInertie());
                this.vue.setMessInertie(createImageOfMot(String.valueOf(iner)));
            }
        }
        /// Gestion de la flèche :
        if (e.getKeyCode() == KeyEvent.VK_UP) // Si on monte avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 0) ? 4 : this.vue.getFleche() - 1);

        if (e.getKeyCode() == KeyEvent.VK_DOWN) // Si on descend avec la fleche :
            this.vue.setFleche((this.vue.getFleche() == 4) ? 0 : this.vue.getFleche() + 1);
    }

    @Override
    void keyControlReleased(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        int y = (10 * this.vue.getHeight() / 100);
        int h = this.vue.getHeight() * 30 / 1026;

        if (e.getY() > y && e.getY() < y + h) {
            this.vue.setNiveau((this.vue.getNiveau() + 1) % 4);
            this.vue.setMessNiveau(createImageOfMot(String.valueOf(this.vue.getNiveau())));
            return;
        }
        y += this.vue.getSautLigne();

        if (e.getY() > y && e.getY() < y + h) {
            this.pack = (this.pack == this.vue.getListPackSkin().size() - 1) ? 0 : this.pack + 1;
            this.vue.setSkin(this.vue.getListPackSkin().get(this.pack));
            this.vue.setMessSkin(createImageOfMot(this.vue.getSkin()));
            return;
        }
        y += this.vue.getSautLigne();
        if (e.getY() > y && e.getY() < y + h) {
            String iner = (this.vue.isInertie()) ? "NON" : "OUI";
            this.vue.setInertie(!this.vue.isInertie());
            this.vue.setMessInertie(createImageOfMot(String.valueOf(iner)));
            return;
        }
        y += this.vue.getSautLigne();
        if (e.getY() > y && e.getY() < y + h) {
            Vue.isSetting = false;
            Vue.isMenuDemarrer = true;
            return;
        }
        y += this.vue.getSautLigne();
        if (e.getY() > y && e.getY() < y + h) {
            System.out.println("À la prochaine !");
            Vue.isQuitte = true; // On quitte le jeu.
            System.exit(0); // On ferme toutes les fenêtres & le programme.
        }
    }
}
