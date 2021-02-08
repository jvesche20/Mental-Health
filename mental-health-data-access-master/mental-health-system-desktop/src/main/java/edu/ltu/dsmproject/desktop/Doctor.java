/**
Doctor Stage class version: 1.0
@author ChenXiao Liu
*/

package edu.ltu.dsmproject.desktop;

import edu.ltu.dsmproject.dataaccess.dao.PatientsDatabaseAccessObject;
import edu.ltu.dsmproject.dataaccess.domain.*;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import edu.ltu.dsmproject.desktop.Diagnosis.Illness;

public class Doctor extends Application {
    private TableView<Patient> table = new TableView<Patient>();
    private List<Patient> patient;

    // Get all patient information from the database and add them to the patient table
    private void loadPatient() {
        patient = PatientsDatabaseAccessObject.getInstance().getPatients();
        table.getItems().clear();
        for (int i = 0; i < patient.size(); ++i) {
            table.getItems().add(patient.get(i));
        }
        table.refresh();
    }
    // Create a child stage for current window
    private Stage getChildStage(Stage parent) {
        Stage child = new Stage();
        child.initOwner(parent);
        child.initModality(Modality.WINDOW_MODAL);
        return child;
    }

    @Override
    public void start(Stage stage) {         
        table.setMinWidth(700);
        loadPatient();

        // Create table columns and add them to the table
        TableColumn<Patient, Number> id = new TableColumn<Patient, Number>("ID"), diagnosedID = new TableColumn<Patient, Number>("Diagnosis");
        TableColumn<Patient, String> name = new TableColumn<Patient, String>("Name"), gender = new TableColumn<Patient, String>("Gender"), date = new TableColumn<Patient, String>("Date Diagnosed");
        id.setCellValueFactory(new PropertyValueFactory<Patient, Number>("patientID"));
        name.setCellValueFactory(new PropertyValueFactory<Patient, String>("patientName"));
        gender.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
        diagnosedID.setCellValueFactory(new PropertyValueFactory<Patient, Number>("diagnosedID"));
        date.setCellValueFactory(new PropertyValueFactory<Patient, String>("diagnosisDate"));
        table.getColumns().add(id);
        table.getColumns().add(name);
        table.getColumns().add(gender);
        table.getColumns().add(diagnosedID);
        table.getColumns().add(date);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox hBoxForLabel = new HBox(), hBox = new HBox();
        Region spacer = new Region(), spacer2 = new Region();
        Button addPaitent = NodeStyle.createBlackButton("Add Patient", 100.0);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBox.getChildren().addAll(NodeStyle.goBackButton("Back to Menu", stage), spacer, addPaitent, NodeStyle.logoutButton("Back to Menu", stage) );
		
		
		
		
		
        // Allow doctors to add new patient after click the button
        addPaitent.setOnAction(e -> {
            Stage newStage = getChildStage(stage);
            new PatientStage(newStage, "doctor");
            loadPatient(); // Add new patient to the table
            table.scrollTo(patient.size() - 1); // Highlight new patient
            table.getSelectionModel().selectLast();
        });

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Stage newStage = getChildStage(stage);
                Patient patient = table.getSelectionModel().getSelectedItem(); // Get patient information
                
                Label nameLabel = NodeStyle.pinkBorderLabel("Name: " + patient.patientName), genderLabel = NodeStyle.pinkBorderLabel("Gender: " + patient.gender),
                    preIllness = NodeStyle.pinkBorderLabel("Pre-existing Illnesses:"), drugAlcoholUse = NodeStyle.pinkBorderLabel("Drug / Alcohol Use:"),
                    Keyword = NodeStyle.pinkBorderLabel("Keywords:"), illnesses = NodeStyle.pinkBorderLabel("Possible Illnesses:");

                // Put known information into text area and allow doctors to edit
                TextArea preIllnessArea = new TextArea(), keywordArea = new TextArea(patient.words);
                preIllnessArea.setWrapText(true);
                preIllnessArea.setPrefHeight(75);
                keywordArea.setWrapText(true);
                keywordArea.setPrefHeight(75);

                // Create a table to display possible illnesses for the selected patient and add all data to the table
                TableView<Illness> illnessTable = new Diagnosis().getIllnessTable(patient);

                HBox hBoxForCheckBox = new HBox(15), hBoxForButton = new HBox();
                CheckBox drug = new CheckBox("Drug"), alcohol = new CheckBox("Alcohol");
                hBoxForCheckBox.getChildren().addAll(drugAlcoholUse, drug, alcohol);

                Region spacer3 = new Region();
                Button submit = NodeStyle.createButton("Submit Change", 150.0);
                hBoxForButton.getChildren().addAll(NodeStyle.goBackButton("Back to Doctor View", newStage), spacer3, submit, NodeStyle.logoutButton("Back to Menu", stage));
                HBox.setHgrow(spacer3, Priority.ALWAYS);

                submit.setOnMouseClicked(event -> {
                    patient.problemDescription = keywordArea.getText();
                    patient.words = keywordArea.getText();
                    new Diagnosis(patient, newStage, "Edit Patient");
                });

                VBox vBoxForNewStage = new VBox(10);
                vBoxForNewStage.getChildren().addAll(nameLabel, genderLabel, preIllness, preIllnessArea, hBoxForCheckBox, Keyword, keywordArea, illnesses, illnessTable, hBoxForButton);
                vBoxForNewStage.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;");

                newStage.setTitle("Edit Patient");
                newStage.setScene(new Scene(vBoxForNewStage));
                newStage.showAndWait();
                loadPatient(); // Re load table after doctor back to the doctor view
                table.scrollTo(patient);
                table.getSelectionModel().select(patient);
            }
        });

        hBoxForLabel.getChildren().addAll(spacer2, new Label("*Double click a row to view detail information of a patient"));
        hBoxForLabel.setStyle("-fx-border-color: transparent transparent #FA8072 transparent;");
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        VBox vbox = new VBox(8);
        vbox.getChildren().addAll(new Label("Patient:"), table, hBoxForLabel, hBox);
        vbox.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;");

        // Set stage properties
		stage.setTitle("Doctor");
		stage.setScene(new Scene(vbox));
		stage.setResizable(false); // Diable resize function
		stage.show();
        NodeStyle.centerStage(stage);
    }    
}