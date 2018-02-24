package app.command;

import app.cli.Cli;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.cli.CommandLine;

public class InitMongoCommand extends Command {

    private Cli.ConsoleCommand commandId = Cli.ConsoleCommand.URL;

    @Override
    public boolean isCommand(Cli.ConsoleCommand consoleCommand) {
        return this.commandId == consoleCommand;
    }

    @Override
    public boolean doCommand(CommandLine commandLine) {
        String url = commandLine.getOptionValue(commandId.getNotation());
        mongoClientURI = new MongoClientURI(url.trim());
        mongoClient = new MongoClient(mongoClientURI);
        return false;
    }
}