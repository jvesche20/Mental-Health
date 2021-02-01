package edu.ltu.dsmproject.dataaccess.dao;

/**
 * Exception indicating that a database write operation failed.
 * @see DatabaseReadException
 */
public class DatabaseWriteException extends RuntimeException {
    /**
     * Initializes a new instance of the DatabaseWriteException class.
     * @param message The error message.
     * @param cause The cause of this exception.
     */
    public DatabaseWriteException(String message, Throwable cause) {
        super("Failed to write to database: " + message, cause);
    }
}