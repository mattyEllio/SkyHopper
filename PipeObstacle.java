import java.awt.Color;
import java.awt.Graphics2D;
// Creates the pipe obstacles in the game
public class PipeObstacle extends GameObject {
    private final boolean topPipe;// Identifies if the pipe is on top
    private final int speed;// Controls how fast the pipe moves
// Constructor to set the pipe's position, size, type, and speed
    public PipeObstacle(int x, int y, int width, int height, boolean topPipe, int speed) {
        super(x, y, width, height);
        this.topPipe = topPipe;
        this.speed = speed;
    }
    // Moves the pipe to the left
    @Override
    public void update() {
        setX(getX() - speed);
    }
  // Draws the pipe obstacle
    @Override
    public void draw(Graphics2D g2) {
        // Set the main pipe color
        g2.setColor(new Color(78, 170, 152));
        g2.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
         // Draw the dark green side of the pipe
        g2.setColor(new Color(22, 125, 67));
        g2.fillRect(getX() + 5, getY(), 6, getHeight());
         // Draw the lighter green pipe cap
        g2.setColor(new Color(28, 145, 75));
        int capHeight = 18;
            // Set the cap position depending on the pipe type
        int capY = topPipe ? getY() + getHeight() - capHeight : getY();
        g2.fillRoundRect(getX() - 5, capY, getWidth() + 10, capHeight, 6, 6);
    }
}
