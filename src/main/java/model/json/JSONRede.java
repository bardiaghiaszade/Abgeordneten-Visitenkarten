package model.json;

import model.Rede;
import org.bson.Document;

/**
 * Rede aus der JSON-Datei
 * Lesen Daten aus der JSON-Datei und erzeugen
 * per toDocument() Dokumente für den Import in MongoDB.
 */
public class JSONRede implements Rede {

    private final int abgeordnetenId;
    private final String datum;
    private final String top;
    private final int wp;
    private final String content;

    /**
     *
     * constructor.
     * DOCUMENT ------> JAVA OBJECT (typsichere Felder, Zugriff über Getter).
     * Erstellt Rede aus einem JSON-Objekt
     *
     * @param abgeordnetenId id des Abgeordneten
     * @param d geparstes JSON-Objekt (Dokument)
     */
    public JSONRede(int abgeordnetenId, Document d) {
        this.abgeordnetenId = abgeordnetenId;
        this.datum = d.getString("datum");
        this.top = d.getString("top");
        this.wp = d.getInteger("wp");
        this.content = d.getString("content");
    }


    /**
     * JAVA OBJECT ------> DOCUMENT (für MongoDB)
     * Dokument für die Collection "reden".
     * @return Dokument
     */
    public Document toDocument() {
        return new Document("abgeordneterId", abgeordnetenId)
                .append("datum", datum)
                .append("top", top)
                .append("wp", wp)
                .append("content", content);
    }

    @Override
    public int getAbgeordnetenId() { return abgeordnetenId; }

    @Override
    public String getDatum() { return datum; }

    @Override
    public String getTop() { return top; }

    @Override
    public int getWp() { return wp; }

    @Override
    public String getContent() { return content; }
}