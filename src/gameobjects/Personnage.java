package gameobjects;

import java.security.cert.PolicyQualifierInfo;

public class Personnage extends GameObject implements Moveable{
    
    double dx,dy; 

    public Personnage(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public void move(double deltaT) {
        double g=-9.81;
        dy+=-g*deltaT;
        this.setY((-g/2)*deltaT*deltaT+dy*deltaT+this.getY());
        
    }

    public boolean collides(GameObject go) {
        double epsilone=0.0; // marge d'erreur qu'on se donne
        boolean val=false;
        if(go instanceof Items item){
            val =(// on test si ils se chevauchent horizentalement  
                    (this.getX() >=item.getX() && this.getX()<= item.getX()+item.getWidth())
                    ||
                    (this.getX() + this.getWidth()>=item.getX() && this.getX()+this.getWidth()<= item.getX()+item.getWidth())
                )&&
                // condition vertical depends du type
                Math.abs(this.getY() - item.getY())<epsilone;


        }else if(go instanceof Plateforme plateforme){
            val =(// on test si ils se chevauchent horizentalement  
                    (this.getX() >=plateforme.getX() && this.getX()<= plateforme.getX()+plateforme.getWidth())
                    ||
                    (this.getX() + this.getWidth()>=plateforme.getX() && this.getX()+this.getWidth()<= plateforme.getX()+plateforme.getWidth())
                )&&
                // condition vertical depends du type
                Math.abs(this.getY() - plateforme.getY()+plateforme.getHeight())<epsilone;
            }
        return val;
    }




}
