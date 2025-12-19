package com.ozyra.battlearena.model;

// A Class tells the code how to do something. An Interface only tells the code what needs to be done.
// Any class that 'implements' Weapon MUST have these methods.
public interface Weapon {

    String getName();
    int getDamage();
    double getSpeed();
    long getCooldownMs();

    // Defines how the weapon fires a projectile
    // (x,y) = start pos, (dx,dy) = direction, owner = who shot it
    Projectile createProjectile(double x, double y, double dx, double dy, GameCharacter owner);

    // This helper method creates weapons for us so we don't have to type "new SimpleWeapon(...)" everywhere
    // static means we can call this method without needing an existing weapon object
    static Weapon createWeapon(String type) {

        return switch (type) {

            // Sword: High Damage, Medium Speed
            case "Sword" -> new SimpleWeapon("Sword", 10, 400, 500);

            // Bow: Low Damage, Fast Speed, Fast fire rate
            case "Bow"   -> new SimpleWeapon("Bow", 7, 600, 300);

            // Magic: Highest Damage, Slow Speed, Slow fire rate
            case "Magic" -> new SimpleWeapon("Magic", 15, 300, 1000);

            // Default: Weak "Fist" attack if something goes wrong
            default      -> new SimpleWeapon("Fist", 5, 200, 500);
        };
    }
}
