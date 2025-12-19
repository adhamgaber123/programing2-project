package com.ozyra.battlearena.model;

public class Projectile {

    private double x, y; // Current position of the bullet
    private final double vx, vy; // Velocity (speed + direction) in x and y axis
    private final int damage; // how much health this bullet reduces
    private final GameCharacter owner; // who fired this bullet

    // Constructor: creates the bullet starting at (x,y) with a specific speed and owner
    public Projectile(double x, double y, double vx, double vy, int damage, GameCharacter owner) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.damage = damage;
        this.owner = owner;
    }

    // Moves the bullet forward
    public void update(double dt) {
        // position = old position + (velocity * time passed)
        // multiplying by 'dt' (Delta Time) ensures smooth movement on all computers
        x += vx * dt;
        y += vy * dt;
    }

    // checks if the bullet has left the game window
    // we use this to delete bullets so the game doesn't get slow
    public boolean isOffScreen(double w, double h) {
        return x < 0 || x > w || y < 0 || y > h;
    }

    // Getters allow the controller to see where the bullet is
    public double getX() { return x; }
    public double getY() { return y; }
    public int getDamage() { return damage; }
    public GameCharacter getOwner() { return owner; }
}
