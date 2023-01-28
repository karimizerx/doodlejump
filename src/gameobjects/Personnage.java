package gameobjects;


public class Personnage extends GameObject implements Moveable{
    
    private double dx,dy; 
    final private double dy_i;//vitesse initiale
    public Personnage(double x, double y, double w, double h,double dy,double dy_i) {
        super(x, y, w, h);
        this.dy=dy;
        this.dy_i=dy_i;
    }
    

    @Override
    public void move(double deltaT) {
        // partie gravite
        double g=9.81;
        double newX,newY;
        dy= dy>-g? (-g*deltaT+dy) :-g ;//chute libre ;
        
        this.setY((-g/2)*(deltaT*deltaT)+dy*deltaT+this.getY());

        //partie horizental
        newX=dx*deltaT+this.getX();
        // if(newX+this.getHeight()/2>)
        this.setX(newX);

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
            if(val){
                item.runEffect(this);
            }

        }else if(go instanceof Plateforme plateforme){
            val =(// on test si ils se chevauchent horizentalement  
                    (this.getX() >=plateforme.getX() && this.getX()<= plateforme.getX()+plateforme.getWidth())
                    ||
                    (this.getX() + this.getWidth()>=plateforme.getX() && this.getX()+this.getWidth()<= plateforme.getX()+plateforme.getWidth())
                )&&
                // condition vertical depends du type 
                Math.abs(this.getY() - plateforme.getY()+plateforme.getHeight())<epsilone
                && 
                dy<=0 // le personnage decends
                ;
                if(val){
                    dy=dy_i;
                }
            }
        return val;
    }


    public double getDx() {
        return dx;
    }


    public void setDx(double dx) {
        this.dx = dx;
    }


    public double getDy() {
        return dy;
    }


    public void setDy(double dy) {
        this.dy = dy;
    }


    public double getDy_i() {
        return dy_i;
    }





}
