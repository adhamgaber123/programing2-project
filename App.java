package com.ozyra.battlearena;

import com.ozyra.battlearena.scenes.SelectionScene;
import javafx.application.Application;
import javafx.stage.Stage;

// 'extends Application' allows this class to manage a JavaFX GUI window
public class App extends Application {

    // This method runs automatically when the JavaFX application launches.
    // The 'Stage' parameter represents the main window
    @Override
    public void start(Stage stage) {

        // 1. Set the text that appears in the top bar of the window
        stage.setTitle("Battle Arena");

        // 2. Load the first scene (The Character Selection Menu)
        // We create a new SelectionScene, get its View (Scene object), and put it on the Stage
        stage.setScene(new SelectionScene(stage).getScene());

        // 3. Make the window visible to the user (it's hidden by default!)
        stage.show();
    }

    // The JAVA 'main' method
    public static void main(String[] args) {
        // 'launch()' is a special JavaFX method that sets up the graphics
        // and calls the 'start()' method above
        launch();
    }
}
