/**
Node Style class version: 2.0
@author ChenXiao Liu
Update:
1. Made all method final
2. Added more click action for go back button
3. Added a label style
*/

package edu.ltu.dsmproject.desktop;

import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class NodeStyle {
    public static final String FOCUS = "-fx-focus-color: #FA8072;";
    public static final String PROMPT_TEXT = "-fx-prompt-text-fill: derive(-fx-control-inner-background, -40%);" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;"  + "-fx-faint-focus-color: transparent;";
    public static final String TEXT_FIELD = FOCUS + PROMPT_TEXT;

    // Create the go back button with same style for any stage
    public static final Button goBackButton(String action, Stage stage)
    {
        Button button = new Button("<< Go Back");
        final String normalStyle = "-fx-border-color: transparent transparent #FA8072 transparent;" + "-fx-background-color: transparent;" + "-fx-text-fill: #4682b4;";
        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> {
            button.setCursor(Cursor.HAND);
            button.setStyle(normalStyle + "-fx-text-fill: #1e90ff;");
        });
        button.setOnMouseExited(e -> {
            button.setStyle(normalStyle);
        });
        button.setOnMousePressed(e -> {
            button.setStyle(normalStyle + "-fx-text-fill: #4169e1;");
        });

        if (action.equals("Back to Menu"))
        {
            button.setOnMouseClicked(e -> {
                stage.close();
                new Menu().start(stage);
            });
        }
        else if (action.equals("Back to Guest Menu")) {
            button.setOnMouseClicked(e -> {
                stage.close();
                new Guest().start(stage);
            });
        }
        else if (action.equals("Back to Admin Menu")) {
            button.setOnMouseClicked(e -> {
                stage.close();
                new Administrator().start(stage);
            });
        }
        else if (action.equals("Back to Doctor View")) {
            button.setOnMouseClicked(e -> {
                stage.close();
            });
        }
        return button;
    }

	// Create a logout button 
	
	public static final Button logoutButton(String action, Stage stage){
		Button button = new Button("Logout");
		final String normalStyle = "-fx-border-color: transparent transparent #FA8072 transparent;" + "-fx-background-color: transparent;" + "-fx-text-fill: #4682b4;";

		button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> {
            button.setCursor(Cursor.HAND);
            button.setStyle(normalStyle + "-fx-text-fill: #1e90ff;");
        });
        button.setOnMouseExited(e -> {
            button.setStyle(normalStyle);
        });
        button.setOnMousePressed(e -> {
            button.setStyle(normalStyle + "-fx-text-fill: #4169e1;");
        });
		
		if (action.equals("Back to Menu"))
        {
            button.setOnMouseClicked(e -> {
                stage.close();
                new Menu().start(stage);
            });
        }
		return button;
	}
	
	
	
	
	
	
	
    // Create the button with same style for guest and sign up
    public static final Button createBlackButton(String text, double width) {
        Button button = new Button(text);
        String normalborderStyle = "-fx-border-color: black;" + "-fx-border-radius: 50;";
        String normalEventStyle = "-fx-text-fill: #ffffff;" + "-fx-background-radius: 50;";

        button.setPrefWidth(width);
        button.setStyle("-fx-background-color: transparent;" + normalborderStyle);
        button.setOnMouseEntered(e -> {
            button.setCursor(Cursor.HAND);
            button.setStyle("-fx-background-color: #000000;" + normalEventStyle + normalborderStyle);
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent;" + normalborderStyle);
        });
        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: #000000D7;" + normalEventStyle + normalborderStyle);
        });
        return button;
    }

    // Create the button with same style for sign in button in different classes
    public static final Button createButton(String text, double width) {
        Button button = new Button(text);
        String signInNormalStyle = "-fx-background-radius: 50;" + "-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;";

        button.setPrefWidth(width);
        button.setStyle("-fx-background-color: #FA8072;" + signInNormalStyle);
        button.setOnMouseEntered(e -> {
            button.setCursor(Cursor.HAND);
            button.setStyle("-fx-background-color: #FFA07A;" + signInNormalStyle);
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #FA8072;" + signInNormalStyle);
        });
        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: #F08080;" + signInNormalStyle);
        });
        return button;
    }

    // Set title of different windows with same style
    public static final Label setTitle() {
        Label title = new Label("Mental Health System");
        title.setStyle("-fx-font-size: 16");
        VBox.setMargin(title, new Insets(0, 0, 15, 0));
        return title;
    }

    public static final void centerStage(Stage stage) {
        // Center the stage
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
		stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static final TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle(TEXT_FIELD);
        return textField;
    }

    public static final PasswordField createPasswordField(String promptText) {
        PasswordField textField = new PasswordField();
        textField.setPromptText(promptText);
        textField.setStyle(TEXT_FIELD);
        return textField;
    }

    public static final Label pinkBorderLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-border-color: transparent transparent transparent #FA8072;" + "-fx-padding: 0 0 0 5");
        return label;
    }
}