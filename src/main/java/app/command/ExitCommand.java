package app.command;

public class ExitCommand extends AbstractCommand {

    @Override
    public boolean doCommand(String data) {
        mongoClient.close();
        return true;
    }
}