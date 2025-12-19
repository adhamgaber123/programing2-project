package com.ozyra.battlearena.model;

// Inherits from GameCharacter, so it gets all the movement and health logic
public class Archer extends GameCharacter {

    // // Constructor: Creates a new Archer specific to the passed parameters
    public Archer(String name, double x, double y, Weapon weapon) {

        // Calls the paren (GameCharacter) constructor to set specific stats for the archer
        // 90 is the starting health
        //CharacterShape.SQUARE = the visual shape
    	super(name, x, y, 90, weapon, CharacterShape.TRIANGLE); // medium health
    }
}