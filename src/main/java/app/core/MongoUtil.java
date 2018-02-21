package app.core;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {

    private static MongoClient client;
    private static String databaseName;

    private MongoUtil() {
    }

    public static MongoDatabase getDatabase(String url) {
        MongoClientURI clientURI = new MongoClientURI(url);
        client = new MongoClient(clientURI);
        MongoDatabase database = client.getDatabase(clientURI.getDatabase());
        databaseName = database.getName();
        return database;
    }

    public static void closeConnection() {
        client.close();
    }

    public static String getUrlToCurrentDatabase() {
        return String.format("Url to current  mongodb://%s/%s", client.getAddress().toString(), databaseName);
    }
}