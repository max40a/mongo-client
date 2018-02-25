package app.command;

import app.cli.Cli;

public class ShowHelpCommand implements Command {

    private Cli cli;

    public ShowHelpCommand(Cli cli) {
        this.cli = cli;
    }

    @Override
    public boolean doCommand(String data) {
        this.cli.printCliHelp();
        return false;
    }
}