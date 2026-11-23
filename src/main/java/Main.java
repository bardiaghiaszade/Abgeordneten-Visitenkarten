import database.MongoDBConnector;

public class Main {
    public static void main(String[] args) throws Exception {
        try (MongoDBConnector c = new MongoDBConnector("PPR_2026_060.txt")) {
            System.out.println("abgeordnete: " + c.count("abgeordnete"));
        }
    }
}