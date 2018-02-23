package app.syntax.check;

public class SelectQueryChecker extends SyntaxChecker {

    @Override
    public void validateSqlQuery(String query) {
        if (!query.startsWith("SELECT")) {
            throw new SyntaxParseException("Support only SELECT query.Query must begin: SELECT <projections>");
        }
        if (!query.contains("FROM")) {
            throw new SyntaxParseException("Query must be consist: FROM <target>");
        }
    }
}