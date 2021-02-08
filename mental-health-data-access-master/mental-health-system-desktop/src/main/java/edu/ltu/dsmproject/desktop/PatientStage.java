/**
Patient Stage class version: 2.0
@author ChenXiao Liu
Update:
1. Changed the way invoking this stage
2. Perform different action according to different types of user
*/

package edu.ltu.dsmproject.desktop;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class PatientStage extends Application {
    private TextField firstName = NodeStyle.createTextField("First Name"), lastName = NodeStyle.createTextField("Last Name");
    private RadioButton male = new RadioButton("Male"), female = new RadioButton("Female");
    private ToggleGroup group = new ToggleGroup();
    private TextArea description = new TextArea();
    private Button submit = NodeStyle.createButton("Submit", 175.0);
    private String userType;

    PatientStage(Stage stage, String type) {
        userType = type;
        start(stage);
    }

    @Override
    public void start(Stage stage) {
        submit.setDisable(true); // Forbid user click the sign in button

        group.getToggles().addAll(male, female);

        // Create a box for radio buttons 
        HBox paneForRadioButtons = new HBox(20);
        paneForRadioButtons.getChildren().addAll(male, female);

        // Set the style for the text area
        description.setWrapText(true); // Automatically make new line
        description.setPromptText("Describe your symptom here...");
        description.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -40%);");

        firstName.setOnKeyReleased(e -> listenVBoxAction());
        lastName.setOnKeyReleased(e -> listenVBoxAction());
        male.setOnAction(e -> listenVBoxAction());
        female.setOnAction(e -> listenVBoxAction());
        description.setOnKeyReleased(e -> listenVBoxAction());

        submit.setOnAction(e -> {
            //save the new patient and start processing their disorder
            new Diagnosis(firstName.getText() + " " + lastName.getText(), description.getText(), male.isSelected(), stage, userType);		
		});

        // Create a vertical box pane and set its style
        VBox vbox = new VBox(8);
        vbox.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;" + "-fx-alignment: center;");
        if (userType.equals("doctor")) {
            vbox.getChildren().addAll(firstName, lastName, paneForRadioButtons, description, submit);
        }
        else if (userType.equals("admin")) {
            vbox.getChildren().addAll(firstName, lastName, paneForRadioButtons, description, submit, NodeStyle.goBackButton("Back to Admin Menu", stage), NodeStyle.logoutButton("Back to Menu", stage));  
        }
        else {
            vbox.getChildren().addAll(firstName, lastName, paneForRadioButtons, description, submit, NodeStyle.goBackButton("Back to Guest Menu", stage), NodeStyle.logoutButton("Back to Menu", stage));
        }

        // Set stage properties
		stage.setTitle("Patient");
		stage.setScene(new Scene(vbox));
        stage.setResizable(false); // Diable resize function
        if (userType.equals("doctor")) {
            stage.showAndWait();
        }
        else {
            stage.show();
            NodeStyle.centerStage(stage);
        }
    }

    // Check if every information is inputed
    private void listenVBoxAction() {
        if ((male.isSelected() || female.isSelected())
        && !firstName.getText().isEmpty()
        && !lastName.getText().isEmpty()
        && !description.getText().isEmpty()) {
            submit.setDisable(false); // If it is not, then disable user to click the sign up button
        }
        else { // Otherwise, allow user to click the sign up button
            submit.setDisable(true);
        }
    }
}