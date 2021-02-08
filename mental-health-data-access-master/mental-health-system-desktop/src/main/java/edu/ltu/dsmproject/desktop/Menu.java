/**
Menu class version: 2.0
@author ChenXiao Liu
Update:
1. Separate the sign in function, so that this class only demonstrate the menu
2. Use css to set styles for all nodes.
3. Changed the name of this class
If you need Detailed guide about javaFX css, please @see https://openjfx.io/javadoc/11/javafx.graphics/javafx/scene/doc-files/cssref.html
*/

package edu.ltu.dsmproject.desktop;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

// Display the menu
public class Menu extends Application {
    public void launchGUI() {
        launch();
    }

    @Override
    public void start(Stage stage) {
        // Create a vertical box pane and set its style
        VBox menu = new VBox(8);
        menu.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;" + "-fx-alignment: center;");

        // Create guest, sign up, and sign in button
        Button signInButton = NodeStyle.createButton("Sign In", 125.0), guestButton = NodeStyle.createBlackButton("Guest", 125.0), signUpButton = NodeStyle.createBlackButton("Sign Up", 125.0);

        // Place the title and three buttons in the box pane
        menu.getChildren().addAll(NodeStyle.setTitle(), guestButton, signUpButton, signInButton);

        // Close current stage and show guest stage
		guestButton.setOnMouseClicked(e -> {
			stage.close();
            new Guest().start(stage);
        });

        // Close current stage and show sign up stage
        signUpButton.setOnMouseClicked(e -> {
            stage.close();
            new SignUp().start(stage);
        });

        // Close current stage and show sign in stage
        signInButton.setOnMouseClicked(e -> {
            stage.close();
            new SignIn().start(stage);
        });

        // Set stage properties
		stage.setTitle("Welcome");
		stage.setScene(new Scene(menu));
		stage.setResizable(false); // Diable resize function
		stage.show();
        NodeStyle.centerStage(stage);
    }
}