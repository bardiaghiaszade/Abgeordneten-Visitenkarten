import database.MongoDBConnector;
import exception.DatabaseException;
import exception.InvalidInputException;
import export.Exporter;
import export.HTMLExporter;
import factory.Factory;
import importer.Importer;
import model.Abgeordneter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Menu =D
 */
public class Main {

    //Pfad der Konfigurationsdatei
    private static final String CONFIG = "PPR_2026_060.txt";

    public static void main(String[] args) {
        try (MongoDBConnector connector = new MongoDBConnector(CONFIG);
             Scanner scanner = new Scanner(System.in)) {
            Importer importer = new Importer(connector);
            Factory factory = new Factory(connector);
            Exporter exporter = new HTMLExporter(factory);

            boolean laeuft = true;
            while (laeuft) {
                System.out.println("\n1 Daten importieren");
                System.out.println("2 Abgeordnete auflisten");
                System.out.println("3 HTML exportieren");
                System.out.println("0 Beenden");
                System.out.print("Auswahl: ");
                try {
                    switch (scanner.nextLine().trim()) {
                        case "1" -> importieren(importer, scanner);
                        case "2" -> auflisten(factory, scanner);
                        case "3" -> exportieren(exporter, scanner);
                        case "0" -> laeuft = false;
                        default -> throw new InvalidInputException("Ungültige Auswahl");
                    }
                } catch (InvalidInputException | DatabaseException | IOException e) {
                    // Fehler anzeigen, Menü läuft weiter
                    System.out.println("Fehler: " + e.getMessage());
                }
            }
        } catch (DatabaseException e) {
            // Konfiguration oder Verbindung fehlerhaft: Programm beenden
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Import; leerer Input-Path = Default-Pfad.
     *
     * @param importer Importer
     * @param scanner  Eingabe
     * @throws DatabaseException     bei Import- oder Datenbankfehler
     * @throws InvalidInputException wenn die Datei nicht existiert
     */
    private static void importieren(Importer importer, Scanner scanner)
            throws DatabaseException, InvalidInputException {
        System.out.print("Input-Path (leer = " + Importer.DEFAULT_INPUT + "): ");
        String pfad = scanner.nextLine().trim().replace("\"", ""); // Anführungszeichen (Copy as path) entfernen
        if (pfad.isEmpty()) {
            pfad = Importer.DEFAULT_INPUT;
        }
        if (!new File(pfad).isFile()) {
            throw new InvalidInputException("Datei nicht gefunden: " + pfad);
        }
        System.out.println(importer.importDaten(pfad) ? "Import fertig" : "Bereits importiert");
    }


    //------------------------------------------------------------------------------------------------------------------


    /**
     * Listet Abgeordnete, optional gefiltert nach dem Namen der Fraktion (Groß-/Kleinschreibung egal).
     *
     * @param factory Factory
     * @param scanner Eingabe
     * @throws DatabaseException     bei Datenbankfehler
     * @throws InvalidInputException bei unbekannter Fraktion
     */
    private static void auflisten(Factory factory, Scanner scanner)
            throws DatabaseException, InvalidInputException {
        List<String> fraktionen = factory.getFraktionen();
        for (String f : fraktionen) {
            if (!f.isBlank()) {
                System.out.println(f);
            }
        }
        System.out.print("Fraktion (Name, leer = alle): ");
        String eingabe = scanner.nextLine().trim();

        String fraktion = null; // null = alle
        if (!eingabe.isEmpty()) {
            for (String f : fraktionen) {
                if (f.equalsIgnoreCase(eingabe)) {
                    fraktion = f;
                }
            }
            if (fraktion == null) {
                throw new InvalidInputException("Unbekannte Fraktion: " + eingabe);
            }
        }
        for (Abgeordneter a : factory.getAbgeordnete(fraktion)) {
            System.out.println(a.getAnzahlReden() + " Reden: " + a.getVorname() + " " + a.getName()
                    + " (" + a.getPartei() + ")");
        }
    }


    //------------------------------------------------------------------------------------------------------------------


    /**
     * HTML-Export; leerer Output-Path = Default-Ordner.
     *
     * @param exporter Exporter
     * @param scanner Eingabe
     * @throws DatabaseException     bei Datenbankfehler
     * @throws IOException           bei Fehler beim Schreiben
     * @throws InvalidInputException bei ungültigem Pfad oder nicht existierendem Ordner
     */
    private static void exportieren(Exporter exporter, Scanner scanner)
            throws DatabaseException, IOException, InvalidInputException {
        System.out.print("Output-Path (leer = " + HTMLExporter.DEFAULT_OUTPUT + "): ");
        String ordner = scanner.nextLine().trim().replace("\"", ""); // Anführungszeichen entfernen

        if (ordner.isEmpty()) {
            ordner = HTMLExporter.DEFAULT_OUTPUT;
        }

        File outputFolder = new File(ordner);

        if (!outputFolder.isDirectory()) {
            throw new InvalidInputException("Ordner nicht gefunden: " + ordner);
        }

        System.out.println(exporter.export(ordner) ? "Export fertig" : "Bereits exportiert");
    }

    //------------------------------------------------------------------------------------------------------------------

    
}