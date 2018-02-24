package app;

import app.cli.Cli;
import app.command.*;
import app.mongo.MongoQueryPreparer;
import app.parser.Parser;
import app.service.ConsoleService;
import app.syntax.check.ReservedWordsChecker;
import app.syntax.check.SelectQueryChecker;
import app.syntax.check.SyntaxChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Cli cli = new Cli();

        List<SyntaxChecker> checkers = new ArrayList<SyntaxChecker>() {{
            add(new ReservedWordsChecker());
            add(new SelectQueryChecker());
        }};

        List<Command> commands = new ArrayList<Command>() {{
            add(new InitMongoCommand());
            add(new ShowCurrentDbUriCommand());
            add(new QueryCommand(new MongoQueryPreparer(new Parser(checkers))));
            add(new ExitCommand());
            add(new ShowHelpCommand());
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
                System.out.println(e.getMessage());
            }
        }
    }
}