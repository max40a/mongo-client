package app.command;

import app.cli.Cli;
import app.mongo.MongoQueryPreparer;
import app.mongo.MongoRequestHandler;
import app.mongo.PreparedMongoQuery;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.cli.CommandLine;

public class QueryCommand extends Command {

    private Cli.ConsoleCommand commandId = Cli.ConsoleCommand.QUERY;
    private MongoQueryPreparer preparer;

    public QueryCommand(MongoQueryPreparer preparer) {
        this.preparer = preparer;
    }

    @Override
    public boolean isCommand(Cli.ConsoleCommand consoleCommand) {
        return commandId == consoleCommand;
    }

    @Override
    public boolean doCommand(CommandLine commandLine) {
        String query = commandLine.getOptionValue(commandId.getNotation());
        PreparedMongoQuery preparedMongoQuery = preparer.prepareMongoQuery(query.trim());
        MongoDatabase database = mongoClient.getDatabase(mongoClientURI.getDatabase());
        MongoRequestHandler mongoRequestHandler = new MongoRequestHandler(database);
        mongoRequestHandler.doQuery(preparedMongoQuery).forEach(System.out::println);
        return false;
    }
}