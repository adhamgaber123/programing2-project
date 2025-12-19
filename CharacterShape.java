package com.ozyra.battlearena.model;

// 'enum' (Enumeration) defines a fixed list of options that cannot be changed
// this ensures we only use these specific shapes in the game
public enum CharacterShape {
    CIRCLE, // Represents the circular visual style (Mage)
    TRIANGLE, // Represents the circular visual style (Archer)
    SQUARE // Represents the circular visual style (Warrior)
}

//Using an Enum is safer because it prevents spelling mistakes.
//If we typed 'Curcle' by accident in a String, the game would crash or look wrong.
//With an Enum, the compiler catches the error immediately.