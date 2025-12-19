package com.ozyra.battlearena.model;

// Constructor: Creates a new Warrior specific to the passed parameters
public class Warrior extends GameCharacter {

    // Calls the parent constructor with Warrior-specific stats:
    // 100 = Highest starting health (Tanky character)
    // CharacterShape.CIRCLE = The visual shape
    public Warrior(String name, double x, double y, Weapon weapon) {
    	super(name, x, y, 100, weapon, CharacterShape.SQUARE);
    }
}
