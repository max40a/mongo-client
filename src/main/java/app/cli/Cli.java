package app.cli;

import org.apache.commons.cli.*;

public class Cli {

    private static final String URL = "url";
    private static final String QUERY = "query";
    private static final String HELP = "help";
    private static final String EXIT = "exit";
    private static final String CURRENT_DB_URL = "current-db-url";

    private Options options = new Options();

    public Cli() {
        initOptions();
    }

    public CommandLine getPreparedCommandLine(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
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

    private void initOptions() {
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
}