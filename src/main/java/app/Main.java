package app;

import app.core.Client;
import app.core.Parser;
import com.mongodb.Block;
import org.bson.Document;

public class Main {

    private static String simpleQuery = "SELECT * FROM counter WHERE id >= 2 AND id <> 5 ORDER_BY title ASC";

    private static String hostName = "localhost";
    private static int port = 27017;
    private static String dataBase = "testdb";

    public static void main(String[] args) {
        Parser sqlParser = new Parser();
        Client mongoClient = new Client(sqlParser);

        mongoClient.initDbProperties(hostName, port, dataBase);
        mongoClient.doQuery(simpleQuery).forEach((Block<? super Document>) System.out::println);
    }
}