package export;

import model.Abgeordneter;
import model.Rede;

import java.util.List;
import java.util.Map;

/**
 * erzeugt HTML Text.
 */
public class HTMLService {

    private static final String KOPF = "<html>\n<head><meta charset=\"UTF-8\"></head>\n<body>\n";
    private static final String ENDE = "</body>\n</html>\n";

    /**
     * Übersicht: Abgeordnete je Fraktion, nach Anzahl Reden sortiert, mit Links zu den Visitenkarten.
     *
     * @param nachFraktion Abgeordnete je Fraktion
     * @return HTML Text
     */
    public static String uebersicht(Map<String, List<Abgeordneter>> nachFraktion) {
        StringBuilder sb = new StringBuilder(KOPF);
        sb.append("<h1>Abgeordnete</h1>\n");
        for (Map.Entry<String, List<Abgeordneter>> e : nachFraktion.entrySet()) {
            String fraktion = e.getKey().isBlank() ? "(ohne Fraktion)" : e.getKey();
            sb.append("<h2>").append(fraktion).append("</h2>\n<ul>\n");
            for (Abgeordneter a : e.getValue()) {
                sb.append("<li><a href=\"abgeordnete/").append(a.getId()).append(".html\">")
                        .append(a.getVorname()).append(" ").append(a.getName())
                        .append("</a> (").append(a.getAnzahlReden()).append(" Reden)</li>\n");
            }
            sb.append("</ul>\n");
        }
        return sb.append(ENDE).toString();
    }

    /**
     * Visitenkarte eines Abgeordneten mit seinen Reden.
     *
     * @param a Abgeordneter
     * @param reden seine Reden
     * @return HTML-Text
     */
    public static String visitenkarte(Abgeordneter a, List<Rede> reden) {
        StringBuilder sb = new StringBuilder(KOPF);
        sb.append("<a href=\"../index.html\">Übersicht</a>\n");
        sb.append("<h1>").append(a.getVorname()).append(" ").append(a.getName()).append("</h1>\n<ul>\n");
        sb.append("<li>Titel: ").append(a.getAkademischerTitel()).append("</li>\n");
        sb.append("<li>Fraktion: ").append(a.getPartei()).append("</li>\n");
        sb.append("<li>Beruf: ").append(a.getBeruf()).append("</li>\n");
        sb.append("<li>Geburtsdatum: ").append(a.getGeburtsdatum()).append("</li>\n");
        sb.append("<li>Geburtsort: ").append(a.getGeburtsort()).append("</li>\n");
        sb.append("<li>Sterbedatum: ").append(a.getSterbedatum()).append("</li>\n");
        sb.append("<li>Geschlecht: ").append(a.getGeschlecht()).append("</li>\n");
        sb.append("<li>Wahlperioden: ").append(String.join(", ", a.getPerioden())).append("</li>\n");
        sb.append("<li>Anzahl Reden: ").append(a.getAnzahlReden()).append("</li>\n");
        sb.append("</ul>\n<h2>Reden</h2>\n");
        for (Rede r : reden) {
            sb.append("<h3>WP ").append(r.getWp()).append(", ").append(r.getDatum())
                    .append(", ").append(r.getTop()).append("</h3>\n")
                    .append("<p>").append(r.getContent()).append("</p>\n");
        }
        return sb.append(ENDE).toString();
    }
}