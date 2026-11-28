<Abgeordneten-Visitenkarten>

Die Abgeordneten des Deutschen Bundestages sollen in einer Webanwendung übersichtlicher
und ansprechender visualisiert und durchsuchbar gemacht werden. Hierfür ist es wichtig,
zunächst eine stabile Datenbasis sowie ein Backend zu erstellen. Die Implementierung soll
in Java unter Verwendung der NoSQL-Datenbank MongoDB erfolgen. Die geforderten

• Abgeordnete
• Reden

Da wir noch keinen Designer mit der Gestaltung der Weboberfläche beauftragt haben, erstellen
Sie bitte einen einfachen HTML-Export, damit wir uns interaktiv einen Eindruck von der
Datenlage verschaffen können. Hierzu sollen folgende Daten abgebildet werden:
Die statische HTML-Seite soll eine Übersicht über alle Abgeordneten enthalten, die nach Frak
tionen getrennt dargestellt werden. Die Sortierung soll hierbei anhand der Gesamtzahl der
Reden erfolgen. Für jeden Abgeordneten soll eine „Visitenkarte“ mit den entsprechenden De
tails erstellt werden, die zusätzlich die Reden des jeweiligen Abgeordneten darstellt


<Aufgaben>


1 Modellierung


(a) Erstellen Sie ein Klassendiagramm gemäß den geforderten Anforderungen unter Verwendung
objektorientierter Modellierung. Die Umsetzung hat in LATEX unter Verwendung des tikz-uml
Pakets1 zu erfolgen.


2 Datenbankanbindung


(a) Erstellen Sie im Package database eine Klasse namens MongoDBConnector.
• Diese Klasse soll die Kommunikation mit einer MongoDB ermöglichen. Sie soll das Erstel
len, Lesen, Zählen sowie Aggregieren von Dokumenten in der MongoDB unterstützen.
• Die Konfiguration der Verbindung soll parameterbasiert über eine Konfigurationsdatei er
folgen. Beschäftigen Sie sich hierzu mit der Klasse java.util.Properties und verwenden
Sie diese zum Einlesen der Zugangsdaten.
• Damit Sie über Java eine Verbindung zu einer MongoDB-Instanz aufbauen können, ver
wenden Sie die Maven-Dependency, die Sie auch in der POM-Datei im OLAT-Kurs finden.

(b) Erstellen Sie unter Verwendung von Interfaces Repräsentationen für die in der Aufgabenbe
schreibung benötigten Datenstrukturen sowie die dazugehörigen Implementierungen.

(c) Lesen Sie die zur Verfügung gestellten Datenstrukturen unter Verwendung der zuvor erstellten
Interfaces, jedoch mit anderen Implementierungen, ein.

(d) Importieren Sie die zuvor eingelesenen Datenstrukturen in Ihre MongoDB, wobei Sie für jede
Datenstruktur eine eigene Collection erstellen. Stellen Sie sicher, dass die Daten nicht doppelt
eingelesen werden.

(e) Der Zugriff auf die Datenstrukturen erfolgt über eine Factory.

(f) Erstellen Sie ebenfalls über die Factory Selektions- und Filtermethoden für die Umsetzung
von Aufgabe 3. Die Selektion sowie die Aufbereitung der Daten hat ausschließlich über die
NoSQL-Datenbank zu erfolgen.

(g) Implementieren Sie ein einfaches Menü zur Auflistung der Abgeordneten. Definieren Sie darüber
hinaus eigene Exceptions, um fehlerhafte Eingaben und auftretende Fehler geeignet behandeln zu können.



3 HTML-Serialisierung


(a) Exportieren Sie unter Verwendung eines Interfaces alle in der Aufgabenbeschreibung definierten
Datenstrukturen in eine statische HTML-Seite, die der interaktiven Exploration der Daten
dient. Ob Sie eine einzige HTML-Seite erstellen oder für jeden Abgeordneten eine eigene Seite
erzeugen und diese über eine Übersichtsseite verlinken, bleibt Ihnen überlassen.

(b) Verwenden Sie hierfür Service-Funktionen, die auf Klassenmethoden basieren