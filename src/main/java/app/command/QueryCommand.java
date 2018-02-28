package app.command;

import app.mongo.MongoQueryPreparer;
import app.mongo.MongoRequestHandler;
import app.mongo.PreparedMongoQuery;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Objects;

public class QueryCommand extends AbstractCommand {

    private MongoQueryPreparer preparer;

    public QueryCommand(MongoQueryPreparer preparer) {
        this.preparer = preparer;
    }

    @Override
    public boolean doCommand(String query) {
        if (Objects.isNull(mongoClient)) {
            String message = "You must init connection to mongo database. \n" +
                    "For example, use command \"-u mongodb://localhost:27017/test\"";
            throw new CommandException(message);
        }
        PreparedMongoQuery preparedMongoQuery = preparer.prepareMongoQuery(query.trim());
        MongoDatabase database = mongoClient.getDatabase(mongoClientURI.getDatabase());
        MongoRequestHandler mongoRequestHandler = new MongoRequestHandler(database);
        mongoRequestHandler.doQuery(preparedMongoQuery)
                .stream()
                .map(Document::entrySet)
                .map(Objects::toString)
                .forEach(System.out::println);
        return false;
    }
}