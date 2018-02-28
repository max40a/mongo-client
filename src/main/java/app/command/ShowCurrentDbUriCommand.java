package app.command;

import java.util.Objects;

public class ShowCurrentDbUriCommand extends AbstractCommand {

    @Override
    public boolean doCommand(String data) {
        if (Objects.isNull(mongoClient)) {
            String message = "Database not available. You must init connection to mongo database.\n" +
                    "For example, use command \"-u mongodb://localhost:27017/test\"";
            throw new CommandException(message);
        }
        System.out.println("URI to current db - " + mongoClientURI.getURI());
        return false;
    }
}