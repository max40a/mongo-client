package app.command;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public abstract class Command {

    protected static MongoClientURI mongoClientURI;
    protected static MongoClient mongoClient;

    abstract public boolean doCommand(String data);
}