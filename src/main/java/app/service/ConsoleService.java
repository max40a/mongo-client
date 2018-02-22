package app.service;

import app.cli.Cli;
import app.mongo.MongoQueryPreparer;
import app.parser.Parser;
import app.syntax.check.SyntaxChecker;
import app.mongo.MongoClientManager;
import app.mongo.MongoRequestHandler;
import org.apache.commons.cli.CommandLine;

import java.net.MalformedURLException;
import java.util.List;

public class ConsoleService {

    private static final String URL = "url";
    private static final String QUERY = "query";
    private static final String HELP = "help";
    private static final String EXIT = "exit";
    private static final String CURRENT_DB_URL = "current-db-url";

    private MongoClientManager mongoClientManager;
    private MongoRequestHandler mongoRequestHandler;
    private CommandLine commandLine;
    private List<SyntaxChecker> syntaxCheckers;
    private Cli cli;

    public ConsoleService(MongoClientManager mongoClientManager,
                          List<SyntaxChecker> syntaxCheckers,
                          Cli cli) {
        this.mongoClientManager = mongoClientManager;
        this.syntaxCheckers = syntaxCheckers;
        this.cli = cli;
    }

    public void doService(String[] args) throws Exception {
        commandLine = cli.getPreparedCommandLine(args);
        if (commandLine.hasOption(HELP)) {
            cli.printCliHelp();
        } else if (commandLine.hasOption(URL)) {
            initDatabase();
        } else if (commandLine.hasOption(QUERY)) {
            processQuery();
        } else if (commandLine.hasOption(EXIT)) {
            exit();
        } else if (commandLine.hasOption(CURRENT_DB_URL)) {
            System.out.println(mongoClientManager.getUriToCurrentDatabase());
        }
    }

    private void initDatabase() throws MalformedURLException {
        String urlStringNotation = commandLine.getOptionValue(URL);
        mongoRequestHandler = new MongoRequestHandler(mongoClientManager.getDatabaseByUri(urlStringNotation.trim()));
    }

    private void processQuery() {
        String query = commandLine.getOptionValue(QUERY);
        //validate SQL
        syntaxCheckers.forEach(syntaxChecker -> syntaxChecker.validateSqlQuery(query.trim()));

        MongoQueryPreparer preparer = new MongoQueryPreparer(new Parser());
        mongoRequestHandler.doQuery(preparer.preparedMongoQuery(query)).forEach(System.out::println);
    }

    private void exit() {
        mongoClientManager.closeConnection();
        System.exit(0);
    }
}