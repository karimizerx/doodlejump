package src.GameObjects;
public abstract class GameObject {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double width;
    private double height;

	public GameObject(double x, double y, double w, double h, double dx, double dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.width = w;
		this.height = h;
		
	}

}
