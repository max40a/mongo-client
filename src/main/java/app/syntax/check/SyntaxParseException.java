package app.syntax.check;

public class SyntaxParseException extends RuntimeException {
    public SyntaxParseException(String message) {
        super(message);
    }

    public SyntaxParseException(String message, Throwable cause) {
        super(message, cause);
    }
}