package importer;

import database.MongoDBConnector;
import exception.DatabaseException;
import factory.Factory;
import model.json.JSONAbgeordneter;
import model.json.JSONRede;
// BSON: binäres JSON-Format von MongoDB (Document, JsonReader, BsonType gehören dazu)
import org.bson.BsonType;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.json.JsonReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Import: JSON-Datei einlesen und in MongoDB schreiben.
 */
public class Importer {

    /** Default-Pfad der Importdatei (liegt im Projektordner). */
    public static final String DEFAULT_INPUT = "mdb_speeches.json";

    private final MongoDBConnector connector;

    /** @param connector Verbindung zur MongoDB */
    public Importer(MongoDBConnector connector) {
        this.connector = connector;
    }

    /**
     * Liest die JSON-Datei und füllt die Collections "abgeordnete" und "reden".
     * Nichts passiert, wenn schon Daten vorhanden sind (kein doppelter Import).
     *
     * @param pfad Pfad der JSON-Datei
     * @return false, wenn bereits importiert
     * @throws DatabaseException Datei nicht lesbar oder Datenbankfehler
     */
    public boolean importDaten(String pfad) throws DatabaseException {
        if (connector.count(Factory.ABGEORDNETE) > 0 || connector.count(Factory.REDEN) > 0) {
            return false;
        }
        List<Document> abgeordnete = new ArrayList<>();
        List<Document> reden = new ArrayList<>();

        try (Reader in = new InputStreamReader(new FileInputStream(pfad), StandardCharsets.UTF_8)) {
            // JSON-Text ---> Document, Element für Element
            JsonReader reader = new JsonReader(in); // liest JSON-Text Stück für Stück
            DocumentCodec codec = new DocumentCodec(); // JSON-Objekt -> Document
            reader.readStartArray(); // Beginn des Arrays "["
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) { // solange ein weiteres Element existiert (nicht "]")
                Document d = codec.decode(reader, DecoderContext.builder().build()); // ein Abgeordneter
                JSONAbgeordneter a = new JSONAbgeordneter(d);
                abgeordnete.add(a.toDocument());
                for (JSONRede r : a.getReden()) {
                    reden.add(r.toDocument());
                }
            }
            reader.readEndArray();
        } catch (IOException | RuntimeException e) {
            throw new DatabaseException("Importdatei nicht lesbar oder fehlerhaft: " + pfad, e);
        }
        // alle gesammelten Dokumente in die jeweilige Collection schreiben
        connector.insert(Factory.ABGEORDNETE, abgeordnete);
        connector.insert(Factory.REDEN, reden);
        return true;
    }
}