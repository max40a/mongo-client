package app.command;

import java.util.Objects;

public class ExitCommand extends AbstractCommand {

    @Override
    public boolean doCommand(String data) {
        if (Objects.nonNull(mongoClient)) {
            mongoClient.close();
        }
        return true;
    }
}