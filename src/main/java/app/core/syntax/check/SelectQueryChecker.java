package app.core.syntax.check;

public class SelectQueryChecker extends SyntaxChecker {

    @Override
    public void check(String query) {
        if (!query.startsWith("SELECT")) {
            throw new SyntaxParseException("support only select query, query must begin with SELECT <projections>");
        }
        if (!query.contains("FROM")) {
            throw new SyntaxParseException("query must be consist FROM <target>");
        }
    }
}