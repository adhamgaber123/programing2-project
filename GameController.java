package com.ozyra.battlearena.controller;

import com.ozyra.battlearena.model.GameCharacter;
import com.ozyra.battlearena.model.Projectile;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class GameController {

    private final double width, height; // Dimensions of game world
    private final GameCharacter p1, p2; // Game players
    private final List<Projectile> projectiles = new ArrayList<>(); // List to store all active bullets in the game
    private AnimationTimer timer; // Timers that runs the game loop

    // Constructor-> initialize the controller with map size and players
    public GameController(double width, double height, GameCharacter p1, GameCharacter p2) {
        this.width = width;
        this.height = height;
        this.p1 = p1;
        this.p2 = p2;
    }

    // Starts the game loop
    // lets the scene know when to redraw screen
    public void start(Consumer<Double> onUpdate) {
        timer = new AnimationTimer() {
            long last = 0; // Tracks the time of the previous frame

            @Override
            public void handle(long now) {
                if (last == 0) { // First frame setup
                    last = now;
                    return;
                }

                // Calc delta time (dt): time passed since the last frame in seconds
                // ensures movement is smooth regardless of computer speed
                double dt = (now - last) / 1e9;

                update(dt); // updates game logic
                onUpdate.accept(dt); // tell the view to draw the new state
                last = now; // Reset time for next frame
            }
        };
        timer.start(); // Activate the timer
    }

    // Stops the game loop "when game over"
    public void stop() {
        if (timer != null) timer.stop();
    }

    // Main game logic: updates bullets and checks for collisions
    private void update(double dt) {
        // Iterator is used to safely remove items from a list while looping through it
        Iterator<Projectile> it = projectiles.iterator();

        while (it.hasNext()) {
            Projectile p = it.next();
            p.update(dt); // Move the projectile based on its speed

            // Check if player 1 gets hit by a bullet that is NOT owned by him
            if (p.getOwner() != p1 && intersects(p, p1)) {
                p1.takeDamage(p.getDamage()); // Apply damage
                it.remove(); // Remove the bullet
                continue; // Skip to next bullet
            }

            // Check if player 2 gets hit by a bullet that is NOT owned by him
            if (p.getOwner() != p2 && intersects(p, p2)) {
                p2.takeDamage(p.getDamage()); //Apply damage
                it.remove(); // Remove the bullet
                continue; // Skip to next bullet
            }

            //Remove bullets that fly of the screen to save memory
            if (p.isOffScreen(width, height)) {
                it.remove();
            }
        }
    }

    // Simple collision detection (Circle based)
    // Checks if the distance between a bullet and a player is small enough to hit
    private boolean intersects(Projectile p, GameCharacter c) {
        double dx = p.getX() - c.getX();
        double dy = p.getY() - c.getY();
        // Check if distance squared is less than radius squared (25 pixels)
        return dx * dx + dy * dy <= 25 * 25;
    }

    // Adds a new bullet to the game (called when a player shoots)
    public void addProjectile(Projectile p) {
        if (p != null) projectiles.add(p);
    }

    // Getters to allow the view (GameScene) to see the data
    public List<Projectile> getProjectiles() { return projectiles; }
    public GameCharacter getP1() { return p1; }
    public GameCharacter getP2() { return p2; }
}

