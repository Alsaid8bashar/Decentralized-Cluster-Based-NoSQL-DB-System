package org.example.Command.CompositCommands;

import org.example.Command.Command;

public interface CompositeCommand extends Command {
    void addCommand(Command command);
}
