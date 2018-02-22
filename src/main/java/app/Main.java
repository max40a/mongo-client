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

    private static Map<String, String> expressionsMap = new HashMap<String, String>() {{
        put(" = ", " $eq ");
        put(" > ", " $gt ");
        put(" < ", " $lt ");
        put(" >= ", " $gte ");
        put(" <= ", " $lte ");
        put(" <> ", " $ne ");
        put(" AND ", " $and ");
        put(" OR ", " $or ");
    }};

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
                consoleService.doService(new String[]{prepareQuery(nextQuery)});
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String prepareQuery(String next) {
        for (String s : expressionsMap.keySet()) {
            if (next.contains(s)) {
                next = next.replace(s, expressionsMap.get(s));
            }
        }
        return next;
    }
}