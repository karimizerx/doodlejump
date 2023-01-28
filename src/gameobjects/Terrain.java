package gameobjects;

import java.util.ArrayList;

public class Terrain {

    ArrayList<GameObject> objetcs;
    Joueur joueur;
    final double height,width;// dimensions du terrain

    double maxHeight=0.8;
    /** Hauteur maximal que peut attendre le personnage, maxHeight*height, avant faire monter le jeu.
     * 
     */


    /** baisse le plus le score monte, affecte la densite des plateformes et la proba qu'un heli ou fusee apare, 
     *  inversement affacte la proba d'apparition des monstres, surement on inverse 1/difficulte 
    */
    double difficulty=1.0;
    
    
    
    
    public Terrain(Joueur joueur, double height, double width) {
        this.objetcs = new ArrayList<GameObject>();
        this.joueur = joueur;
        this.height = height;
        this.width = width;
    }

    void generateObstacles(){

    }

    void removeObstacles(){

    }
    
 

    void update(double deltaT){
        joueur.perso.move(deltaT);
        if(joueur.perso.getX()+joueur.perso.getWidth()/2<=0) //bord a droite, on le mets a gauche
            joueur.perso.setX(width-joueur.perso.getWidth()/2);
        else if (joueur.perso.getX()+joueur.perso.getWidth()/2>=width) //bord a gauche, on le mets a gauche
            joueur.perso.setX(joueur.perso.getWidth()/2);
        if(joueur.perso.getY()<=0){
            // le jeu est terminé
        }else if(joueur.perso.getY()+joueur.perso.getHeight()>maxHeight*height){
            // on fait monter le jeu
        }    
        for (GameObject gameObject : objetcs) {
            joueur.perso.collides(gameObject);
        }
    }

}