package app.command;

public class ShowCurrentDbUriCommand extends Command {

    @Override
    public boolean doCommand(String data) {
        System.out.println(String.format("Uri to current db - mongodb://%s/%s",
                mongoClient.getAddress().toString(),
                mongoClientURI.getDatabase()));
        return false;
    }
}