import java.awt.Color;
import java.awt.Graphics2D;

public class StarCollectible extends GameObject {
    private final int speed;
    private boolean collected;

    // Constructor for the collectible star as well as setting the x and y data
    public StarCollectible(int x, int y, int speed) {
        super(x, y, 20, 20);
        this.speed = speed;
        collected = false;
    }

    //for setting the X to minus the speed
    @Override
    public void update() {
        setX(getX() - speed);
    }

    //Methods for the Collecting of stars
    public boolean isCollected() { return collected; }
    public void collect() { collected = true; }

    //For the Graphics Renderable to check what the Graphic Design the Star will look like
    @Override
    public void draw(Graphics2D g2) {
        if (collected) return;
        int[] xs = {10, 13, 20, 14, 16, 10, 4, 6, 0, 7};
        int[] ys = {0, 7, 8, 13, 20, 16, 20, 13, 8, 7};
        for (int i = 0; i < xs.length; i++) xs[i] += getX();
        for (int i = 0; i < ys.length; i++) ys[i] += getY();
        g2.setColor(new Color(255, 220, 65));
        g2.fillPolygon(xs, ys, xs.length);
        g2.setColor(new Color(230, 160, 25));
        g2.drawPolygon(xs, ys, xs.length);
    }
}
