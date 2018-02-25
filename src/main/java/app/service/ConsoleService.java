package app.service;

import app.cli.Cli;
import app.command.Command;
import app.mongo.MongoCharacterAdapter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConsoleService {

    private Cli cli;
    private Map<Cli.ConsoleCommand, Command> commands;

    public ConsoleService(Cli cli, Map<Cli.ConsoleCommand, Command> commands) {
        this.cli = cli;
        this.commands = commands;
    }

    public boolean doService(String... args) throws ParseException {
        for (int i = 0; i < args.length; i++) {
            args[i] = MongoCharacterAdapter.convertCharacters(args[i]);
        }
        CommandLine preparedCommandLine = cli.getPreparedCommandLine(args);
        Cli.ConsoleCommand[] consoleCommands = Cli.ConsoleCommand.values();
        for (Cli.ConsoleCommand command : consoleCommands) {
            if (preparedCommandLine.hasOption(command.getNotation())) {
                String optionValue = preparedCommandLine.getOptionValue(command.getNotation());
                return commands.get(command).doCommand(optionValue);
            }
        }
        return false;
    }
}