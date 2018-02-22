package app.syntax.check;

public class SyntaxParseException extends RuntimeException {
    public SyntaxParseException(String message) {
        super(message);
    }
}