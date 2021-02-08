/**
Sign In class version: 2.0
@author ChenXiao Liu
Update:
1. Invoking different stage according to different user types
*/

package edu.ltu.dsmproject.desktop;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import edu.ltu.dsmproject.dataaccess.dao.UserDatabaseAccessObject;
import java.sql.SQLException;

// Display a window for sign in
public class SignIn extends Application {
    @Override
    public void start(Stage stage) {
        // Create a vertical box pane and set its style
        VBox vbox = Node.getVBox(8.0, "center");

        // Create text field for username and password, then set their styles
        TextField username = Node.getField("text", "Username");
        TextField password = Node.getField("password", "Password");
        Button signIn = Node.getButton("pink", "Sign In", 125.0); // Create the sign in button
        
        // Place text fields and button in the box pane
        vbox.getChildren().addAll(Node.getLabel("title", "Mental Health System"), username, password, signIn, Node.goBack("Back to Menu", stage));

        // Check if username matches password
        signIn.setOnMouseClicked(event -> {
            try {   // If not match, display invalid message by message box
                if(!UserDatabaseAccessObject.getInstance().checkUserCredentials(username.getText(), password.getText())) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Username or Passowrd!\nPlease enter again!").showAndWait();
                }
                else { // Otherwise, close the current stage, and go to new stage
                    stage.close();
                    if (UserDatabaseAccessObject.getInstance().permissionLevelOfUser.equals("Admin")) {
                        new Administrator().start(stage);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        stage.setTitle("Sign In"); // Set title for the stage
        stage.setScene(new Scene(vbox)); // Place the box pane in a scene, and then place the scene in the stage
        stage.show(); // Display the stage
        Node.centerStage(stage);
    }    
}