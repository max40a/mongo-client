package app.cli;

import app.Main;

public class Shell {

    private static String url = "mongodb://localhost:27017/testdb";

    private static String query = "SELECT * FROM counter WHERE id >= 2 AND id <> 5 ORDER_BY title ASC";
    private static String query1 = "SELECT * FROM counter WHERE title = ACID";
    private static String query2 = "SELECT * FROM counter WHERE id > 2";

    public static void main(String[] args) throws Exception {
        Main.main(new String[]{"-u " + url});
    }
}