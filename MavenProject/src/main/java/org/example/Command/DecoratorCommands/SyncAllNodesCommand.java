package org.example.Command.DecoratorCommands;

import org.example.Broadcast.Broadcasting.Broadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.Command;

public class SyncAllNodesCommand extends DecoratorCommand {
    private final Broadcast broadcast;
    private final Topic topic;

    public SyncAllNodesCommand(Command command, Broadcast broadcast, Topic topic) {
        super(command);
        this.broadcast = broadcast;
        this.topic = topic;
    }

    @Override
    public Boolean execute() {
        command.execute();
        broadcast.sendToAllConsumers(topic, command);
        return true;
    }

    @Override
    public String toString() {
        return "CommandWithSyncToAllNodes{" +
                "broadcast=" + broadcast +
                ", topic=" + topic +
                ", command=" + this.command +
                '}';
    }
}
