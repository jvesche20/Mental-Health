package edu.ltu.dsmproject.desktop;

import edu.ltu.dsmproject.dataaccess.dao.UserDatabaseAccessObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import javax.swing.text.*;
import java.awt.event.*;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
/**
 * Provides the registration form.
 */
public class NewUserForm extends JFrame {

    private static final String UI_FONT_FAMILY = "Arial";
    private static final Font CHECKBOX_FONT = new Font(UI_FONT_FAMILY, Font.PLAIN, 15);
    private static final Font RADIO_BUTTON_FONT = new Font(UI_FONT_FAMILY, Font.PLAIN, 15);
    private static final Font TEXT_FIELD_FONT = new Font(UI_FONT_FAMILY, Font.PLAIN, 15);
    private static final Font RESULTS_FONT = new Font(UI_FONT_FAMILY, Font.PLAIN, 20);
    private static final Font TITLE_FONT = new Font(UI_FONT_FAMILY, Font.PLAIN, 30);
    private static final Font TEXT_FIELD_LABEL_FONT = new Font(UI_FONT_FAMILY, Font.PLAIN, 20);

    private JTextField usernameTextField;
    private JTextField passwordPasswordField;
    private JTextField confirmPasswordPasswordField;
    private ButtonGroup userTypeRadioButtonsGroup;
    private final JCheckBox termsAndConditionsCheckbox;
    private final JButton submitButton;
    private final JLabel resultLabel;
	private final JTextArea terms;
	private final JScrollPane scroll;
	
	
	

    public NewUserForm()
    {	
		
		try {
			File file = new File("src/main/java/edu/ltu/dsmproject/desktop/Terms/Terms.txt");
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
        setTitle("Registration Form");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        Container container = getContentPane();
        container.setLayout(null);

        var title = new JLabel("Registration Form");
        title.setFont(TITLE_FONT);
        title.setSize(300, 30);
        title.setLocation(300, 30);
        container.add(title);

        container.add(createUserCredentialsPanel());

        container.add(createPermissionsPanel());
		
		
		
		
		terms = new JTextArea();
		try (BufferedReader reader = new BufferedReader(new FileReader (new File("src/main/java/edu/ltu/dsmproject/desktop/Terms/Terms.txt")))) {
            terms.read(reader, "File");
        } catch (IOException exp) {
            exp.printStackTrace();
        }
		
		
		terms.setEditable(false);  
		terms.setVisible(true);
		terms.setLineWrap(true);
		
		scroll = new JScrollPane(terms);
		
		scroll.setSize(350,150);
		scroll.setLocation(100,240);
		scroll.setVisible(true);
	
		
		
		container.add(scroll);
		
		
        termsAndConditionsCheckbox = new JCheckBox("Accept Terms And Conditions.");
        termsAndConditionsCheckbox.setFont(CHECKBOX_FONT);
        termsAndConditionsCheckbox.setSize(250, 20);
        termsAndConditionsCheckbox.setLocation(150, 400);
        termsAndConditionsCheckbox.addActionListener(actionEvent -> updateReadyToSubmit());
        container.add(termsAndConditionsCheckbox);

        submitButton = new JButton("Submit");
        submitButton.setFont(RADIO_BUTTON_FONT);
        submitButton.setSize(100, 20);
        submitButton.setLocation(150, 450);
        submitButton.addActionListener(e -> submitForm());
        container.add(submitButton);

        var resetButton = new JButton("Reset");
        resetButton.setFont(RADIO_BUTTON_FONT);
        resetButton.setSize(100, 20);
        resetButton.setLocation(270, 450);
        resetButton.addActionListener(actionEvent -> resetForm());
        container.add(resetButton);

        resultLabel = new JLabel("");
        resultLabel.setFont(RESULTS_FONT);
        resultLabel.setSize(500, 25);
        resultLabel.setLocation(100, 500);
        container.add(resultLabel);

        setUpTextFieldListening();

        updateReadyToSubmit();
        setVisible(true);
    }
	
	
    private JPanel createUserCredentialsPanel() {
        var userCredentialsPanel = new JPanel(new GridBagLayout());

        var usernameLabelConstraints = new GridBagConstraints();
        usernameLabelConstraints.gridx = 0;
        usernameLabelConstraints.gridy = 0;
        usernameLabelConstraints.anchor = GridBagConstraints.LINE_START;
        usernameLabelConstraints.insets = new Insets(0, 0, 2, 2);
        var usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(TEXT_FIELD_LABEL_FONT);
        userCredentialsPanel.add(usernameLabel, usernameLabelConstraints);

        var usernameFieldConstraints = new GridBagConstraints();
        usernameFieldConstraints.gridx = 1;
        usernameFieldConstraints.gridy = 0;
        usernameFieldConstraints.insets = new Insets(0, 0, 2, 0);
        usernameFieldConstraints.weightx = 1;
        usernameFieldConstraints.fill = GridBagConstraints.BOTH;
        usernameTextField = new JTextField();
        usernameTextField.setFont(TEXT_FIELD_FONT);
        userCredentialsPanel.add(usernameTextField, usernameFieldConstraints);

        var passwordLabelConstraints = new GridBagConstraints();
        passwordLabelConstraints.gridx = 0;
        passwordLabelConstraints.gridy = 1;
        passwordLabelConstraints.anchor = GridBagConstraints.LINE_START;
        passwordLabelConstraints.insets = new Insets(0, 0, 2, 2);
        var passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(TEXT_FIELD_LABEL_FONT);
        userCredentialsPanel.add(passwordLabel, passwordLabelConstraints);

        var passwordPasswordFieldConstraints = new GridBagConstraints();
        passwordPasswordFieldConstraints.gridx = 1;
        passwordPasswordFieldConstraints.gridy = 1;
        passwordPasswordFieldConstraints.insets = new Insets(0, 0, 2, 0);
        passwordPasswordFieldConstraints.weightx = 1;
        passwordPasswordFieldConstraints.fill = GridBagConstraints.BOTH;
        passwordPasswordField = new JPasswordField();
        passwordPasswordField.setFont(TEXT_FIELD_FONT);
        userCredentialsPanel.add(passwordPasswordField, passwordPasswordFieldConstraints);

        var confirmPasswordLabelConstraints = new GridBagConstraints();
        confirmPasswordLabelConstraints.gridx = 0;
        confirmPasswordLabelConstraints.gridy = 2;
        confirmPasswordLabelConstraints.anchor = GridBagConstraints.LINE_START;
        confirmPasswordLabelConstraints.insets = new Insets(0, 0, 2, 2);
        var confirmPasswordLabel = new JLabel("Confirm password:");
        confirmPasswordLabel.setFont(TEXT_FIELD_LABEL_FONT);
        userCredentialsPanel.add(confirmPasswordLabel, confirmPasswordLabelConstraints);

        var confirmPasswordPasswordFieldConstraints = new GridBagConstraints();
        confirmPasswordPasswordFieldConstraints.gridx = 1;
        confirmPasswordPasswordFieldConstraints.gridy = 2;
        confirmPasswordPasswordFieldConstraints.insets = new Insets(0, 0, 2, 0);
        confirmPasswordPasswordFieldConstraints.weightx = 1;
        confirmPasswordPasswordFieldConstraints.fill = GridBagConstraints.BOTH;
        confirmPasswordPasswordField = new JPasswordField();
        confirmPasswordPasswordField.setFont(TEXT_FIELD_FONT);
        userCredentialsPanel.add(confirmPasswordPasswordField, confirmPasswordPasswordFieldConstraints);

        userCredentialsPanel.setSize(300, 80);
        userCredentialsPanel.setLocation(100, 100);

        return userCredentialsPanel;
    }

    private JPanel createPermissionsPanel() {
        final var permissionsPanel = new JPanel();
        permissionsPanel.setLayout(new BoxLayout(permissionsPanel, BoxLayout.LINE_AXIS));
        userTypeRadioButtonsGroup = new ButtonGroup();

        final var permissionLevels = new String[] { "Guest", "Patient", "Doctor", "Admin" };

        var permissionLevelLabel = new JLabel("Permission Level");
        permissionLevelLabel.setFont(new Font(UI_FONT_FAMILY, Font.PLAIN, 13));
        permissionsPanel.add(permissionLevelLabel);

        for (String permissionLevel : permissionLevels) {
            var radioButton = new JRadioButton((permissionLevel));
            radioButton.setFont(RADIO_BUTTON_FONT);
            radioButton.setSelected(false);
            radioButton.setActionCommand(permissionLevel);
            radioButton.addActionListener(actionEvent -> updateReadyToSubmit());

            userTypeRadioButtonsGroup.add(radioButton);
            permissionsPanel.add(radioButton);
        }

        permissionsPanel.setSize(500, 30);
        permissionsPanel.setLocation(100, 200);

        return permissionsPanel;
    }

    private boolean enteredPasswordsDoNotMatch() {
        final String password = passwordPasswordField.getText();
        final String confirmPassword = confirmPasswordPasswordField.getText();
        return !password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword);
    }
	
	private boolean passwordLength(){
		
		final String password = passwordPasswordField.getText();
		if (password.length() < 6){
			return false;
		}
		else{
			return true;
		}
		
	}
	
    private String getPermissionLevel() {
        try {
            return userTypeRadioButtonsGroup.getSelection().getActionCommand();
        }
        catch (NullPointerException e) {
            throw new RuntimeException("Permission level has not been set.");
        }
    }

    private boolean hasMatchingPasswordsEntered() {
        final String password = passwordPasswordField.getText();
        final String confirmPassword = confirmPasswordPasswordField.getText();
        return !password.isEmpty() && !confirmPassword.isEmpty() && password.equals(confirmPassword);
    }

    private boolean hasPermissionSelected() {
        return Collections.list(userTypeRadioButtonsGroup.getElements()).stream()
                .anyMatch(AbstractButton::isSelected);
    }

    private void resetForm() {
        usernameTextField.setText("");
        passwordPasswordField.setText("");
        confirmPasswordPasswordField.setText("");
        userTypeRadioButtonsGroup.clearSelection();
        resultLabel.setText("");
        termsAndConditionsCheckbox.setSelected(false);
    }

    private void setUpTextFieldListening() {
        var textFields = new JTextField[] { usernameTextField, passwordPasswordField, confirmPasswordPasswordField };
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateReadyToSubmit();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateReadyToSubmit();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateReadyToSubmit();
            }
        };
        for (JTextField textField : textFields) {
            textField.getDocument().addDocumentListener(documentListener);
        }
    }

    private void submitForm() {
        final String resultText = trySubmitNewUser()
                ? "User registration successful."
                : "User registration failed";
        resultLabel.setText(resultText);
		
    }

    private boolean trySubmitNewUser() {
        try {
            final String username = usernameTextField.getText();
            final String password = passwordPasswordField.getText();
            final String permissionLevel = getPermissionLevel();
            UserDatabaseAccessObject.getInstance().addNewUser(0, username, password, permissionLevel);
            return true;
        } catch (SQLException ex) {
            System.out.println("New user submission to database failed: " + ex.getMessage());
            return false;
        }
    }

    private void updateReadyToSubmit() {
        boolean readyToSubmit = !usernameTextField.getText().isEmpty()
                && hasMatchingPasswordsEntered()
                && hasPermissionSelected()
                && termsAndConditionsCheckbox.isSelected()
				&& passwordLength();
        resultLabel.setText(enteredPasswordsDoNotMatch() ? "Entered passwords do not match." : "");
        resultLabel.setText(!passwordLength() ? "Password Length: 6 characters or more" : "");
        submitButton.setEnabled(readyToSubmit);
    }
}

