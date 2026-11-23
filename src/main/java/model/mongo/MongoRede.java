package model.mongo;

import model.Rede;
import org.bson.Document;

/**
 * Rede aus der MongoDB
 */
public class MongoRede implements Rede {

    private final Document doc;

    /**
     * @param doc Dokument aus der Collection "reden"
     */
    public MongoRede(Document doc) {
        this.doc = doc;
    }

    @Override
    public int getAbgeordnetenId() { return doc.getInteger("abgeordneterId"); }

    @Override
    public String getDatum() { return doc.getString("datum"); }

    @Override
    public String getTop() { return doc.getString("top"); }

    @Override
    public int getWp() { return doc.getInteger("wp"); }

    @Override
    public String getContent() { return doc.getString("content"); }
}