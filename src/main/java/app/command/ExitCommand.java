package app.command;

import app.cli.Cli;
import org.apache.commons.cli.CommandLine;

public class ExitCommand extends Command {

    private Cli.ConsoleCommand commandId = Cli.ConsoleCommand.EXIT;

    @Override
    public boolean isCommand(Cli.ConsoleCommand consoleCommand) {
        return this.commandId == consoleCommand;
    }

    @Override
    public boolean doCommand(CommandLine commandLine) {
        mongoClient.close();
        return true;
    }
}