package com.ozyra.battlearena.model;

// Implements the 'weapon' interface, meaning is MUST have methods
public class SimpleWeapon implements Weapon {

    //Basics stats for this specific weapon
    private final String name;
    private final int damage;
    private final double speed; // How fast the bullet flies
    private final long cooldown; // Time in ms before you can shoot again

    // Constructor: sets the stats when we create a weapon
    public SimpleWeapon(String name, int damage, double speed, long cooldown) {
        this.name = name;
        this.damage = damage;
        this.speed = speed;
        this.cooldown = cooldown;
    }

    //Standard getters required by the interface
    @Override public String getName() { return name; }
    @Override public int getDamage() { return damage; }
    @Override public double getSpeed() { return speed; }
    @Override public long getCooldownMs() { return cooldown; }

    // the factory method that actually spawns the bullet
    @Override
    public Projectile createProjectile(double x, double y, double dx, double dy, GameCharacter owner) {

        // 1. Calculate the length of the direction vector using Pythagoras (a^2 + b^2 = c^2)
        double len = Math.sqrt(dx * dx + dy * dy);

        // 2. safety check: avoid dividing by zero if the player isn't moving
        if (len == 0) len = 1;

        // 3. Create the bullet
        return new Projectile(
                x, y, // start at player's position

                // Math:
                //(dx / len) creates a "Unit Vector" (length of 1)
                //Then we multiply by 'speed' to make it fly at the correct velocity
                dx / len * speed,
                dy / len * speed,

                damage, // Pass weapon damage
                owner // Pass who shoot it
        );
    }
}
