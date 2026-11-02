SKY HOPPER - JAVA SWING GAME

PROJECT TYPE
Game Development - 2D Desktop Game

REQUIREMENTS
- Java Development Kit (JDK) 8 or later
- Java Swing (included with the JDK)
- No database or game engine is used

HOW TO RUN
1. Extract the SkyHopper project folder.
2. Open a terminal or command prompt inside the folder.
3. Compile the Java files:
   javac *.java
4. Run the game:
   java SkyHopperGame

CONTROLS
- Spacebar: Flap upward
- Up Arrow: Flap upward
- Mouse Click: Flap upward
- P: Pause or resume
- R: Restart after game over
- ESC: Return to the main menu

GAMEPLAY
Guide the bird through the gaps between the pipes. Avoid touching pipes,
the top boundary, or the ground. Collect the yellow stars for bonus points.
The game ends when the bird hits an obstacle or leaves the play area.

SCORING
- Passing an obstacle set: 1 point
- Collecting a star: 2 bonus points
- Best score is kept in memory while the application is running.
  It resets when the application is closed.

OOP DESIGN
- Encapsulation: GameObject and game classes keep fields private.
- Abstraction: GameObject is an abstract base class.
- Inheritance: Bird, PipeObstacle, and StarCollectible extend GameObject.
- Polymorphism: GamePanel stores different game objects and calls their
  overridden update() and draw() methods.
- Interface: Renderable defines the draw() behavior.
- Composition: GamePanel owns the bird, obstacles, and collectibles.
- Exception Handling: Constructor validation and look-and-feel setup
  handle invalid dimensions and possible UI setup errors.

PROJECT FILES
- Renderable.java
- GameObject.java
- Bird.java
- PipeObstacle.java
- StarCollectible.java
- SkyHopperGame.java
