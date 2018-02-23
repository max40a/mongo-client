package app;

import app.cli.Cli;
import app.command.Command;
import app.command.ExitCommand;
import app.command.HelpCommand;
import app.command.SetDatabaseCommand;
import app.mongo.MongoClientManager;
import app.mongo.MongoQueryPreparer;
import app.mongo.MongoRequestHandler;
import app.parser.Parser;
import app.service.ConsoleService;
import app.syntax.check.ReservedWordsChecker;
import app.syntax.check.SelectQueryChecker;
import app.syntax.check.SyntaxChecker;

import java.util.*;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static List<SyntaxChecker> checkers = new ArrayList<SyntaxChecker>() {{
        add(new ReservedWordsChecker());
        add(new SelectQueryChecker());
    }};

    public static void main(String[] args) throws Exception {
        Cli cli = new Cli();
        MongoClientManager mongoClientManager = new MongoClientManager();
        MongoQueryPreparer queryPreparer = new MongoQueryPreparer(new Parser(checkers));

        ConsoleService consoleService = new ConsoleService(mongoClientManager, queryPreparer, cli);
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