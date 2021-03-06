/**
Diagnosis Stage class version: 2.0
@author ChenXiao Liu
Update:
1. Allow users to view all illnesses
2. Set different scences for different types of user
*/

package edu.ltu.dsmproject.desktop;

import edu.ltu.dsmproject.dataaccess.*;
import edu.ltu.dsmproject.dataaccess.dao.PatientsDatabaseAccessObject;
import edu.ltu.dsmproject.dataaccess.domain.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.value.*;
import java.util.List;

// Diagnose possible illnesses for patient based on the patient's description
public class Diagnosis extends Application {
    private Patient patient;
    private Diagnoser diagnoser;
    private Q question;
    private TableView<Illness> table = new TableView<Illness> ();
    private Illness[] illness = new Illness[10];
    private List<Disorder> disorders = PatientsDatabaseAccessObject.getInstance().getDisorders(); // Get all disorders from the database
    private ChoiceBox<String> choice = new ChoiceBox<String> ();
    private VBox vbox = new VBox(8);
    private Label illnesses = new Label("Possible Illness:"), number = new Label("Number of illness:"), questionLabel = new Label();
    private Score score;
    private HBox hBox = new HBox(5), hBoxForButton = new HBox(20), hBoxForViewIllnessList = new HBox();
    private String userType;

    // Take user information from the previous stage
    Diagnosis(String name, String description, boolean gender, Stage stage, String type) {
        userType = type;
        patient = PatientsDatabaseAccessObject.getInstance().createAndAddPatient(name, description, gender);
        diagnoser = new Diagnoser(patient);
        question = diagnoser.nextQ(0);
        setQuestion();
        start(stage);
    }

    // Take user information from the doctor stage
    Diagnosis(Patient pat, Stage stage, String type) {
        userType = type;
        patient = pat;
        diagnoser = new Diagnoser(patient);
        question = diagnoser.nextQ(0);
        setQuestion();
        start(stage);
    }

    Diagnosis() {}
    // Return illness table to the doctor stage
    public TableView<Illness> getIllnessTable(Patient patient) {
        diagnoser = new Diagnoser(patient);
        getIllness(); // Store top 10 possible illnesses
        displayIllness(10); // Load illness information to the table view
        setTable();
        return table;
    }

    private void setTable() {
        table.setPrefHeight(200);
        // Create table columns and load data to each columns
        TableColumn<Illness, Number> id = new TableColumn<Illness, Number> ("ID");
        id.setCellValueFactory(new PropertyValueFactory<Illness, Number>("id"));
        TableColumn<Illness, String> name = new TableColumn<Illness, String> ("Name"), percentage = new TableColumn<Illness, String> ("Percentage");
        name.setCellValueFactory(new PropertyValueFactory<Illness, String>("name"));
        name.setMinWidth(300);
        percentage.setCellValueFactory(new PropertyValueFactory<Illness, String>("percentage"));
        percentage.setPrefWidth(175);
        table.getColumns().add(id);
        table.getColumns().add(name);
        table.getColumns().add(percentage);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @Override
    public void start(Stage stage) {
        getIllness(); // Store top 10 possible illnesses
        displayIllness(3); // Load illness information to the table view
        setTable();

        // Set choice box
        choice.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        choice.setValue("3");
        // Update the table once users want to see more or less possible illnesses
        choice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String> () {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                displayIllness(Integer.parseInt(newValue));
            }
        });

        // Create a button for users to view all illnesses, and place the button at the right side of the window
        Region spacer = new Region(), spacer2 = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(illnesses, new Insets(9, 0, 0, 0));
        HBox.setMargin(number, new Insets(9, 0, 0, 0));
        hBox.getChildren().addAll(illnesses, spacer, number, choice);

        // Create yes and no buttons and add them to a hBox
        Button yes = NodeStyle.createBlackButton("Yes", 70.0), no = NodeStyle.createBlackButton("No", 70.0), viewAllIllness = new Button("View All Illnesses");
        hBoxForButton.getChildren().addAll(yes, no);

        // Update the patient information and diagnosis once users answered the question
        yes.setOnAction(e -> {
            patient.words = patient.words + " " + question.symName;
            patient.symptoms = patient.symptoms + " " + question.symName;
            patient.problemDescription = patient.problemDescription + " " + question.symName;
            updateDiagnosis();
        });

        no.setOnAction(e -> {
            patient.symptoms = patient.symptoms + " " + question.symName;
            updateDiagnosis();
        });

        // Set view all illnesses button action
        hBoxForViewIllnessList.getChildren().addAll(spacer2, viewAllIllness);
        final String NORMAL = "-fx-background-color: transparent;" + "-fx-text-fill: #4682b4;" + "-fx-underline: true;";
        viewAllIllness.setStyle(NORMAL);
        viewAllIllness.setOnMouseEntered(e -> {
            viewAllIllness.setCursor(Cursor.HAND);
            viewAllIllness.setStyle(NORMAL + "-fx-text-fill: #1e90ff;");
        });
        viewAllIllness.setOnMouseExited(e -> viewAllIllness.setStyle(NORMAL));
        viewAllIllness.setOnMousePressed(e -> {
            viewAllIllness.setStyle(NORMAL + "-fx-text-fill: #4169e1;");
        });
        viewAllIllness.setOnMouseClicked(e -> {
            new ViewAllIllnesses().start(new Stage());
        });
        hBoxForViewIllnessList.setStyle("-fx-border-color: transparent transparent #FA8072 transparent;");
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        // Place all nodes in the vBox and set vBox and question label style
        vbox.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;");
        if (userType.equals("doctor") || userType.equals("Edit Patient")) {
            vbox.getChildren().addAll(new Label("Name: " + patient.patientName), hBox, table, hBoxForViewIllnessList, questionLabel, hBoxForButton);
        }
        else if (userType.equals("admin")) {
            stage.close();
            vbox.getChildren().addAll(new Label("Name: " + patient.patientName), hBox, table, hBoxForViewIllnessList, questionLabel, hBoxForButton, NodeStyle.goBackButton("Back to Admin Menu", stage), NodeStyle.logoutButton("Back to Menu", stage));
        }
        else {
            stage.close();
            vbox.getChildren().addAll(new Label("Name: " + patient.patientName), hBox, table, hBoxForViewIllnessList, questionLabel, hBoxForButton, NodeStyle.goBackButton("Back to Guest Menu", stage), NodeStyle.logoutButton("Back to Menu", stage));
        }
        vbox.getChildren().get(0).setStyle("-fx-border-color: transparent transparent #FA8072 transparent;");
        questionLabel.setStyle("-fx-font-size: 14;");

        // Set stage properties
		stage.setTitle("Diagnosis");
		stage.setScene(new Scene(vbox));
        stage.setResizable(false); // Disable resize function
        if (userType.equals("Edit Patient")) {
            score = diagnoser.getScores().get(question.diagnosisID);
            if (score.matchPercentage >= 50) {
                showReport();
            }
        }
        else if (userType.equals("patient") || userType.equals("admin")) {
            stage.show();
        }
        NodeStyle.centerStage(stage);
    }

    // Create a Illness class to store information from different classes
    // Make sure this class is public so that the table view could easily get column data form this class
    public class Illness {
        private String name, percentage;
        private int id;

        Illness(String name, int id, String percentage) {
            this.name = name;
            this.id = id;
            this.percentage = percentage;
        }

        public String getName() { return name; }
        public int getId() { return id; }
        public String getPercentage() { return percentage; }
    }

    // Update illnesses
    private void getIllness() {
        for (int i = 0; i < 10; ++i) {
            for (Disorder disorderFromList : disorders) {
                if (disorderFromList.disorderID == diagnoser.getScores().get(i).dID) {
                    int id = diagnoser.getScores().get(i).dID;
                    illness[i] = new Illness(disorderFromList.disorderName, id, diagnoser.getScores().get(i).matchPercentage + "%");
                    break;
                }
            }
        }
    }

    // Update the table
    private void displayIllness(int number) {
        table.getItems().clear();
        for (int i = 0; i < number; ++i) {
            table.getItems().add(illness[i]);
        }
        table.refresh();
    }

    // Set questiones if diagnosis is updated
    private void setQuestion() {
        String questionText = question.question.text;		
        int spot = questionText.indexOf("_");
		questionLabel.setText(questionText.substring(0, spot) + question.symName + questionText.substring(spot + 1));
    }

    // Update the diagnosis once users answered questions
    private void updateDiagnosis() {
		diagnoser.findWordMatch();
        score = diagnoser.getScores().get(question.diagnosisID);
        getIllness();
        if(score.matchPercentage >= 50) {
            showReport();
        }
        else {
            if(question.diagnosisID > 3) {
                new Alert(Alert.AlertType.ERROR, "User symptoms did not match any disorder with 50%.\nConsider restarting diagnosis with more information.").showAndWait();
                showReport();
            }
            else {
                question = diagnoser.nextQ(question.diagnosisID);
                setQuestion();
                displayIllness(Integer.parseInt(choice.getValue()));
            }
        }
    }

    // Demonstrate results of the diagnosis
    private void showReport() {
        displayIllness(10);
        choice.setValue("10");
        questionLabel.setText("Diagnosis Completed!");
        hBoxForButton.getChildren().clear();

        ChoiceBox<String> chooseIllness = new ChoiceBox<String> ();
        for (int i = 0; i < 10; ++i) {
            chooseIllness.getItems().add("" + illness[i].id);
        }
        chooseIllness.setValue("" + illness[0].id);

        Label illnessSelection = new Label("Please select an illness:");
        illnessSelection.setStyle("-fx-font-size: 14;");

        Button submit = NodeStyle.createButton("Submit", 75.0);

        submit.setOnAction(e -> {
			if(patient.diagnosedID == 0) {
				patient.diagnosedID = score.dID;
            }
			patient.diagnosedID = Integer.parseInt(chooseIllness.getValue());            
            PatientsDatabaseAccessObject.getInstance().addPatientResults(patient);
            vbox.getChildren().remove(4);
            vbox.getChildren().remove(4);
            new Alert(Alert.AlertType.INFORMATION, "Successfully submitted the diagnosis case!").showAndWait();
        });

        hBoxForButton.getChildren().addAll(illnessSelection, chooseIllness, submit);
    }

    public class ViewAllIllnesses extends Application {
        @Override
        public void start(Stage stage) {
            TableView<Disorder> table = new TableView<Disorder> ();
            // Add all illnesses to the table
            for (int i = 0; i < disorders.size(); ++i) {
                table.getItems().add(disorders.get(i));
            }
            // Create table columns and load data to each columns
            TableColumn<Disorder, Number> id = new TableColumn<Disorder, Number> ("ID");
            id.setCellValueFactory(new PropertyValueFactory<Disorder, Number>("disorderID"));
            TableColumn<Disorder, String> name = new TableColumn<Disorder, String> ("Name");
            name.setCellValueFactory(new PropertyValueFactory<Disorder, String>("disorderName"));
            table.getColumns().add(id);
            table.getColumns().add(name);
    
            VBox vBox = new VBox(8);
            vBox.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;");
            vBox.getChildren().addAll(new Label("Illnesses:"), table);
    
            // Set stage properties
            stage.setTitle("All Illnesses");
            stage.setScene(new Scene(vBox));
            stage.show();
            NodeStyle.centerStage(stage);
        }
    }
}