package edu.ltu.dsmproject.dataaccess.dao;

/**
 * Exception indicating that a database read operation failed.
 * @see DatabaseWriteException
 */
public class DatabaseReadException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of the DatabaseReadException class.
     * 
     * @param message The error message.
     * @param cause   The cause of this exception.
     */
    public DatabaseReadException(String message, Throwable cause) {
        super("Failed to read from database: " + message, cause);
    }
}
