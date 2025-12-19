package com.ozyra.battlearena.model;

// Inherits from GameCharacter, meaning it gets all movement, health, and shooting logic for free
public class Mage extends GameCharacter {

    // Constructor: Creates a new Mage specific to the passed parameters
    public Mage(String name, double x, double y, Weapon weapon) {

        // Calls the parent constructor (GameCharacter) with Mage-specific stats:
        // 80 = Lowest health
        // CharacterShape.TRIANGLE = The visual shape
    	super(name, x, y, 80, weapon, CharacterShape.CIRCLE); // less health
    }
}
