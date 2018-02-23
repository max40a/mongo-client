package app.service;

import app.cli.Cli;
import app.command.Command;
import app.mongo.MongoCharacterAdapter;
import app.mongo.MongoClientManager;
import app.mongo.MongoQueryPreparer;
import app.mongo.MongoRequestHandler;
import app.mongo.PreparedMongoQuery;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.net.MalformedURLException;
import java.util.List;

import static app.cli.Cli.ConsoleCommand.*;

public class ConsoleService {

    private MongoClientManager mongoClientManager;
    private MongoQueryPreparer preparer;
    private MongoRequestHandler mongoRequestHandler;
    private CommandLine commandLine;
    private Cli cli;

    public ConsoleService(MongoClientManager mongoClientManager,
                          MongoQueryPreparer preparer,
                          Cli cli) {
        this.mongoClientManager = mongoClientManager;
        this.preparer = preparer;
        this.cli = cli;
    }

    public boolean doService(String... args) throws ParseException {
        for (int i = 0; i < args.length; i++) {
            args[i] = MongoCharacterAdapter.convertCharacters(args[i]);
        }
        commandLine = cli.getPreparedCommandLine(args);
        if (commandLine.hasOption(HELP.getNotation())) {
            cli.printCliHelp();
            return false;
        } else if (commandLine.hasOption(URL.getNotation())) {
            setDatabase();
            return false;
        } else if (commandLine.hasOption(QUERY.getNotation())) {
            processQuery();
            return false;
        } else if (commandLine.hasOption(CURRENT_DB.getNotation())) {
            System.out.println(mongoClientManager.getUriToCurrentDatabase());
            return false;
        } else if (commandLine.hasOption(EXIT.getNotation())) {
            mongoClientManager.closeConnection();
            return true;
        }
        return false;
    }

    private void setDatabase() {
        String urlStringNotation = commandLine.getOptionValue(URL.getNotation());
        mongoRequestHandler = new MongoRequestHandler(mongoClientManager.getDatabaseByUri(urlStringNotation.trim()));
    }

    private void processQuery() {
        String query = commandLine.getOptionValue(QUERY.getNotation());
        PreparedMongoQuery preparedQuery = preparer.preparedMongoQuery(query.trim());
        mongoRequestHandler.doQuery(preparedQuery).forEach(System.out::println);
    }
}