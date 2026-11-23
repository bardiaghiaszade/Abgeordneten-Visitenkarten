package model.json;

import model.Abgeordneter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Abgeordneter aus der JSON-Datei
 * Lesen Daten aus der JSON-Datei und erzeugen
 * per toDocument() Dokumente für den Import in MongoDB.
 */
public class JSONAbgeordneter implements Abgeordneter {

    private final int id;
    private final String vorname;
    private final String name;
    private final String akademischerTitel;
    private final String anredeTitel;
    private final String beruf;
    private final String geburtsdatum;
    private final String geburtsort;
    private final String sterbedatum;
    private final String geschlecht;
    private final String partei;

    private final List<String> perioden; //liste
    private final List<JSONRede> reden = new ArrayList<>(); //liste

    /**
     * constructor.
     * DOCUMENT ------> JAVA OBJECT (typsichere Felder, Zugriff über Getter).
     * Erstellt Abgeordneten aus einem JSON-Objekt
     *
     * @param d geparstes JSON-Objekt (Dokument)
     */
    public JSONAbgeordneter(Document d) {
        this.id = d.getInteger("id");
        this.vorname = d.getString("vorname");
        this.name = d.getString("name");
        this.akademischerTitel = d.getString("akademischer_titel");
        this.anredeTitel = d.getString("anrede_titel");
        this.beruf = d.getString("beruf");
        this.geburtsdatum = d.getString("geburtsdatum");
        this.geburtsort = d.getString("geburtsort");
        this.sterbedatum = d.getString("sterbedatum");
        this.geschlecht = d.getString("geschlecht");
        this.partei = d.getString("partei");

        // String.class weil perioden String sind
        this.perioden = d.getList("perioden", String.class);
        // Document.class weil jedes Mitglied dieser Liste selber wieder ein neues Dokument ist
        // da unsere Reden kein id für sich selbst haben, fügen wir id von Abgeordneter hinzu
        for (Document s : d.getList("speeches", Document.class)) {
            reden.add(new JSONRede(id, s));
        }
    }


    /**
     * JAVA OBJECT ------> DOCUMENT (für MongoDB)
     * Dokument für die Collection "Abgeordneter" (ohne Reden, diese kommen in eigene Collection)
     * @return Dokument
     */
    public Document toDocument() {
        return new Document("id", id)
                .append("vorname", vorname)
                .append("name", name)
                .append("akademischer_titel", akademischerTitel)
                .append("anrede_titel", anredeTitel)
                .append("beruf", beruf)
                .append("geburtsdatum", geburtsdatum)
                .append("geburtsort", geburtsort)
                .append("sterbedatum", sterbedatum)
                .append("geschlecht", geschlecht)
                .append("partei", partei)
                .append("perioden", perioden);
    }

    // Reden dieses Abgeordneten
    public List<JSONRede> getReden() { return reden; }

    @Override
    public int getId() { return id; }

    @Override
    public String getVorname() { return vorname; }

    @Override
    public String getName() { return name; }

    @Override
    public String getAkademischerTitel() { return akademischerTitel; }

    @Override
    public String getAnredeTitel() { return anredeTitel; }

    @Override
    public String getBeruf() { return beruf; }

    @Override
    public String getGeburtsdatum() { return geburtsdatum; }

    @Override
    public String getGeburtsort() { return geburtsort; }

    @Override
    public String getSterbedatum() { return sterbedatum; }

    @Override
    public String getGeschlecht() { return geschlecht; }

    @Override
    public String getPartei() { return partei; }

    @Override
    public List<String> getPerioden() { return perioden; }

    @Override
    public int getAnzahlReden() { return reden.size(); }
}