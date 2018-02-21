package app.core.syntax.check;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReservedWordsChecker extends SyntaxChecker {

    @Override
    public void check(String query) {
        Map<String, Integer> checkedResult = new LinkedHashMap<>();
        reservedWords.forEach(s -> checkedResult.put(s, 0));
        Arrays.stream(query.split(" ")).forEach(s -> checkedResult.computeIfPresent(s, (k, v) -> v + 1));
        checkedResult.forEach((k, v) -> {
            if (v > 1) {
                throw new SyntaxParseException(String.format("must be only one reserved words : \n%s \nyou have %s : %d", reservedWords, k, v));
            }
        });
    }
}