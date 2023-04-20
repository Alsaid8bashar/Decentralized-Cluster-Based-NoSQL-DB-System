package org.example.Command.DecoratorCommands;

import org.example.Command.Command;

public abstract  class DecoratorCommand implements Command {
    protected Command command;
    public DecoratorCommand(Command command) {
        this.command = command;
    }

}
