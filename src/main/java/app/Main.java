package app;

import app.cli.CliHandler;
import app.core.Client;
import app.core.Parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    public static void main(String[] args) throws Exception {
        CliHandler cliHandler = new CliHandler();
        cliHandler.parse(args);
        while (true) {
            try {
                System.out.print("mongo-client>");
                String next = scanner.nextLine();
                cliHandler.parse(new String[]{handleNext(next)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String handleNext(String next) {
        for (String s : expressionsMap.keySet()) {
            if (next.contains(s)) {
                next = next.replace(s, expressionsMap.get(s));
            }
        }
        return next;
    }
}