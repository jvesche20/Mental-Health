/**
UserManagement class version: 1.0
@author ChenXiao Liu
*/

package edu.ltu.dsmproject.desktop;

import edu.ltu.dsmproject.dataaccess.dao.*;
import edu.ltu.dsmproject.dataaccess.domain.*;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class UserManagement extends Application {
    @Override
    public void start(Stage stage) {
        TableView<User> table = new TableView<User>();
        List<User> user = UserDatabaseAccessObject.getInstance().getUserList();

        for (int i = 0; i < user.size(); ++i) {
            table.getItems().add(user.get(i));
        }

        TableColumn<User, Number> id = new TableColumn<User, Number>("ID");
        TableColumn<User, String> userName = new TableColumn<User, String>("Username"), password = new TableColumn<User, String>("Password"), type = new TableColumn<User, String>("Type");
        id.setCellValueFactory(new PropertyValueFactory<User, Number>("userID"));
        userName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        type.setCellValueFactory(new PropertyValueFactory<User, String>("permissionLevel"));
        table.getColumns().add(id);
        table.getColumns().add(userName);
        table.getColumns().add(password);
        table.getColumns().add(type);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vbox = Node.getVBox(10.0, "left");
        vbox.getChildren().addAll(Node.getLabel("pink", "User Database:"), table, Node.goBack("Back to Admin Menu", stage), Node.logout(stage));

        // Set stage properties
		stage.setTitle("User Management");
		stage.setScene(new Scene(vbox));
		stage.setResizable(false); // Diable resize function
		stage.show();
        Node.centerStage(stage);
    }    
}