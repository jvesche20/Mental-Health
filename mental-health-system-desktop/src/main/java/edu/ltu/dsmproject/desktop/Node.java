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
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class Node {
    // Create the go back button with same style for any stage
    public static final Button goBack(String action, Stage stage) {
        Button button = new Button("<< Go Back");
        button.setId("button-goback");
        
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
	public static final Button logout(Stage stage) {
		Button button = new Button("Logout");
        button.setId("button-goback");
        button.setOnMouseClicked(e -> {
            stage.close();
            new Menu().start(stage);
        });
		return button;
	}	
	
    // Create the button with same style for guest and sign up
    public static final Button getButton(String style, String text, double width) {
        Button button = new Button(text);
        button.setId("button-" + style);
        button.setPrefWidth(width);
        return button;
    }

    public static final VBox getVBox(double spacing, String alignment) {
        VBox vbox = new VBox(spacing);
        vbox.getStylesheets().add("style.css");
        if (alignment.equals("left")) {
            vbox.setId("vbox-left");
        }
        return vbox;
    }

    public static final void centerStage(Stage stage) {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
		stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static final TextField getField(String type, String promptText) {
        TextField field = type.equals("text") ? new TextField() : new PasswordField();
        field.setPromptText(promptText);
        return field;
    }

    public static final Label getLabel(String type, String text) {
        Label label = new Label(text);
        if (type.equals("pink")) {
            label.setId("label-pink");
        }
        else {
            label.setId("label-" + type);
            VBox.setMargin(label, new Insets(0, 0, 15, 0));
        }
        return label;
    }
}