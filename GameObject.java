import java.awt.Rectangle;
// Parent class for all game objects
public abstract class GameObject implements Renderable {
     // Position and size of the object
    private int x;
    private int y;
    private final int width;
    private final int height;
 // Constructor to set the object's position and size
    public GameObject(int x, int y, int width, int height) {
        // Make sure the dimensions are positive
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Object dimensions must be positive.");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    // Get the object's position and size
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
 // Change the object's position
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    // Create a rectangle around the object for collision detection
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    // Each child class must have its own update method
    public abstract void update();
   // Check if this object collides with another object
    public boolean collidesWith(GameObject other) {
        return other != null && getBounds().intersects(other.getBounds());
    }
}
