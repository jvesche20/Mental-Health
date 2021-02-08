/**
Administrator Stage class version: 1.0
@author ChenXiao Liu
*/

package edu.ltu.dsmproject.desktop;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class Administrator extends Application {
    public void start(Stage stage) {
        Button doctor = NodeStyle.createBlackButton("Doctor View", 125.0);
        Button patient = NodeStyle.createBlackButton("Patient View", 125.0);
        Button admin = NodeStyle.createBlackButton("Manage User", 125.0);

        doctor.setOnMouseClicked(e -> {
            stage.close();
            new Doctor().start(stage);
        });

        patient.setOnMouseClicked(e -> {
            stage.close();
            new PatientStage(stage, "admin");
        });

        admin.setOnMouseClicked(e -> {
            stage.close();
            new UserManagement().start(stage);
        });

        // Set vbox style and place all nodes in the vbox
        VBox vBox = new VBox(8);
        vBox.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;" + "-fx-alignment: center;");
        vBox.getChildren().addAll(NodeStyle.setTitle(), doctor, patient, admin, NodeStyle.goBackButton("Back to Menu", stage));

        stage.setTitle("Administrator"); // Set title for the stage
        stage.setScene(new Scene(vBox)); // Place the box pane in a scene, and then place the scene in the stage
        stage.show(); // Display the stage
        NodeStyle.centerStage(stage);
    }
}