package app.command;

import app.cli.Cli;
import org.apache.commons.cli.CommandLine;

public class ShowCurrentDbUriCommand extends Command {

    private Cli.ConsoleCommand commandId = Cli.ConsoleCommand.CURRENT_DB;

    @Override
    public boolean isCommand(Cli.ConsoleCommand consoleCommand) {
        return commandId == consoleCommand;
    }

    @Override
    public boolean doCommand(CommandLine commandLine) {
        System.out.println(String.format("Uri to current db - mongodb://%s/%s",
                mongoClient.getAddress().toString(),
                mongoClientURI.getDatabase()));
        return false;
    }
}