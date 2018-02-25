package app.command;

public class ExitCommand extends Command {

    @Override
    public boolean doCommand(String data) {
        mongoClient.close();
        return true;
    }
}