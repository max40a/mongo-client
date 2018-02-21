package app.cli;

import app.Main;

public class Shell {

    private static String url = "mongodb://localhost:27017/testdb";

    public static void main(String[] args) throws Exception {
        Main.main(new String[]{"-u " + url});
    }
}