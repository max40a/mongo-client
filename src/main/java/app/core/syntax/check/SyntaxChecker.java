package app.core.syntax.check;

import java.util.ArrayList;
import java.util.List;

public abstract class SyntaxChecker {

    static List<String> reservedWords = new ArrayList<String>() {{
        add("SELECT");
        add("FROM");
        add("WHERE");
        add("ORDER_BY");
        add("SKIP");
        add("LIMIT");
    }};

    public abstract void check(String query);
}