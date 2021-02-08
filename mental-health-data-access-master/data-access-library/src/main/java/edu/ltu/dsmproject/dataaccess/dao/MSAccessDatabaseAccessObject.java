package edu.ltu.dsmproject.dataaccess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class MSAccessDatabaseAccessObject {
    private static final String JDBC_UCANACCESS_URL = "jdbc:ucanaccess://";

    private static String msAccessDatabasesFolder;

    private String msAccessDatabaseFilename;
    private String absoluteDatabaseFilepath;

    // The connection remains open so that the db can be queried without having to reconnect.
    protected Connection connection;

    protected MSAccessDatabaseAccessObject(String msAccessDatabaseFilename) {
        msAccessDatabasesFolder = findDatabasesFolder();
        setMsAccessDatabaseFilename(msAccessDatabaseFilename);
        initializeDatabaseConnection();
    }

    private void initializeDatabaseConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("Attempting database connection to " + absoluteDatabaseFilepath + " ...");
            connection = DriverManager.getConnection(absoluteDatabaseFilepath);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to connect to database: " + e);
        }
    }

    private void setMsAccessDatabaseFilename(String msAccessDatabaseFilename) {
        this.msAccessDatabaseFilename = msAccessDatabaseFilename;
        absoluteDatabaseFilepath = JDBC_UCANACCESS_URL + msAccessDatabasesFolder + msAccessDatabaseFilename;
    }

    private static String findDatabasesFolder() {
        return System.getProperty("user.dir") + "/../db/";
    }
}
