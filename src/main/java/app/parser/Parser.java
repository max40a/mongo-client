package app.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {

    public Map<String, String> parseSqlQuery(String query) {
        return parseQuery(analyzePresentFields(query), query);
    }

    private List<SqlOptionName> analyzePresentFields(String query) {
        return Arrays.stream(SqlOptionName.values())
                .filter(f -> query.contains(f.name()))
                .collect(Collectors.toList());
    }

    private Map<String, String> parseQuery(List<SqlOptionName> options, String query) {
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