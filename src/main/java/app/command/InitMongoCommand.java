package app.command;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class InitMongoCommand extends AbstractCommand {

    @Override
    public boolean doCommand(String uri) {
        mongoClientURI = new MongoClientURI(uri.trim());
        mongoClient = new MongoClient(mongoClientURI);
        return false;
    }
}