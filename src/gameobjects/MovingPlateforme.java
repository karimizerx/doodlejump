package gameobjects;

public class MovingPlateforme extends Plateforme implements Moveable {

    public MovingPlateforme(double x, double y, double w, double h, double saut,double dx) {
        super(x, y, w, h, saut);
        this.dx=dx;
    }
    final double dx;

    @Override
    public void move(double deltaT) {
        setX(getX()+dx*deltaT);
    }
    


    
}
