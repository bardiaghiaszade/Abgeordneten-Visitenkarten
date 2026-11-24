import database.MongoDBConnector;
import factory.Factory;

public class Main {
    public static void main(String[] args) throws Exception {
        try (MongoDBConnector c = new MongoDBConnector("PPR_2026_060.txt")) {
            Factory f = new Factory(c);
            System.out.println(f.importDaten() ? "Import fertig" : "Bereits importiert");
            System.out.println("abgeordnete: " + c.count("abgeordnete"));
            System.out.println("reden: " + c.count("reden"));
        }
    }
}