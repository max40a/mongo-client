package app.syntax.check;

import java.util.*;

public class ReservedWordsChecker implements SyntaxChecker {

    private List<String> reservedWords = new ArrayList<String>() {{
        add("SELECT");
        add("FROM");
        add("WHERE");
        add("ORDER_BY");
        add("SKIP");
        add("LIMIT");
    }};

    @Override
    public void validateSqlQuery(String query) {
        Map<String, Integer> checkedResult = new LinkedHashMap<>();
        reservedWords.forEach(s -> checkedResult.put(s, 0));
        Arrays.stream(query.split(" ")).forEach(s -> checkedResult.computeIfPresent(s, (k, v) -> v + 1));
        checkedResult.forEach((k, v) -> {
            if (v > 1) {
                throw new SyntaxParseException(String.format("Must be only one reserved words : %s you have %s : %d", reservedWords, k, v));
            }
        });
    }
}