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
        return parseQuery(analyzePresentFields());
    }

    private void sqlSyntaxCheck(String query) {
        checkers.forEach(syntaxChecker -> syntaxChecker.validateSqlQuery(query));
    }

    private List<SqlOptionName> analyzePresentFields() {
        return Arrays.stream(SqlOptionName.values())
                .filter(f -> query.contains(f.name()))
                .collect(Collectors.toList());
    }

    private Map<String, String> parseQuery(List<SqlOptionName> options) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < options.size() - 1; i++) {
            String key = options.get(i).name();
            String till = options.get(i + 1).name();
            String value = query.substring(query.indexOf(key) + key.length(), query.indexOf(till));
            result.put(options.get(i).getPropertyName(), value.trim());
        }
        String key = options.get(options.size() - 1).name();
        String value = query.substring(query.indexOf(key) + key.length());
        result.put(options.get(options.size() - 1).getPropertyName(), value.trim());
        return result;
    }
}