package com.ozyra.battlearena.scenes;

import com.ozyra.battlearena.model.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelectionScene {

    // the container for the entire screen content
    private final Scene scene;

    // Constructor: builds the menu UI
    public SelectionScene(Stage stage) {

        // 1. SETUP layout
        // VBox (Vertical Box) stacks items on the top of each other
        VBox root = new VBox(15); // 15px gap between items
        root.setAlignment(Pos.CENTER); // Center everything in the middle of the screen

        // 2. TITLES
        // The main title
        Label mainTitle = new Label("B A T T L E   A R E N A");
        mainTitle.getStyleClass().add("mainTitle");

        // The Subtitle
        Label title = new Label("Character Selection");
        title.getStyleClass().add("header-label");

        // 3. Player1 selection UI
        Label p1Label = new Label("PLAYER 1");

        // Character Dropdown
        ChoiceBox<String> p1Character = new ChoiceBox<>();
        p1Character.getItems().addAll("Warrior", "Mage", "Archer");
        p1Character.setValue("Warrior");

        //Weapon Dropdown
        ChoiceBox<String> p1Weapon = new ChoiceBox<>();
        p1Weapon.getItems().addAll("Sword", "Bow", "Magic");
        p1Weapon.setValue("Sword");

        // 4. Player2 selection UI
        Label p2Label = new Label("PLAYER 2");

        ChoiceBox<String> p2Character = new ChoiceBox<>();
        p2Character.getItems().addAll("Warrior", "Mage", "Archer");
        p2Character.setValue("Warrior");

        ChoiceBox<String> p2Weapon = new ChoiceBox<>();
        p2Weapon.getItems().addAll("Sword", "Bow", "Magic");
        p2Weapon.setValue("Sword");

        // 5. Start btn logic
        Button start = new Button("START GAME");

        // Runs when the btn is clicked
        start.setOnAction(e -> {

            // ---- CREATE PLAYER1 OBJECT ----
            GameCharacter player1 = switch (p1Character.getValue()) {

                // If they picked Mage, create a new Mage object
                case "Mage" -> new Mage("P1", 100, 300,
                        Weapon.createWeapon(p1Weapon.getValue()));

                // If they picked Archer, create a new Archer object
                case "Archer" -> new Archer("P1", 100, 300,
                        Weapon.createWeapon(p1Weapon.getValue()));

                // Default to Warrior
                default -> new Warrior("P1", 100, 300,
                        Weapon.createWeapon(p1Weapon.getValue()));
            };

            // ---- CREATE PLAYER 2 OBJECT ----
            // P2 starts at X=1100 (Right side of screen)
            GameCharacter player2 = switch (p2Character.getValue()) {
                case "Mage" -> new Mage("P2", 1100, 300,
                        Weapon.createWeapon(p2Weapon.getValue()));
                case "Archer" -> new Archer("P2", 1100, 300,
                        Weapon.createWeapon(p2Weapon.getValue()));
                default -> new Warrior("P2", 1100, 300,
                        Weapon.createWeapon(p2Weapon.getValue()));
            };

            // Switch the scene
            // Create the actual GameScene, pass the players we just created, and set it on the stage
            stage.setScene(new GameScene(stage, player1, player2).getScene());
        });

        // ---- ADD EVERYTHING TO ROOT ----
        root.getChildren().addAll(
                mainTitle,
                title,

                p1Label,
                p1Character,
                p1Weapon,

                p2Label,
                p2Character,
                p2Weapon,

                start
        );

        // Create the scene with size 1200x600
        scene = new Scene(root, 1200, 600);

        // Load the CSS file for styling
        scene.getStylesheets().add("file:assets/styles.css");
    }

    // Helper to give the scene to the Stage
    public Scene getScene() {
        return scene;
    }
}
