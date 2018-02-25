package app.command;

public class ShowCurrentDbUriCommand extends AbstractCommand {

    @Override
    public boolean doCommand(String data) {
        System.out.println("URI to current db - " + mongoClientURI.getURI());
        return false;
    }
}