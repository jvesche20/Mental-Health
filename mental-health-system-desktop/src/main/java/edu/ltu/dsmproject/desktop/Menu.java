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
        VBox menu = Node.getVBox(8.0, "center");

        // Create guest, sign up, and sign in button
        Button signIn = Node.getButton("pink", "Sign In", 125.0);
        Button guest = Node.getButton("black", "Guest", 125.0);
        Button signUp = Node.getButton("black", "Sign Up", 125.0);

        // Place the title and three buttons in the box pane
        menu.getChildren().addAll(Node.getLabel("title", "Mental Health System"), guest, signUp, signIn);

        // Close current stage and show guest stage
		guest.setOnMouseClicked(e -> {
			stage.close();
            new Guest().start(stage);
        });

        // Close current stage and show sign up stage
        signUp.setOnMouseClicked(e -> {
            stage.close();
            new SignUp().start(stage);
        });

        // Close current stage and show sign in stage
        signIn.setOnMouseClicked(e -> {
            stage.close();
            new SignIn().start(stage);
        });

        // Set stage properties
		stage.setTitle("Welcome");
		stage.setScene(new Scene(menu));
		stage.setResizable(false); // Diable resize function
		stage.show();
        Node.centerStage(stage);
    }
}