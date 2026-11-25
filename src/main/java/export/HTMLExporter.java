package export;

import exception.DatabaseException;
import factory.Factory;
import model.Abgeordneter;
import model.Rede;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Export als HTML Seiten: Übersicht + eine Seite je Abgeordneter.
 */
public class HTMLExporter implements Exporter {

    public static final String DEFAULT_OUTPUT = "output";

    private final Factory factory;

    /** @param factory Zugriff auf die Daten */
    public HTMLExporter(Factory factory) {
        this.factory = factory;
    }

    @Override
    public boolean export(String ordner) throws DatabaseException, IOException {
        Path ziel = Path.of(ordner);
        Path index = ziel.resolve("index.html");
        if (Files.exists(index)) {
            return false; // bereits exportiert
        }
        Path seiten = ziel.resolve("abgeordnete");
        Files.createDirectories(seiten);

        // je Fraktion die Abgeordneten (von der Factory bereits nach Anzahl Reden sortiert)
        Map<String, List<Abgeordneter>> nachFraktion = new LinkedHashMap<>();
        for (String fraktion : factory.getFraktionen()) {
            nachFraktion.put(fraktion, factory.getAbgeordnete(fraktion));
        }

        // eine Seite je Abgeordneter
        for (List<Abgeordneter> liste : nachFraktion.values()) {
            for (Abgeordneter a : liste) {
                // Reden nur laden, wenn es welche gibt
                List<Rede> reden = a.getAnzahlReden() > 0 ? factory.getReden(a.getId()) : List.of();
                Files.writeString(seiten.resolve(a.getId() + ".html"), HTMLService.visitenkarte(a, reden));
            }
        }
        // Übersicht zuletzt: existiert sie, ist der Export vollständig
        Files.writeString(index, HTMLService.uebersicht(nachFraktion));
        return true;
    }
}