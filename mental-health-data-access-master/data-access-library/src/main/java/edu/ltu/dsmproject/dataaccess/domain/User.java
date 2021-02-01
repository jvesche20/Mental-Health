package edu.ltu.dsmproject.dataaccess.domain;

public class User {
    private String userName;
    private String password;
    private int userID;
    private String permissionLevel;
    public User(String userName, String password, int userID, String permissionLevel) {
        this.userName = userName;
        this.password = password;
        this.userID = userID;
        this.permissionLevel = permissionLevel;
    }

    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public int getUserID() {
        return userID;
    }
    public String getPermissionLevel() {
        return permissionLevel;
    }

}
