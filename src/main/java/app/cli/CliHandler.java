package app.cli;

import app.core.Client;
import app.core.MongoUtil;
import app.core.Parser;
import app.core.syntax.check.SyntaxChecker;
import com.mongodb.Block;
import org.apache.commons.cli.*;
import org.bson.Document;

import java.net.MalformedURLException;
import java.util.List;

public class CliHandler {

    private static final String URL = "url";
    private static final String QUERY = "query";
    private static final String HELP = "help";
    private static final String EXIT = "exit";
    private static final String CURRENT_DB_URL = "current-db-url";

    private Options options;
    private CommandLine commandLine;

    private Client client;
    private List<SyntaxChecker> syntaxCheckers;

    public CliHandler(List<SyntaxChecker> syntaxCheckers) {
        this.syntaxCheckers = syntaxCheckers;
        options = new Options();
        options.addOption(Option.builder("u")
                .longOpt(URL)
                .desc("url to database")
                .hasArg(true)
                .numberOfArgs(1)
                .build());
        options.addOption(Option.builder("q")
                .longOpt(QUERY)
                .desc("sql query")
                .hasArg(true)
                .numberOfArgs(1)
                .build());
        options.addOption(Option.builder("h")
                .longOpt(HELP)
                .desc("help menu")
                .hasArg(false)
                .build());
        options.addOption(Option.builder("e")
                .longOpt(EXIT)
                .desc("exit out program")
                .build());
        options.addOption(Option.builder("c")
                .longOpt(CURRENT_DB_URL)
                .desc("show current db url")
                .hasArg(false)
                .build());
    }

    public void parse(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, args);
        if (commandLine.hasOption(HELP)) {
            printCliHelp();
        } else if (commandLine.hasOption(URL)) {
            initDatabase();
        } else if (commandLine.hasOption(QUERY)) {
            getQuery();
        } else if (commandLine.hasOption(EXIT)) {
            exit();
        } else if (commandLine.hasOption("c")) {
            System.out.println(MongoUtil.getUrlToCurrentDatabase());
        }
    }

    public void initDatabase() throws MalformedURLException {
        String urlStringNotation = commandLine.getOptionValue(URL);
        client = new Client(new Parser(), MongoUtil.getDatabase(urlStringNotation.trim()));
    }

    public void getQuery() {
        String query = commandLine.getOptionValue(QUERY);
        syntaxCheckers.forEach(syntaxChecker -> syntaxChecker.check(query.trim()));
        client.doQuery(query).forEach((Block<? super Document>) System.out::println);
    }

    public void exit() {
        MongoUtil.closeConnection();
        System.exit(0);
    }

    public void printCliHelp() {
        String commandLineSyntax = "USE : java -jar my-mongo-client.jar";
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(
                commandLineSyntax,
                "=================HELP MENU=================",
                options,
                "============================================"
        );
    }
}