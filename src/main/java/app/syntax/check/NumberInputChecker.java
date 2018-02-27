package app.syntax.check;

import java.util.ArrayList;
import java.util.List;

public class NumberInputChecker implements SyntaxChecker {

    private List<String> numberContainsBlocks = new ArrayList<String>() {{
        add("SKIP");
        add("LIMIT");
    }};

    @Override
    public void validateSqlQuery(String query) {
        try {
            for (String block : numberContainsBlocks) {
                if (query.contains(block)) {
                    String numericContent = query.substring(query.indexOf(block), query.length())
                            .replaceAll("(SKIP|LIMIT)", "")
                            .replaceAll(" ", "");
                    Integer.parseInt(numericContent);
                }
            }
        } catch (NumberFormatException e) {
            String exceptionMessage = e.getMessage();
            String input = exceptionMessage.substring(exceptionMessage.indexOf(":"), exceptionMessage.length()).trim();
            String message = String.format("Block <SKIP> or <LIMIT> should be contain only numeric data. Your input %s", input);
            throw new SyntaxParseException(message, e);
        }
    }
}