package app.command;

import app.cli.Cli;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.cli.CommandLine;

public abstract class Command {

    protected static MongoClientURI mongoClientURI;
    protected static MongoClient mongoClient;

    abstract public boolean isCommand(Cli.ConsoleCommand consoleCommand);

    abstract public boolean doCommand(CommandLine commandLine);
}