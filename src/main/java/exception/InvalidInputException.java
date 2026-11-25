package exception;

/**
 * Fehlerhafte Eingabe (z.B. ungültige Menüauswahl).
 */
public class InvalidInputException extends Exception {

    /** @param message Fehlermeldung */
    public InvalidInputException(String message) {
        super(message);
    }
}