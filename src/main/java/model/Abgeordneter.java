package model;

import java.util.List;

/**
 * Abgeordneter (Stammdaten + Anzahl Reden)
 */
public interface Abgeordneter {

    // id
    int getId();

    // vorname
    String getVorname();

    // nachname
    String getName();

    // akademischer Titel (z.B. Dr)
    String getAkademischerTitel();

    // Anrede-Titel
    String getAnredeTitel();

    // beruf
    String getBeruf();

    // geburtsdatum (TT.MM.JJJJ)
    String getGeburtsdatum();

    // geburtsort
    String getGeburtsort();

    // Sterbedatum, leer wenn noch am leben
    String getSterbedatum();

    // Geschlecht
    String getGeschlecht();

    // partei
    String getPartei();

    // wahlperioden
    List<String> getPerioden();

    // gesamtzahl der Reden
    int getAnzahlReden();
}