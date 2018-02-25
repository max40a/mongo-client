package app.command;

import app.mongo.MongoQueryPreparer;
import app.mongo.MongoRequestHandler;
import app.mongo.PreparedMongoQuery;
import com.mongodb.client.MongoDatabase;

public class QueryCommand extends Command {

    private MongoQueryPreparer preparer;

    public QueryCommand(MongoQueryPreparer preparer) {
        this.preparer = preparer;
    }

    @Override
    public boolean doCommand(String query) {
        PreparedMongoQuery preparedMongoQuery = preparer.prepareMongoQuery(query.trim());
        MongoDatabase database = mongoClient.getDatabase(mongoClientURI.getDatabase());
        MongoRequestHandler mongoRequestHandler = new MongoRequestHandler(database);
        mongoRequestHandler.doQuery(preparedMongoQuery).forEach(System.out::println);
        return false;
    }
}