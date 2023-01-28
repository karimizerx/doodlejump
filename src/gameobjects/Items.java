package gameobjects;

public abstract class Items extends GameObject{

    int time;

    public Items(double x, double y, double w, double h, int t) {
        super(x, y, w, h);
        time=t;
    }


    public abstract void runEffect(Personnage p);



}
