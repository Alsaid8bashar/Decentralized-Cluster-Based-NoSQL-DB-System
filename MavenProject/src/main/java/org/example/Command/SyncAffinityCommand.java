package org.example.Command;

import org.example.Broadcast.Broadcasting.Broadcast;
import org.example.Broadcast.Broadcasting.Topic;

public class SyncAffinityCommand implements Command {
    private Command command;
    private Broadcast broadcast;
    private Topic topic;
    private Integer containerId;

    public SyncAffinityCommand(Command command, Broadcast broadcast, Topic topic, Integer containerId) {
        this.command = command;
        this.broadcast = broadcast;
        this.topic = topic;
        this.containerId = containerId;
    }

    @Override
    public Boolean execute() {
        broadcast.sendToConsumer(topic, command, containerId);
        return true;
    }

    @Override
    public String toString() {
        return "CommandWithSyncToAffinity{" +
                "command=" + command +
                ", broadcast=" + broadcast +
                ", topic=" + topic +
                ", containerId=" + containerId +
                '}';
    }
}
