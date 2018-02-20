package app.cli;

import app.core.Client;
import app.core.MongoUtil;
import app.core.Parser;
import com.mongodb.Block;
import org.apache.commons.cli.*;
import org.bson.Document;

import java.net.MalformedURLException;
import java.util.Arrays;

public class CliHandler {

    private static final String URL = "url";
    private static final String QUERY = "query";
    private static final String HELP = "help";
    private static final String EXIT = "exit";

    private Options options;
    private CommandLine commandLine;

    private Client client;

    public CliHandler() {
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
    }

    public void parse(String[] args) throws Exception {
        System.out.println("args = " + Arrays.toString(args));
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
        }
    }

    public void initDatabase() throws MalformedURLException {
        String urlStringNotation = commandLine.getOptionValue(URL);
        client = new Client(new Parser(), MongoUtil.getDatabase(urlStringNotation.trim()));
    }

    public void getQuery() {
        String query = commandLine.getOptionValue(QUERY);
        client.doQuery(query).forEach((Block<? super Document>) System.out::println);
    }

    public void exit() {
        MongoUtil.closeConnection();
        System.exit(0);
    }

    public void printCliHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar my-mongo-client.jar", options);
    }
}