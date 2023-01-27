package gameobjects;

import java.util.ArrayList;

public class Terrain {

    ArrayList<GameObject> objetcs;
    Joueur joueur;
    final double height,width;// dimensions du terrain
    
    
    double difficulty=1.0;
    /*  baisse le plus le score monte, affecte la densite des plateformes et la proba qu'un heli ou fusee apare, 
    *   inversement affacte la proba d'apparition des monstres, surement on inverse 1/difficulte 
    */
    
    
    
    
    public Terrain(ArrayList<GameObject> objetcs, Joueur joueur, double height, double width) {
        this.objetcs = objetcs;
        this.joueur = joueur;
        this.height = height;
        this.width = width;
    }

    void generateObstacles(){

    }

    void removeObstacles(){

    }
    
 

    void update(double deltaT){
        
    }

}