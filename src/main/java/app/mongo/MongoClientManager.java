package app.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoClientManager {

    private MongoClient client;
    private String databaseName;

    public MongoDatabase getDatabaseByUri(String uri) {
        MongoClientURI clientURI = new MongoClientURI(uri);
        client = new MongoClient(clientURI);
        MongoDatabase database = client.getDatabase(clientURI.getDatabase());
        databaseName = database.getName();
        return database;
    }

    public void closeConnection() {
        client.close();
    }

    public String getUriToCurrentDatabase() {
        return String.format("Uri to current db - mongodb://%s/%s", client.getAddress().toString(), databaseName);
    }
}