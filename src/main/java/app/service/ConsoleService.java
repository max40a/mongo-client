package app.service;

import app.cli.Cli;
import app.command.Command;
import app.mongo.MongoCharacterAdapter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.List;

public class ConsoleService {

    private Cli cli;
    private List<Command> commands;

    public ConsoleService(Cli cli, List<Command> commands) {
        this.cli = cli;
        this.commands = commands;
    }

    public boolean doService(String... args) throws ParseException {
        for (int i = 0; i < args.length; i++) {
            args[i] = MongoCharacterAdapter.convertCharacters(args[i]);
        }
        CommandLine preparedCommandLine = cli.getPreparedCommandLine(args);
        for (Command command : commands) {
            for (Cli.ConsoleCommand consoleCommand : Cli.ConsoleCommand.values()) {
                if (preparedCommandLine.hasOption(consoleCommand.getNotation()) && command.isCommand(consoleCommand)) {
                    return command.doCommand(preparedCommandLine);
                }
            }
        }
        return false;
    }
}