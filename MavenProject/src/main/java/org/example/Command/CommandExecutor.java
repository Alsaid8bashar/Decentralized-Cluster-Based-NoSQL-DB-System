package org.example.Command;

public class CommandExecutor<T> {
    public T executeCommand(Command command) {
        return (T) command.execute();
    }
}
