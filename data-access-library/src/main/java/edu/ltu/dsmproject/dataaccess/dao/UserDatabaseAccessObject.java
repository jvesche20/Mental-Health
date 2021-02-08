package edu.ltu.dsmproject.dataaccess.dao;

import edu.ltu.dsmproject.dataaccess.domain.User;

//import java.security.Permission;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides the users portion of the data access layer for the mental health application as a singleton.
 */
public class UserDatabaseAccessObject extends MSAccessDatabaseAccessObject {
    private static final String MS_ACCESS_DB_FILENAME = "UserCredentials.accdb";

    private static UserDatabaseAccessObject instance = null;

    private final List<User> userList = new ArrayList<>();
    private final HashMap<String, String> userHashMap = new HashMap<>();
    public String permissionLevelOfUser;

    /**
     * Creates a new UserDatabaseHandler instance and opens the connection to the database.
     */
    private UserDatabaseAccessObject() {
        super(MS_ACCESS_DB_FILENAME);
        try {
            fillUsers();
            userListToDictionary();
        } catch (Exception e) {
            System.out.print("Failed to initialize user information from database: " + e);
        }
    }

    /**
     * Gets the singleton instance.
     * @return The singleton instance.
     */
    public static UserDatabaseAccessObject getInstance() {
        if (instance == null) {
            instance = new UserDatabaseAccessObject();
        }
        return instance;
    }

    /**
     * Returns true if the username and password match, and false otherwise.
     * @param username The username to check.
     * @param password The password to check.
     * @return True if the username and password match, and false otherwise.
     */
    public boolean checkUserCredentials(String username, String password) throws SQLException {
        if(userHashMap.containsKey(username) && password.equals(userHashMap.get(username))) {
            User matchedUser = null;
            for(int count = 0; count < userList.size(); count++) {
                if(userList.get(count).getUserName().equals(username)) {
                    matchedUser = userList.get(count);
                    permissionLevelOfUser = matchedUser.getPermissionLevel();
                    break;
                }
            }
        }
        return userHashMap.containsKey(username) && password.equals(userHashMap.get(username));
    }

    public void addNewUser(int userID, String username, String password, String permissionLevel) throws SQLException {

        PreparedStatement pstmt = (PreparedStatement) super.connection.prepareStatement("INSERT INTO UserCredentials (UserID,Username,Password,PermissionLevel) values(?,?,?,?);");
        pstmt.setString(1, Integer.toString(userID));
        pstmt.setString(2, username);
        pstmt.setString(3, password);
        pstmt.setString(4, permissionLevel);
        pstmt.executeUpdate();
    }
    private void userListToDictionary() throws SQLException {
        for (User user : userList) {
            userHashMap.put(user.getUserName(), user.getPassword());
        }
        System.out.println(userHashMap);
    }

    private void fillUsers() {
        try {
            Statement s = connection.createStatement();
            String selTable = "SELECT UserID, UserName, Password, "
                    + "PermissionLevel FROM UserCredentials;";
            s.execute(selTable);
            ResultSet rs = s.getResultSet();

            while((rs!=null) && (rs.next()))
            {
                User user = new User(
                        rs.getString("UserName"),
                        rs.getString("Password"),
                        rs.getInt("UserID"),
                        rs.getString("PermissionLevel")
                );
                permissionLevelOfUser = user.getPermissionLevel();
                this.userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public List<User> getUserList() { return userList;}
}