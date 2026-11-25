package database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import exception.DatabaseException;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
// wegen index
import com.mongodb.client.model.Indexes;

/**
 * Verbindung zur MongoDB: erstellen, lesen, zählen, aggregieren.
 * Zugangsdaten kommen aus einer Konfigurationsdatei (Properties).
 * AutoCloseable ist ein Interface mit methode close.
 */
public class MongoDBConnector implements AutoCloseable {

    private final MongoClient client;
    private final MongoDatabase db;

    /**
     * Liest Konfiguration und baut die Verbindung auf.
     * Schlüssel: remote_host, remote_port, remote_database, remote_user, remote_password.
     *
     * @param configPfad Pfad der Konfigurationsdatei
     * @throws DatabaseException Datei fehlt, Schlüssel fehlt oder Port ungültig
     */
    public MongoDBConnector(String configPfad) throws DatabaseException {
        Properties p = new Properties(); //properties ist ein class um Konfigurationsdatei zu lesen (key: value)
        try (FileInputStream in = new FileInputStream(configPfad)) {
            p.load(in); // liest jede Zeile und speichert in einem Map
        } catch (IOException e) {
            throw new DatabaseException("Konfigurationsdatei nicht lesbar: " + configPfad, e);
        }

        // lese(p, key) ----> speichert Value in host/database/user/password
        String host = lese(p, "remote_host");
        String database = lese(p, "remote_database");
        String user = lese(p, "remote_user");
        String password = lese(p, "remote_password");
        int port;
        try {
            port = Integer.parseInt(lese(p, "remote_port"));
        } catch (NumberFormatException e) {
            throw new DatabaseException("Ungültiger Port in der Konfiguration", e);
        }

        // b -> b.hosts(...) ist lambda
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(b -> b.hosts(List.of(new ServerAddress(host, port))))
                .credential(MongoCredential.createCredential(user, database, password.toCharArray()))
                .build();

        this.client = MongoClients.create(settings);
        this.db = client.getDatabase(database);
    }

    /** Wert eines Schlüssels, Fehler wenn nicht vorhanden. */
    private static String lese(Properties p, String key) throws DatabaseException {
        String wert = p.getProperty(key);
        if (wert == null || wert.isBlank()) {
            throw new DatabaseException("Konfiguration unvollständig, fehlt: " + key);
        }
        return wert.trim();
    }

    /**
     * Erstellt Dokumente in einer Collection.
     *
     * @param collection Name der Collection
     * @param dokumente einzufügende Dokumente
     * @throws DatabaseException bei Datenbankfehler
     */
    public void insert(String collection, List<Document> dokumente) throws DatabaseException {
        if (dokumente.isEmpty()) {
            return;
        }
        try {
            db.getCollection(collection).insertMany(dokumente);
        } catch (MongoException e) {
            throw new DatabaseException("Einfügen in '" + collection + "' fehlgeschlagen", e);
        }
    }

    /**
     * Liest Dokumente anhand eines Filters.
     * filter befindet sich in factory
     *
     * @param collection Name der Collection
     * @param filter Filter (leeres Document = alle)
     * @return passende Dokumente
     * @throws DatabaseException bei Datenbankfehler
     */
    public List<Document> find(String collection, Bson filter) throws DatabaseException {
        try {
            return db.getCollection(collection).find(filter).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new DatabaseException("Lesen aus '" + collection + "' fehlgeschlagen", e);
        }
    }

    /**
     * zählt alle Dokumente einer Collection.
     *
     * @param collection Name der Collection
     * @return Anzahl (0, wenn Collection nicht existiert)
     * @throws DatabaseException bei Datenbankfehler
     */
    public long count(String collection) throws DatabaseException {
        try {
            return db.getCollection(collection).countDocuments();
        } catch (MongoException e) {
            throw new DatabaseException("Zählen in '" + collection + "' fehlgeschlagen", e);
        }
    }

    /**
     * führt eine Aggregation aus.
     * pipeline: Liste von Stufen, jede Stufe verarbeitet das Ergebnis der vorigen
     * pipeline befindet sich in factory
     *
     * @param collection Name der Collection
     * @param pipeline   Aggregation-Stufen
     * @return Ergebnisdokumente
     * @throws DatabaseException bei Datenbankfehler
     */
    public List<Document> aggregate(String collection, List<Bson> pipeline) throws DatabaseException {
        try {
            return db.getCollection(collection).aggregate(pipeline).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new DatabaseException("Aggregation auf '" + collection + "' fehlgeschlagen", e);
        }
    }



    /**
     * Erstellt einen Index für schnelle Suche (nichts passiert, wenn er schon existiert).
     *
     * @param collection Name der Collection
     * @param feld Feld
     * @throws DatabaseException bei Datenbankfehler
     */
    public void createIndex(String collection, String feld) throws DatabaseException {
        try {
            db.getCollection(collection).createIndex(Indexes.ascending(feld));
        } catch (MongoException e) {
            throw new DatabaseException("Index auf '" + collection + "' fehlgeschlagen", e);
        }
    }


    //Schließt die Verbindung
    @Override
    public void close() {
        client.close();
    }
}