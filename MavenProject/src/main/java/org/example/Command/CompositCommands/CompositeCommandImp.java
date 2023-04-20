package org.example.Command.CompositCommands;

import org.example.Command.Command;

import java.util.ArrayList;

public class CompositeCommandImp implements CompositeCommand {
    private  java.util.List<Command> commandList;

    public CompositeCommandImp() {
        this.commandList = new ArrayList<>();
    }

    @Override
    public Boolean execute() {
        for (Command command : commandList) {
            command.execute();
        }
        return true;
    }

    @Override
    public void addCommand(Command command) {
        commandList.add(command);
    }

    @Override
    public String toString() {
        return "CompositeCommandImp{" +
                "commandList=" + commandList +
                '}';
    }
}
