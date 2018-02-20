package app.cli;

import app.core.Client;
import com.mongodb.Block;
import org.apache.commons.cli.*;
import org.bson.Document;

import java.net.MalformedURLException;
import java.net.URL;

public class CliHandler {

    private static final String URL = "url";
    private static final String QUERY = "query";
    private static final String HELP = "help";
    private static final String EXIT = "exit";

    private Options options;
    private CommandLine commandLine;

    private Client client;

    public CliHandler(Client client) {
        options = new Options();
        options.addOption(Option.builder("u")
                //.required(true)
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

        this.client = client;
    }

    public void parse(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, args);
        if (commandLine.hasOption(HELP)) {
            printCliHelp();
        } else if (commandLine.hasOption(URL)) {
            getDatabaseConnectionParams();
        } else if (commandLine.hasOption(QUERY)) {
            getQuery();
        } else if (commandLine.hasOption(EXIT)) {
            exit();
        }
    }

    public void getDatabaseConnectionParams() throws MalformedURLException {
        String urlStringNotation = commandLine.getOptionValue(URL);
        URL url = new URL(urlStringNotation);
        String host = url.getHost();
        int port = url.getPort();
        String path = url.getPath();
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }
        client.initDbProperties(host, port, path);
    }

    public void getQuery() {
        String query = commandLine.getOptionValue(QUERY);
        client.doQuery(query).forEach((Block<? super Document>) System.out::println);
    }

    public void exit() {
        System.exit(0);
    }

    public void printCliHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar mongo-client-jar-with-dependencies.jar", options);
    }
}