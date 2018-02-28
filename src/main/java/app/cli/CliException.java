package app.cli;

public class CliException extends RuntimeException {

    public CliException(String message, Throwable cause) {
        super(message, cause);
    }
}