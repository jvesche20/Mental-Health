/**
Guest class version: 1.1
@author ChenXiao Liu
Update:
1. Changed the button name
2. Change the way invoking the patient stage
*/

package edu.ltu.dsmproject.desktop;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

// Display the guest window
public class Guest extends Application {
    @Override
    public void start(Stage stage) {
        Button patient = NodeStyle.createBlackButton("Describe Symptoms", 150.0);
        Label title = NodeStyle.setTitle();
        // Create a vertical box pane and set its style
        VBox vbox = new VBox(8);
        vbox.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;" + "-fx-alignment: center;");
        vbox.getChildren().addAll(title, patient, NodeStyle.goBackButton("Back to Menu", stage));

        patient.setOnMouseClicked(e -> {
            stage.close();
            new PatientStage(stage, "patient");
        });

        // Set stage properties
		stage.setTitle("Guest Menu");
		stage.setScene(new Scene(vbox));
		stage.setResizable(false); // Diable resize function
		stage.show();
        NodeStyle.centerStage(stage);
    }
}