package app.core;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {

    private static MongoClient client;

    private MongoUtil() {
    }

    public static MongoDatabase getDatabase(String url) {
        MongoClientURI clientURI = new MongoClientURI(url);
        client = new MongoClient(clientURI);
        return client.getDatabase(clientURI.getDatabase());
    }

    public static void closeConnection() {
        client.close();
    }
}