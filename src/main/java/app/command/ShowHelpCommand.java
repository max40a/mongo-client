package app.command;

import app.cli.Cli;
import org.apache.commons.cli.CommandLine;

public class ShowHelpCommand extends Command {

    @Override
    public boolean doCommand(String data) {
        new Cli().printCliHelp();
        return false;
    }
}