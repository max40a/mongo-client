package app.parser;

import app.syntax.check.SyntaxChecker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {

    private String query;
    private List<SyntaxChecker> checkers;

    public Parser(List<SyntaxChecker> checkers) {
        this.checkers = checkers;
    }

    public Map<String, String> parseSqlQuery(String query) {
        sqlSyntaxCheck(query);
        this.query = query;
        return parseQuery(analyzePresentReservedWords());
    }

    private void sqlSyntaxCheck(String query) {
        checkers.forEach(syntaxChecker -> syntaxChecker.validateSqlQuery(query));
    }

    private List<SqlReservedWord> analyzePresentReservedWords() {
        return Arrays.stream(SqlReservedWord.values())
                .filter(f -> query.contains(f.getReservedWord()))
                .collect(Collectors.toList());
    }

    private Map<String, String> parseQuery(List<SqlReservedWord> reservedWords) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < reservedWords.size() - 1; i++) {
            String key = reservedWords.get(i).getReservedWord();
            String tail = reservedWords.get(i + 1).getReservedWord();
            String value = query.substring(query.indexOf(key) + key.length(), query.indexOf(tail)).trim();
            String adjective = reservedWords.get(i).getAdjective();
            result.put(adjective, value);
        }
        String key = reservedWords.get(reservedWords.size() - 1).getReservedWord();
        String value = query.substring(query.indexOf(key) + key.length()).trim();
        String adjective = reservedWords.get(reservedWords.size() - 1).getAdjective();
        result.put(adjective, value);
        return result;
    }
}