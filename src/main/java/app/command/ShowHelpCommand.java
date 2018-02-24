package app.command;

import app.cli.Cli;
import org.apache.commons.cli.CommandLine;

public class ShowHelpCommand extends Command {

    private Cli.ConsoleCommand commandId = Cli.ConsoleCommand.HELP;

    @Override
    public boolean isCommand(Cli.ConsoleCommand consoleCommand) {
        return this.commandId == consoleCommand;
    }

    @Override
    public boolean doCommand(CommandLine commandLine) {
        new Cli().printCliHelp();
        return false;
    }
}