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
        Button patient = Node.getButton("black", "Describe Symptoms", 150.0);
        // Create a vertical box pane and set its style
        VBox vbox = Node.getVBox(8.0, "center");
        vbox.getChildren().addAll(Node.getLabel("title", "Mental Health System"), patient, Node.goBack("Back to Menu", stage));

        patient.setOnMouseClicked(e -> {
            stage.close();
            new PatientStage(stage, "patient");
        });

        // Set stage properties
		stage.setTitle("Guest Menu");
		stage.setScene(new Scene(vbox));
		stage.setResizable(false); // Diable resize function
		stage.show();
        Node.centerStage(stage);
    }
}