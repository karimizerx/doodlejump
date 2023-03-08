package gameobjects;

import java.util.Random;

// Représente une plateforme (qui peut être de différents types).
public abstract class Plateforme extends GameObject {

    private double saut; // Constante de saut (différente en fonction de la plateforme)
    private Items item=null;

    public Plateforme(double x, double y, double w, double h, double saut) {
        super(x, y, w, h);
        this.saut = saut;
    }

    // Getter & Setter

    public double getSaut() {
        return saut;
    }

    public void setSaut(double saut) {
        this.saut = saut;
    }

    public void addItem(double x,double y, double w, double h){
        Random rand =new Random();
        if (rand.nextDouble() >0.5){
            item=new Fusee(x,y,w,h, -25, 2.5);
            System.out.println(x+" "+y+" "+" "+w*4000+" "+h*4000+" "+ 100+" "+ 5);
        }else{
            item=new Helicoptere(x,y,w,h, -10, 5);
        }
    }
    public void updateItem(){
        if(item!=null)
            item.setY(this.getY());
    }

    public Items getItem(){
        return item;
    }
    public void setItem(Items it){
        item=it;
    }
    
}