
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// Main class that manages the game window and screens
public class SkyHopperGame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel cards;
    private final GamePanel gamePanel;

    // Set up the game window and its panels
    public SkyHopperGame() {
        super("Sky Hopper");
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        gamePanel = new GamePanel(this);

        // Add the menu, game, and game over screens
        cards.add(new MenuPanel(this), "menu");
        cards.add(gamePanel, "game");
        cards.add(new GameOverPanel(this), "over");

        setContentPane(cards);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 850);
        setResizable(false);
        setLocationRelativeTo(null);
        showMenu();
    }

    // Display the main menu
    public void showMenu() {
        cardLayout.show(cards, "menu");
    }

    // Start a new game and show the game screen
    public void startGame() {
        gamePanel.startNewGame();
        cardLayout.show(cards, "game");
        gamePanel.requestFocusInWindow();
    }

    // Display the game over screen
    public void showGameOver() {
        cardLayout.show(cards, "over");
    }

    // Main method that runs the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Could not apply system look and feel: " + ex.getMessage());
            }
            new SkyHopperGame().setVisible(true);
        });
    }
}

// Panel that displays the main menu
class MenuPanel extends JPanel {
    public MenuPanel(SkyHopperGame game) {
        setLayout(new GridBagLayout());
        setBackground(new Color(120, 205, 240));

        // Create the menu content
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Display the game title
        JLabel title = new JLabel("SKY HOPPER");
        title.setFont(new Font("Arial", Font.BOLD, 38));
        title.setForeground(new Color(25, 75, 115));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Display the game instructions
        JLabel subtitle = new JLabel("Fly high. Collect stars. Avoid the pipes!");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button that starts the game
        JButton start = new JButton("START GAME");
        start.setFont(new Font("Arial", Font.BOLD, 18));
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(e -> game.startGame());

        // Show the game controls
        JLabel controls = new JLabel("SPACE / UP ARROW / CLICK to flap");
        controls.setFont(new Font("Arial", Font.PLAIN, 13));
        controls.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add all menu elements to the panel
        content.add(title);
        content.add(Box.createVerticalStrut(12));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(35));
        content.add(start);
        content.add(Box.createVerticalStrut(18));
        content.add(controls);
        add(content);
    }
}

// Panel displayed when the player loses
class GameOverPanel extends JPanel {
    public GameOverPanel(SkyHopperGame game) {
        setLayout(new GridBagLayout());
        setBackground(new Color(120, 255, 200));

        // Create the game over content
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Display the game over title
        JLabel title = new JLabel("GAME OVER");
        title.setFont(new Font("Arial", Font.BOLD, 38));
        title.setForeground(new Color(145, 45, 45));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Display a message to the player
        JLabel message = new JLabel("Great try! Give it another flight.");
        message.setFont(new Font("Arial", Font.PLAIN, 15));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button to restart the game
        JButton retry = new JButton("PLAY AGAIN");
        retry.setFont(new Font("Arial", Font.BOLD, 17));
        retry.setAlignmentX(Component.CENTER_ALIGNMENT);
        retry.addActionListener(e -> game.startGame());

        // Button to return to the main menu
        JButton menu = new JButton("MAIN MENU");
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.addActionListener(e -> game.showMenu());

        // Add all elements to the game over panel
        content.add(title);
        content.add(Box.createVerticalStrut(12));
        content.add(message);
        content.add(Box.createVerticalStrut(30));
        content.add(retry);
        content.add(Box.createVerticalStrut(12));
        content.add(menu);
        add(content);
    }
}

// Main gameplay panel that handles game actions and drawing
class GamePanel extends JPanel implements ActionListener {
    // Set the game dimensions and obstacle sizes
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 400;
    private static final int GROUND_Y = 700;
    private static final int PIPE_WIDTH = 48;
    private static final int GAP = 150;

    // Store the game window, timer, and game objects
    private final SkyHopperGame window;
    private final Timer timer;
    private final Random random;
    private final ArrayList<GameObject> obstacles;
    private final ArrayList<StarCollectible> stars;

    // Variables used to control the game
    private Bird bird;
    private int score;
    private int bestScore;
    private int frameCount;
    private boolean paused;
    private boolean gameEnded;

    // Initialize the game objects and controls
    public GamePanel(SkyHopperGame window) {
        this.window = window;
        random = new Random();
        obstacles = new ArrayList<>();
        stars = new ArrayList<>();
        timer = new Timer(16, this);
        setFocusable(true);
        setBackground(new Color(220, 255, 200));

        // Listen for keyboard input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e.getKeyCode());
            }
        });

        // Allow the player to flap using the mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!paused && !gameEnded) bird.flap();
            }
        });
    }

    // Reset the game and start a new round
    public void startNewGame() {
        bird = new Bird(95, 280);
        obstacles.clear();
        stars.clear();
        score = 0;
        frameCount = 0;
        paused = false;
        gameEnded = false;
        addObstacleSet(WIDTH + 80);
        timer.start();
        requestFocusInWindow();
        repaint();
    }

    // Handle keyboard controls
    private void handleKey(int key) {
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
            if (!paused && !gameEnded) bird.flap();
        } else if (key == KeyEvent.VK_P) {
            if (!gameEnded) paused = !paused;
        } else if (key == KeyEvent.VK_R && gameEnded) {
            window.startGame();
        } else if (key == KeyEvent.VK_ESCAPE) {
            timer.stop();
            window.showMenu();
        }
    }

    // Create a pair of pipes with a star between them
    private void addObstacleSet(int x) {
        int gapCenter = 240 + random.nextInt(290);
        int topHeight = gapCenter - GAP / 2;
        int bottomY = gapCenter + GAP / 2;
        obstacles.add(new PipeObstacle(x, 0, PIPE_WIDTH, topHeight, true, 7));
        obstacles.add(new PipeObstacle(x, bottomY, PIPE_WIDTH, GROUND_Y - bottomY, false, 9));
        stars.add(new StarCollectible(x + PIPE_WIDTH + 55, gapCenter - 10, 5));
    }

    // Update the game every timer tick
    @Override
    public void actionPerformed(ActionEvent e) {
        if (paused || gameEnded) {
            repaint();
            return;
        }

        // Update the bird, obstacles, and stars
        frameCount++;
        bird.update();

        for (GameObject object : obstacles) object.update();
        for (StarCollectible star : stars) star.update();

        // Add new obstacles at regular intervals
        if (frameCount % 105 == 0) addObstacleSet(WIDTH + 20);

        // Check for pipe collisions and remove off-screen pipes
        Iterator<GameObject> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            GameObject object = obstacleIterator.next();
            if (object.getX() + object.getWidth() < 0) {
                obstacleIterator.remove();
            } else if (bird.collidesWith(object)) {
                endGame();
                return;
            }
        }

        // Check if the bird collects stars
        Iterator<StarCollectible> starIterator = stars.iterator();
        while (starIterator.hasNext()) {
            StarCollectible star = starIterator.next();
            if (!star.isCollected() && bird.collidesWith(star)) {
                star.collect();
                score += 2;
            }
            if (star.getX() + star.getWidth() < 0 || star.isCollected()) {
                starIterator.remove();
            }
        }

        // End the game if the bird hits the ceiling or ground
        if (bird.getY() < 0 || bird.getY() + bird.getHeight() >= GROUND_Y) {
            endGame();
            return;
        }

        // Increase the score as the player passes obstacles
        if (frameCount % 105 == 55) score++;
        repaint();
    }

    // Stop the game and update the best score
    private void endGame() {
        gameEnded = true;
        timer.stop();
        bestScore = Math.max(bestScore, score);
        window.showGameOver();
    }

    // Draw all game elements on the screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the background, obstacles, stars, bird, and score
        drawBackground(g2);
        for (GameObject object : obstacles) object.draw(g2);
        for (StarCollectible star : stars) star.draw(g2);
        if (bird != null) bird.draw(g2);
        drawGround(g2);
        drawHUD(g2);

        // Display a pause screen when the game is paused
        if (paused) {
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 34));
            drawCentered(g2, "PAUSED", 300);
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 15));
            drawCentered(g2, "Press P to resume", 330);
        }

        g2.dispose();
    }

    // Draw the sky, clouds, and hills
    private void drawBackground(Graphics2D g2) {
        g2.setColor(new Color(120, 205, 240));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(255, 255, 255, 175));
        g2.fillOval(55, 90, 75, 28);
        g2.fillOval(85, 78, 65, 40);
        g2.fillOval(340, 145, 85, 30);
        g2.fillOval(370, 130, 65, 42);

        g2.setColor(new Color(100, 185, 125));
        g2.fillOval(-70, 490, 270, 150);
        g2.fillOval(180, 510, 250, 130);
        g2.fillOval(350, 485, 230, 150);
    }

    // Draw the ground at the bottom of the game
    private void drawGround(Graphics2D g2) {
        g2.setColor(new Color(225, 195, 120));
        g2.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);
        g2.setColor(new Color(90, 175, 75));
        g2.fillRect(0, GROUND_Y, WIDTH, 12);
        g2.setColor(new Color(190, 155, 90));
        for (int x = 0; x < WIDTH; x += 35) {
            g2.drawLine(x, GROUND_Y + 22, x + 15, GROUND_Y + 22);
        }
    }

    // Display the current score and game controls
    private void drawHUD(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 27));
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + score, 18, 38);
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("Best: " + bestScore, 20, 59);
        g2.drawString("P: Pause   ESC: Menu", 325, 28);
    }

    // Draw text at the center of the screen
    private void drawCentered(Graphics2D g2, String text, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, y);
    }
}
