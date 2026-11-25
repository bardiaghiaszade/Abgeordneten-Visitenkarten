import database.MongoDBConnector;
import exception.DatabaseException;
import exception.InvalidInputException;
import factory.Factory;
import importer.Importer;
import model.Abgeordneter;

import java.util.List;
import java.util.Scanner;

/**
 * Menü: Import und Auflistung der Abgeordneten
 */
public class Main {

    /** Pfad der Konfigurationsdatei. */
    private static final String CONFIG = "PPR_2026_060.txt";

    public static void main(String[] args) {
        try (MongoDBConnector connector = new MongoDBConnector(CONFIG);
             Scanner scanner = new Scanner(System.in)) {
            Importer importer = new Importer(connector);
            Factory factory = new Factory(connector);

            boolean laeuft = true;
            while (laeuft) {
                System.out.println("\n1 Daten importieren");
                System.out.println("2 Abgeordnete auflisten");
                System.out.println("0 Beenden");
                System.out.print("Auswahl: ");
                try {
                    switch (scanner.nextLine().trim()) {
                        case "1" -> importieren(importer, scanner);
                        case "2" -> auflisten(factory, scanner);
                        case "0" -> laeuft = false;
                        default -> throw new InvalidInputException("Ungültige Auswahl");
                    }
                } catch (InvalidInputException | DatabaseException e) {
                    // Fehler anzeigen, Menü läuft weiter
                    System.out.println("Fehler: " + e.getMessage());
                }
            }
        } catch (DatabaseException e) {
            // Konfiguration oder Verbindung fehlerhaft: Programm beenden
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    /**
     * Import; leerer Input-Path = Default-Pfad.
     *
     * @param importer Importer
     * @param scanner Eingabe
     * @throws DatabaseException bei Import- oder Datenbankfehler
     */
    private static void importieren(Importer importer, Scanner scanner) throws DatabaseException {
        System.out.print("Input-Path (leer = " + Importer.DEFAULT_INPUT + "): ");
        String pfad = scanner.nextLine().trim();
        boolean neu = pfad.isEmpty() ? importer.importDaten() : importer.importDaten(pfad);
        System.out.println(neu ? "Import fertig" : "Bereits importiert");
    }

    /**
     * Listet Abgeordnete, optional gefiltert nach Fraktion.
     *
     * @param factory Factory
     * @param scanner Eingabe
     * @throws DatabaseException     bei Datenbankfehler
     * @throws InvalidInputException bei ungültiger Fraktion
     */
    private static void auflisten(Factory factory, Scanner scanner)
            throws DatabaseException, InvalidInputException {
        List<String> fraktionen = factory.getFraktionen();
        for (int i = 0; i < fraktionen.size(); i++) {
            System.out.println((i + 1) + " " + fraktionen.get(i));
        }
        System.out.print("Fraktion (Nummer, leer = alle): ");
        String eingabe = scanner.nextLine().trim();

        String fraktion = null; // null = alle
        if (!eingabe.isEmpty()) {
            try {
                fraktion = fraktionen.get(Integer.parseInt(eingabe) - 1);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new InvalidInputException("Ungültige Fraktion: " + eingabe);
            }
        }
        for (Abgeordneter a : factory.getAbgeordnete(fraktion)) {
            System.out.println(a.getAnzahlReden() + " Reden: " + a.getVorname() + " " + a.getName()
                    + " (" + a.getPartei() + ")");
        }
    }
}