package app.core;

import app.cli.Cli;
import app.core.syntax.check.SyntaxChecker;
import org.apache.commons.cli.CommandLine;

import java.net.MalformedURLException;
import java.util.List;

public class ConsoleService {

    private static final String URL = "url";
    private static final String QUERY = "query";
    private static final String HELP = "help";
    private static final String EXIT = "exit";
    private static final String CURRENT_DB_URL = "current-db-url";

    private RequestHandler requestHandler;
    private CommandLine commandLine;
    private List<SyntaxChecker> syntaxCheckers;
    private Cli cli;

    public ConsoleService(List<SyntaxChecker> syntaxCheckers, Cli cli) {
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
            getQuery();
        } else if (commandLine.hasOption(EXIT)) {
            exit();
        } else if (commandLine.hasOption(CURRENT_DB_URL)) {
            System.out.println(ConnectionManager.getUrlToCurrentDatabase());
        }
    }

    private void initDatabase() throws MalformedURLException {
        String urlStringNotation = commandLine.getOptionValue(URL);
        requestHandler = new RequestHandler(new Parser(), ConnectionManager.getDatabase(urlStringNotation.trim()));
    }

    private void getQuery() {
        String query = commandLine.getOptionValue(QUERY);
        syntaxCheckers.forEach(syntaxChecker -> syntaxChecker.check(query.trim()));
        requestHandler.doQuery(query).forEach(System.out::println);
    }

    private void exit() {
        ConnectionManager.closeConnection();
        System.exit(0);
    }
}