import java.awt.Color;
import java.awt.Graphics2D;

public class Bird extends GameObject {
    private double velocityY;
    private static final double GRAVITY = 0.48;
    private static final double FLAP_STRENGTH = -8.2;

    public Bird(int x, int y) {
        super(x, y, 38, 49);
        velocityY = 0;
    }

    public void flap() {
        velocityY = FLAP_STRENGTH;
    }

    @Override
    public void update() {
        velocityY += GRAVITY;
        setY(getY() + (int) velocityY);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(250, 205, 155));
        g2.fillOval(getX(), getY(), getWidth(), getHeight());
  
  
      

    }
}
