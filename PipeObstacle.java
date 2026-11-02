import java.awt.Color;
import java.awt.Graphics2D;

public class PipeObstacle extends GameObject {
    private final boolean topPipe;
    private final int speed;

    public PipeObstacle(int x, int y, int width, int height, boolean topPipe, int speed) {
        super(x, y, width, height);
        this.topPipe = topPipe;
        this.speed = speed;
    }

    @Override
    public void update() {
        setX(getX() - speed);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(78, 170, 152));
        g2.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
        g2.setColor(new Color(22, 125, 67));
        g2.fillRect(getX() + 5, getY(), 6, getHeight());
        g2.setColor(new Color(28, 145, 75));
        int capHeight = 18;
        int capY = topPipe ? getY() + getHeight() - capHeight : getY();
        g2.fillRoundRect(getX() - 5, capY, getWidth() + 10, capHeight, 6, 6);
    }
}
