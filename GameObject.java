import java.awt.Rectangle;

public abstract class GameObject implements Renderable {
    private int x;
    private int y;
    private final int width;
    private final int height;

    public GameObject(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Object dimensions must be positive.");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public abstract void update();

    public boolean collidesWith(GameObject other) {
        return other != null && getBounds().intersects(other.getBounds());
    }
}
