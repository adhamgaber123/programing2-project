package com.ozyra.battlearena.model;

// 'abstract' means this class is template, we cannot use new..
// must create a child class to use it
public abstract class GameCharacter {

    // 'Protected' means these var can be used by this class and its children
    protected String name;
    protected double x, y; // position on the screen
    protected int health;
    protected Weapon weapon;
    protected CharacterShape shape; // visual styke

    // Direction vector: (1,0) means facing right, (-1,0) means left
    protected double dirX = 1, dirY = 0;

    // Timestamp of the last time a bullet was fired
    protected long lastShotTime = 0;

    // Constructor: initializes the common data for any character
    public GameCharacter(String name, double x, double y, int health, Weapon weapon,CharacterShape shape) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = health;
        this.weapon = weapon;
        this.shape = shape;
    }

    public CharacterShape getShape() {
        return shape;
    }

    // Checks if enough time has passed since the last shot to fire again
    public boolean canShoot(long now) {
        // Current time minus last shot time must be greater than the weapon's cooldown
        return now - lastShotTime >= weapon.getCooldownMs();
    }

    // Handles the shooting logic
    public Projectile shoot(long now) {
        // 1. check if weapon is ready
        if (!canShoot(now)) return null;
        // 2. update the last shot time to present (resetting the cooldown)
        lastShotTime = now;
        // 3. Create and return the actual bullet object
        return weapon.createProjectile(x, y, dirX, dirY, this);
    }

    // Reduces health when hit
    public void takeDamage(int dmg) {
        // Math.max(0,..) ensures health never goes below 0
        health = Math.max(0, health - dmg);
    }

    // simple check to see if the player has lost
    public boolean isDead() { return health <= 0; }

    // updates which way the character is facing (used for shooting direction)
    public void setDirection(double dx, double dy) {
        this.dirX = dx;
        this.dirY = dy;
    }

    // swaps the current weapon for a new one
    public void setWeapon(Weapon weapon) {
    	 this.lastShotTime = 0; // reset cooldown so they can shoot the new weapon immediately
        this.weapon = weapon;
    }

    //Getters and Setter
    // Allow other classes to read/change values
    public String getName() { return name; }
    public int getHealth() { return health; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getDirX() { return dirX; }
    public double getDirY() { return dirY; }
    public Weapon getWeapon() { return weapon; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}

