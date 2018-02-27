package app;

import app.cli.Cli;
import app.command.*;
import app.mongo.MongoQueryPreparer;
import app.parser.Parser;
import app.service.ConsoleService;
import app.syntax.check.NumberInputChecker;
import app.syntax.check.ReservedWordsChecker;
import app.syntax.check.SelectQueryChecker;
import app.syntax.check.SyntaxChecker;

import java.util.*;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Cli cli = new Cli();

        List<SyntaxChecker> checkers = new ArrayList<SyntaxChecker>() {{
            add(new ReservedWordsChecker());
            add(new SelectQueryChecker());
            add(new NumberInputChecker());
        }};

        Map<Cli.ConsoleCommand, Command> commands = new HashMap<Cli.ConsoleCommand, Command>() {{
            put(Cli.ConsoleCommand.URL, new InitMongoCommand());
            put(Cli.ConsoleCommand.QUERY, new QueryCommand(new MongoQueryPreparer(new Parser(checkers))));
            put(Cli.ConsoleCommand.HELP, new ShowHelpCommand(cli));
            put(Cli.ConsoleCommand.EXIT, new ExitCommand());
            put(Cli.ConsoleCommand.CURRENT_DB, new ShowCurrentDbUriCommand());
        }};

        ConsoleService consoleService = new ConsoleService(cli, commands);
        consoleService.doService(args);
        boolean isExit = false;
        while (!isExit) {
            try {
                System.out.print("mongo-client>");
                String nextQuery = scanner.nextLine();
                isExit = consoleService.doService(nextQuery);
            } catch (Exception e) {
                System.out.println("ERROR> " + e.getMessage());
            }
        }
    }
}