package exception;

/**
 * Fehler bei Konfiguration, Verbindung oder Datenbankzugriff.
 */
public class DatabaseException extends Exception {

    /** @param message Fehlermeldung */
    public DatabaseException(String message) {
        super(message); // Exception(message)
    }

    /**
     * @param message Fehlermeldung
     * @param cause ursprünglicher Fehler
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}