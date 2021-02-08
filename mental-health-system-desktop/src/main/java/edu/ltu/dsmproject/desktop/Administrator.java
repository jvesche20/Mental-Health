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
        Button doctor = Node.getButton("black", "Doctor View", 125.0);
        Button patient = Node.getButton("black", "Patient View", 125.0);
        Button admin = Node.getButton("black", "Manage User", 125.0);

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
        VBox vBox = Node.getVBox(8.0, "center");
        vBox.getChildren().addAll(Node.getLabel("title", "Mental Health System"), doctor, patient, admin, Node.goBack("Back to Menu", stage));

        stage.setTitle("Administrator"); // Set title for the stage
        stage.setScene(new Scene(vBox)); // Place the box pane in a scene, and then place the scene in the stage
        stage.show(); // Display the stage
        Node.centerStage(stage);
    }
}