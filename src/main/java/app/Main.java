package app;

import app.cli.Cli;
import app.mongo.MongoClientManager;
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
        ConsoleService consoleService = new ConsoleService(mongoClientManager, checkers, cli);
        consoleService.doService(args);
        while (true) {
            try {
                System.out.print("mongo-client>");
                String nextQuery = scanner.nextLine();
                consoleService.doService(new String[]{nextQuery});
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}