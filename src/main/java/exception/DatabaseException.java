package exception;

/**
 * Fehler bei Konfiguration, Verbindung oder Datenbankzugriff.
 */
public class DatabaseException extends Exception {

    /**
     * constructor 1.
     * Für Fehler, die wir selbst erkennen und bei denen es
     * keine andere Ursache gibt (z.B. fehlender Schlüssel in der Konfiguration).
     * Nur die Fehlermeldung wird gespeichert.
     * @param message Fehlermeldung */
    public DatabaseException(String message) {
        super(message); // Exception(message)
    }

    /**
     * constructor 2.
     * Für Fehler, bei denen wir eine andere Exception abfangen
     * (z.B. MongoException, IOException).
     * Neben der eigenen Meldung bleibt die ursprüngliche Ursache erhalten
     * und erscheint im Stacktrace.
     * @param message Fehlermeldung
     * @param cause ursprünglicher Fehler
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}