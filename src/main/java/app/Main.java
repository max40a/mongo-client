package app;

import app.cli.CliHandler;
import app.core.Client;
import app.core.Parser;

import java.util.Scanner;

public class Main {

    //http://localhost:27017/testdb

    private static Scanner scanner = new Scanner(System.in);
    private static String query = "SELECT * FROM counter WHERE id >= 2 AND id <> 5 ORDER_BY title ASC";

    private static String hostName = "localhost";
    private static int port = 27017;
    private static String dataBase = "testdb";

    public static void main(String[] args) throws Exception {
        Parser sqlParser = new Parser();
        Client mongoClient = new Client(sqlParser);
        CliHandler cliHandler = new CliHandler(mongoClient);
        cliHandler.parse(args);
        while (true) {
            try {

                System.out.print("query>");
                String next = scanner.nextLine();
                cliHandler.parse(new String[]{next});

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*mongoClient.initDbProperties(hostName, port, dataBase);
        mongoClient.doQuery(query).forEach((Block<? super Document>) System.out::println);*/
    }
}