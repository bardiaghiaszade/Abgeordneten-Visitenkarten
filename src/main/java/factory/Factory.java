package factory;

import database.MongoDBConnector;
import exception.DatabaseException;
import model.Abgeordneter;
import model.Rede;
import model.mongo.MongoAbgeordneter;
import model.mongo.MongoRede;
// BSON: binäres JSON-Format von MongoDB (Document, Bson gehören dazu)
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * Zugriff auf die Daten: Selektion und Filter.
 */
public class Factory {

    public static final String ABGEORDNETE = "abgeordnete";
    public static final String REDEN = "reden";
    private final MongoDBConnector connector;

    /**
     * Legt einen Index auf "abgeordneterId" an (schnelles $lookup und Suche der Reden).
     * Index: sortiertes Verzeichnis auf "abgeordneterId"
     * ---> $lookup und Suche finden Reden direkt, ohne die ganze Collection zu lesen
     *
     * @param connector Verbindung zur MongoDB
     * @throws DatabaseException bei Datenbankfehler
     */
    public Factory(MongoDBConnector connector) throws DatabaseException {
        this.connector = connector;
        connector.createIndex(REDEN, "abgeordneterId");
    }



    /**
     * Alle Fraktionen (Feld "partei"), alphabetisch.
     * Jede Pipeline-Stufe ist selbst ein Document: {"$stufe": {Einstellungen}}
     *
     * @return Namen der Fraktionen
     * @throws DatabaseException bei Datenbankfehler
     */
    public List<String> getFraktionen() throws DatabaseException {
        List<Bson> pipeline = List.of(
                new Document("$group", new Document("_id", "$partei")), // je Partei eine Zeile
                new Document("$sort", new Document("_id", 1))); // 1 bedeutet aufsteigend
        List<String> fraktionen = new ArrayList<>();
        for (Document d : connector.aggregate(ABGEORDNETE, pipeline)) {
            fraktionen.add(d.getString("_id"));
        }
        return fraktionen;
    }



    /**
     * Abgeordnete einer Fraktion, absteigend nach Gesamtzahl der Reden.
     * Jede Pipeline-Stufe ist selbst ein Document: {"$stufe": {Einstellungen}}
     * append: weiteres Schlüssel-Wert-Paar zum Document hinzufügen
     *
     * @param fraktion Fraktion (null = alle Abgeordneten)
     * @return sortierte Abgeordnete
     * @throws DatabaseException bei Datenbankfehler
     */
    public List<Abgeordneter> getAbgeordnete(String fraktion) throws DatabaseException {
        List<Bson> pipeline = new ArrayList<>();
        if (fraktion != null) {
            pipeline.add(new Document("$match", new Document("partei", fraktion))); // Filter
        }
        // Reden dazuholen
        // append: weitere Einstellungen (localField, foreignField, as) hinzufügen
        pipeline.add(new Document("$lookup", new Document("from", REDEN)
                .append("localField", "id")
                .append("foreignField", "abgeordneterId")
                .append("as", "reden")));
        // Reden zählen
        pipeline.add(new Document("$addFields", new Document("anzahlReden", new Document("$size", "$reden"))));
        // Redenliste wieder entfernen (nur die Anzahl wird gebraucht)
        pipeline.add(new Document("$project", new Document("reden", 0)));
        // absteigend nach Anzahl, bei Gleichstand nach Name
        // append: weitere Sortierkriterien (Name, Vorname) hinzufügen
        pipeline.add(new Document("$sort", new Document("anzahlReden", -1).append("name", 1).append("vorname", 1)));

        List<Abgeordneter> liste = new ArrayList<>();
        for (Document d : connector.aggregate(ABGEORDNETE, pipeline)) {
            liste.add(new MongoAbgeordneter(d));
        }
        return liste;
    }



    /**
     * Alle Reden eines Abgeordneten.
     * Jede Pipeline-Stufe ist selbst ein Document: {"$stufe": {Einstellungen}}
     *
     * @param abgeordneterId ID des Abgeordneten
     * @return Reden
     * @throws DatabaseException bei Datenbankfehler
     */
    public List<Rede> getReden(int abgeordneterId) throws DatabaseException {
        List<Rede> liste = new ArrayList<>();
        for (Document d : connector.find(REDEN, new Document("abgeordneterId", abgeordneterId))) {
            liste.add(new MongoRede(d));
        }
        return liste;
    }
}