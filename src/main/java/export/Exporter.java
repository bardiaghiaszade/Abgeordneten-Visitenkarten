package export;

import exception.DatabaseException;

import java.io.IOException;

/**
 * Export der Abgeordneten und ihrer Reden.
 */
public interface Exporter {

    /**
     * Exportiert alle Abgeordneten mit ihren Reden in den Zielordner.
     * Nichts passiert, wenn dort bereits exportiert wurde
     *
     * @param ordner Zielordner
     * @return false wenn bereits exportiert
     * @throws DatabaseException bei Datenbankfehler
     * @throws IOException bei der Fehler beim Schreiben
     */
    boolean export(String ordner) throws DatabaseException, IOException;
}