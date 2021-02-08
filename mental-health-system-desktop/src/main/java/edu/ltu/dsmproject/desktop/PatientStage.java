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
import edu.ltu.dsmproject.dataaccess.domain.Patient;
import edu.ltu.dsmproject.dataaccess.dao.PatientsDatabaseAccessObject;

public class PatientStage extends Application {
    private TextField firstName = Node.getField("text", "First Name"), lastName = Node.getField("text", "Last Name");
    private RadioButton male = new RadioButton("Male"), female = new RadioButton("Female");
    private ToggleGroup group = new ToggleGroup();
    private TextArea description = new TextArea();
    private Button submit = Node.getButton("pink", "Submit", 175.0);
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

        firstName.setOnKeyReleased(e -> listenVBoxAction());
        lastName.setOnKeyReleased(e -> listenVBoxAction());
        male.setOnAction(e -> listenVBoxAction());
        female.setOnAction(e -> listenVBoxAction());
        description.setOnKeyReleased(e -> listenVBoxAction());

        submit.setOnAction(e -> {
            Patient patient = PatientsDatabaseAccessObject.getInstance().createAndAddPatient(firstName.getText() + " " + lastName.getText(), description.getText(), male.isSelected());
            //save the new patient and start processing their disorder
            new Diagnosis(patient, stage, userType);		
		});

        // Create a vertical box pane and set its style
        VBox vbox = Node.getVBox(8.0, "center");
        vbox.getChildren().addAll(firstName, lastName, paneForRadioButtons, description, submit);

        if (!userType.equals("doctor")) {
            vbox.getChildren().addAll(Node.goBack("Back to Guest Menu", stage), Node.logout(stage));
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
            Node.centerStage(stage);
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