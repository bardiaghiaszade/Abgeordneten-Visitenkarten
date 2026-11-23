package model.mongo;

import model.Abgeordneter;
import org.bson.Document;

import java.util.List;

/**
 * Abgeordneter aus der MongoDB
 */
public class MongoAbgeordneter implements Abgeordneter {

    private final Document doc;

    /**
     * @param doc Dokument aus der Collection "abgeordnete"; Feld "anzahlReden"
     *            kommt aus der Aggregation (sonst 0)
     */
    public MongoAbgeordneter(Document doc) {
        this.doc = doc;
    }

    @Override
    public int getId() { return doc.getInteger("id"); }

    @Override
    public String getVorname() { return doc.getString("vorname"); }

    @Override
    public String getName() { return doc.getString("name"); }

    @Override
    public String getAkademischerTitel() { return doc.getString("akademischer_titel"); }

    @Override
    public String getAnredeTitel() { return doc.getString("anrede_titel"); }

    @Override
    public String getBeruf() { return doc.getString("beruf"); }

    @Override
    public String getGeburtsdatum() { return doc.getString("geburtsdatum"); }

    @Override
    public String getGeburtsort() { return doc.getString("geburtsort"); }

    @Override
    public String getSterbedatum() { return doc.getString("sterbedatum"); }

    @Override
    public String getGeschlecht() { return doc.getString("geschlecht"); }

    @Override
    public String getPartei() { return doc.getString("partei"); }

    @Override
    public List<String> getPerioden() { return doc.getList("perioden", String.class); }

    @Override
    public int getAnzahlReden() { return doc.getInteger("anzahlReden", 0); }
}