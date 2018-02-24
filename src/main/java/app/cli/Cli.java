package app.cli;

import org.apache.commons.cli.*;

public class Cli {

    private static final String HELP_MENU_HEADER = "=================HELP MENU==================";
    private static final String HELP_MENU_FOOTER = "============================================";

    private Options options = new Options();

    public Cli() {
        initOptions();
    }

    public CommandLine getPreparedCommandLine(String[] args) throws ParseException {
        return new DefaultParser().parse(options, args);
    }

    public void printCliHelp() {
        String commandLineSyntax = "USE : java -jar my-mongo-mongoClient.jar";
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(
                commandLineSyntax,
                HELP_MENU_HEADER,
                options,
                HELP_MENU_FOOTER
        );
    }

    private void initOptions() {
        options.addOption(Option.builder("u")
                .longOpt(ConsoleCommand.URL.notation)
                .desc("url to database")
                .hasArg(true)
                .numberOfArgs(1)
                .build());
        options.addOption(Option.builder("q")
                .longOpt(ConsoleCommand.QUERY.notation)
                .desc("sql query")
                .hasArg(true)
                .numberOfArgs(1)
                .build());
        options.addOption(Option.builder("h")
                .longOpt(ConsoleCommand.HELP.notation)
                .desc("help menu")
                .hasArg(false)
                .build());
        options.addOption(Option.builder("e")
                .longOpt(ConsoleCommand.EXIT.notation)
                .desc("exit out program")
                .build());
        options.addOption(Option.builder("c")
                .longOpt(ConsoleCommand.CURRENT_DB.notation)
                .desc("show current db url")
                .hasArg(false)
                .build());
    }

    public enum ConsoleCommand {

        URL("url"),
        QUERY("query"),
        HELP("help"),
        EXIT("exit"),
        CURRENT_DB("current-db-url");

        private String notation;

        ConsoleCommand(String notation) {
            this.notation = notation;
        }

        public String getNotation() {
            return this.notation;
        }
    }
}