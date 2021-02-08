/**
Sign Up class version: 1.0
@author ChenXiao Liu
*/

package edu.ltu.dsmproject.desktop;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import edu.ltu.dsmproject.dataaccess.dao.UserDatabaseAccessObject;
import java.sql.SQLException;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;


// Display the sign up window
public class SignUp extends Application {
    private TextField username = NodeStyle.createTextField("Username");
    private PasswordField password = NodeStyle.createPasswordField("Password (At least 6 characters)"), confirmePassword = NodeStyle.createPasswordField("Confirm Password");
    private RadioButton patient = new RadioButton("Patient"), doctor = new RadioButton("Doctor"), admin = new RadioButton("Administrator");
    private ToggleGroup group = new ToggleGroup();
    private CheckBox checkBox = new CheckBox("I agree with the Term and Condition");
    private Button signIn = NodeStyle.createButton("Sign Up", 175.0);
    private final String CORRECT_INPUT_STYLE = NodeStyle.PROMPT_TEXT + "-fx-text-box-border: lightgreen;" + "-fx-focus-color: #adff2f;";
    private final String INCORRECT_INPUT_STYLE = NodeStyle.PROMPT_TEXT + "-fx-text-box-border: red;" + "-fx-focus-color: #b22222;";
    
    @Override
    public void start(Stage stage) {
		
		
		
		
        signIn.setDisable(true); // Forbid user click the sign in button

        username.setOnKeyReleased(e -> listenVBoxAction());

        // Check if user entered correctly
        password.setOnKeyReleased(e -> {
            if (password.getText().length() < 6) {
                password.setStyle(INCORRECT_INPUT_STYLE);
            }
            else {
                password.setStyle(CORRECT_INPUT_STYLE);
            }
            checkIfPasswordIsMatch();
            listenVBoxAction();
        });

        // Check if confirme password is the same as the password
        confirmePassword.setOnKeyReleased(e -> {
            checkIfPasswordIsMatch();
            listenVBoxAction();
        });

        // Createt the title for the window
        Label title = new Label("Create Your Account");
        title.setStyle("-fx-font-size: 20");
        VBox.setMargin(title, new Insets(0, 0, 15, 0));

        // Put all radio button in a group
        group.getToggles().addAll(patient, doctor, admin);

        patient.setOnAction(e -> listenVBoxAction());
        doctor.setOnAction(e -> listenVBoxAction());
        admin.setOnAction(e -> listenVBoxAction());
        checkBox.setOnAction(e -> listenVBoxAction());

        // Submit the new user information to the database, and prompt user to sign in
        signIn.setOnMouseClicked(e -> {
            try {
                UserDatabaseAccessObject.getInstance().addNewUser(0, username.getText(), password.getText(), ((RadioButton)group.getSelectedToggle()).getText());
                new Alert(Alert.AlertType.INFORMATION, "Successed!").showAndWait();
                stage.close();
                new SignIn().start(stage);
            } catch (SQLException ex) { // If something wrong, show the message
                new Alert(Alert.AlertType.ERROR, "Failed to sign up!\nPlease try again!").showAndWait();
            }
        });

        // Create a box for radio buttons 
        HBox panForRadioButtons = new HBox(20);
        panForRadioButtons.getChildren().addAll(patient, doctor, admin);

        // Create a text area for term and condition
        TextArea termAndCondition = new TextArea();
		
		
		try {
			File file = new File("src/main/java/edu/ltu/dsmproject/desktop/Terms/Terms.txt");
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}
			Scanner scan = new Scanner(file);
			String data;
			while(scan.hasNextLine()){
				data = scan.nextLine();
				//System.out.println(data);
				
				termAndCondition.appendText("\n" + data);
				//termAndCondition.setText("test");
			}
			scan.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		termAndCondition.setWrapText(true); // Automatically make new line
        termAndCondition.setEditable(false); // diable users to edit the text (read only)

        // Create a box, place all nodes in the box, and set its style
        VBox signUpBox = new VBox(12);
        signUpBox.getChildren().addAll(title, username, password, confirmePassword, panForRadioButtons, termAndCondition, checkBox, signIn, NodeStyle.goBackButton("Back to Menu", stage));
        signUpBox.setStyle("-fx-background-color: #FFFFFF;" + "-fx-padding: 25 35 25 35;" + "-fx-alignment: center;");
        
        // Set stage properties, and show the stage
		stage.setTitle("Sign Up");
		stage.setScene(new Scene(signUpBox));
		stage.show();
        NodeStyle.centerStage(stage);
    }

    // Check if every information is inputed
    private void listenVBoxAction() {
        if ((patient.isSelected() || doctor.isSelected() || admin.isSelected())
        && checkBox.isSelected()
        && !username.getText().isEmpty()
        && password.getStyle().contains("lightgreen")
        && confirmePassword.getStyle().contains("lightgreen")) {
            signIn.setDisable(false); // If it is not, then disable user to click the sign up button
        }
        else { // Otherwise, allow user to click the sign up button
            signIn.setDisable(true);
        }
    }

    // Check if password is the same as the confirm password
    private void checkIfPasswordIsMatch() {
        if (!password.getText().equals(confirmePassword.getText())) {
            confirmePassword.setStyle(INCORRECT_INPUT_STYLE);
        }
        else {
            confirmePassword.setStyle(CORRECT_INPUT_STYLE);
        }
    }
}