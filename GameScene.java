package com.ozyra.battlearena.scenes;

import com.ozyra.battlearena.controller.GameController;
import com.ozyra.battlearena.model.GameCharacter;
import com.ozyra.battlearena.model.Projectile;
import com.ozyra.battlearena.model.Weapon;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class GameScene {

    // The canvas is the "screen" where we draw all the shapes and players
    private final Canvas canvas = new Canvas(1200, 600);

    // The Scene container that holds the canvas
    private final Scene scene;

    // KEY CONCEPT: We use a Set to store keys because a Set cannot have duplicates.
    // This allows us to handle multiple key presses at once
    private final Set<KeyCode> keys = new HashSet<>();

    // The link to the game logic
    private final GameController controller;

    // Flag to stop the game when someone dies
    private boolean gameOver = false;

    // Movement speed in pixels per second
    private static final double SPEED = 200;

    // Visual: Defines the dark spotlight background
    private static final RadialGradient ARENA_BG = new RadialGradient(0, 0, 0.5, 0.5, 1.0, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#2b2b2b")), new Stop(1, Color.web("111111")));

    // Visual: Defines the green gradient for health bars
    private static final LinearGradient HP_GRADIENT = new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#2ecc71")), new Stop(1, Color.web("#27ae60")));;

    // Constructor: sets up the window and starts the loop
    public GameScene(Stage stage, GameCharacter p1, GameCharacter p2) {
        controller = new GameController(1200, 600, p1, p2);

        // StackPane allows us to stack the "Retry" button on TOP of the Canvas later
        StackPane root = new StackPane(canvas);
        scene = new Scene(root);

        // EVENT LISTENERS:
        // When a key is pressed, add it to our "Active Keys" list
        scene.setOnKeyPressed(e -> keys.add(e.getCode()));

        // When a key is released, remove it from the list
        scene.setOnKeyReleased(e -> keys.remove(e.getCode()));

        // Start the game loop
        controller.start(dt -> {
            handleInput(dt); // 1. Check keys and move
            draw(stage); // 2. Draw the new frame
        });
    }

    // Handles all player and movement actions
    private void handleInput(double dt) {
        if (gameOver) return; // Stop inputs if game is over

        GameCharacter p1 = controller.getP1();
        GameCharacter p2 = controller.getP2();

        // ---- Player 1 Movement (WASD) ----
        // Math.max/min ensures they cannot walk off the screen
        if (keys.contains(KeyCode.W)) { p1.setY(Math.max(20, p1.getY() - SPEED * dt)); p1.setDirection(0, -1); }
        if (keys.contains(KeyCode.S)) { p1.setY(Math.min(580, p1.getY() + SPEED * dt)); p1.setDirection(0, 1); }
        if (keys.contains(KeyCode.A)) { p1.setX(Math.max(20, p1.getX() - SPEED * dt)); p1.setDirection(-1, 0); }
        if (keys.contains(KeyCode.D)) { p1.setX(Math.min(600, p1.getX() + SPEED * dt)); p1.setDirection(1, 0); }

        //---- Player 2 Movement (Arrows) ----
        if (keys.contains(KeyCode.UP))    { p2.setY(Math.max(20, p2.getY() - SPEED * dt)); p2.setDirection(0, -1); }
        if (keys.contains(KeyCode.DOWN))  { p2.setY(Math.min(580, p2.getY() + SPEED * dt)); p2.setDirection(0, 1); }
        if (keys.contains(KeyCode.LEFT))  { p2.setX(Math.max(600, p2.getX() - SPEED * dt)); p2.setDirection(-1, 0); }
        if (keys.contains(KeyCode.RIGHT)) { p2.setX(Math.min(1180, p2.getX() + SPEED * dt)); p2.setDirection(1, 0); }

        // Shooting logic ( F for player1, L for player 2)
        long now = System.currentTimeMillis();
        if (keys.contains(KeyCode.F)) controller.addProjectile(p1.shoot(now));
        if (keys.contains(KeyCode.L)) controller.addProjectile(p2.shoot(now));

        // Weapon switching logic
        if (keys.contains(KeyCode.DIGIT1)) p1.setWeapon(Weapon.createWeapon("Sword"));
        if (keys.contains(KeyCode.DIGIT2)) p1.setWeapon(Weapon.createWeapon("Bow"));
        if (keys.contains(KeyCode.DIGIT3)) p1.setWeapon(Weapon.createWeapon("Magic"));

        if (keys.contains(KeyCode.B)) p2.setWeapon(Weapon.createWeapon("Sword"));
        if (keys.contains(KeyCode.N)) p2.setWeapon(Weapon.createWeapon("Bow"));
        if (keys.contains(KeyCode.M)) p2.setWeapon(Weapon.createWeapon("Magic"));
    }

    // The main drawing method called 60 times a second
    private void draw(Stage stage) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // 1. Draw Background
        g.setFill(ARENA_BG);
        g.fillRect(0, 0, 1200, 600);

        // 2. Draw Center line
        g.setStroke(Color.web("#444444"));
        g.setLineWidth(4);
        g.setLineDashes(15, 15); // Dashed effect

        // Add shadow only to the line
        g.save(); // Save settings
        g.setEffect(new DropShadow(10, Color.BLACK));
        g.strokeLine(600, 0, 600, 600);
        g.restore(); // // Restore settings so shadow doesn't apply to everything else

        g.setLineDashes(null); // Reset dashes

        // 3. Draw Players
        drawPlayer(g, controller.getP1());
        drawPlayer(g, controller.getP2());

        // 4. Draw Projectiles
        for (Projectile p : controller.getProjectiles()) {
            g.setFill(Color.YELLOW);
            g.setEffect(new DropShadow(5, Color.ORANGE));
            g.fillOval(p.getX() - 5, p.getY() - 5, 10, 10);
            g.setEffect(null);
        }

        // 5. Check for Game Over
        if (!gameOver && (controller.getP1().isDead() || controller.getP2().isDead())) {
            gameOver = true;
            controller.stop(); // Stop the loop

            // Center Text Alignment
            g.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
            g.setTextBaseline(javafx.geometry.VPos.CENTER);

            // Draw "GAME OVER"
            g.setFill(Color.WHITE);
            g.setFont(javafx.scene.text.Font.font("Segoe UI", 80));
            g.fillText("GAME OVER", 600, 200);

            // Draw Winner
            g.setFont(javafx.scene.text.Font.font("Segoe UI", 40));
            g.fillText(controller.getP1().isDead() ? "PLAYER 2 Wins!" : "PLAYER 1 Wins!", 600, 300);

            // Create and style retry btn
            Button retry = new Button("RETRY");
            retry.setStyle("-fx-background-color: #00a8ff; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 10 40; -fx-background-radius: 30; -fx-cursor: hand;");
            retry.setOnAction(e -> stage.setScene(new SelectionScene(stage).getScene()));


            retry.setTranslateY(150); // move btn down
            ((StackPane) canvas.getParent()).getChildren().add(retry); // Add to screen
        }
    }

    // Helper method to draw a specific player
    private void drawPlayer(GraphicsContext g, GameCharacter c) {

        g.save(); // Isolate effects for this player

        // Choose shape based on character type
        switch (c.getShape()) {

            case CIRCLE -> { //MAGE
                g.setFill(Color.web("#9b59b6"));
                g.setEffect(new DropShadow(20, Color.web("#8e44ad")));
                g.fillOval(c.getX() - 20, c.getY() - 20, 40, 40);

                g.setEffect(null);
                g.setStroke(Color.WHITE);
                g.setLineWidth(1);
                g.strokeOval(c.getX() - 20, c.getY() - 20, 40, 40);
            }

            case SQUARE -> { //WARRIOR
                g.setFill(Color.web("#3498db"));
                g.setStroke(Color.web("#ecf0f1"));
                g.setLineWidth(4);
                g.fillRect(c.getX() - 20, c.getY() - 20, 40, 40);
                g.strokeRect(c.getX() - 20, c.getY() - 20, 40, 40);
            }

            case TRIANGLE -> { //ARCHER
                double[] xPoints = {
                        c.getX(),
                        c.getX() - 22,
                        c.getX() + 22
                };
                double[] yPoints = {
                        c.getY() - 22,
                        c.getY() + 22,
                        c.getY() + 22
                };

                g.setFill(Color.web("#2ecc71"));
                g.setStroke(Color.web("#27ae60"));
                g.setLineWidth(2);
                g.fillPolygon(xPoints, yPoints, 3);
                g.strokePolygon(xPoints, yPoints, 3);
            }
        }

        g.restore(); // Clear effects

        // Draw direction indicator
        g.setStroke(Color.WHITE);
        g.setLineWidth(2);
        g.strokeLine(
                c.getX(),
                c.getY(),
                c.getX() + c.getDirX() * 30,
                c.getY() + c.getDirY() * 30
        );

        // Draw Name
        g.setFill(Color.WHITE);
        g.setFont(javafx.scene.text.Font.font("Segoe UI", javafx.scene.text.FontWeight.BOLD, 12));
        g.fillText(c.getName(), c.getX() - 20, c.getY() - 35);

        // Draw Weapon Name
        g.setFont(javafx.scene.text.Font.font("Segoe UI", javafx.scene.text.FontWeight.NORMAL, 10));
        g.fillText("[" + c.getWeapon().getName() + "]", c.getX() - 18, c.getY() + 45);

        // Health bar logic
        double barX = c.getX() - 25;
        double barY = c.getY() - 65;
        double barWidth = 50;
        double barHeight = 8;

        // Background (Red)
        g.setFill(Color.web("#c0392b"));
        g.fillRoundRect(barX, barY, barWidth, barHeight, 5, 5);

        // Foreground (Green Gradient)
        g.setFill(HP_GRADIENT);
        double maxHealth = 100.0;
        double healthPercentage = c.getHealth() / maxHealth;
        double healthWidth = barWidth * healthPercentage;

        // Safety Clamps
        if (healthWidth < 0) healthWidth = 0;
        if (healthWidth > barWidth) healthWidth = barWidth;

        g.fillRoundRect(barX, barY, healthWidth, barHeight, 5, 5);

        // Border (WHITE)
        g.setStroke(Color.WHITE);
        g.setLineWidth(1);
        g.strokeRoundRect(barX, barY, barWidth, barHeight, 5, 5);


    }


    public Scene getScene() { return scene; }
}

