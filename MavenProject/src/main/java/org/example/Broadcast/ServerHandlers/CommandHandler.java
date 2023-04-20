package org.example.Broadcast.ServerHandlers;

import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.Command;

public class CommandHandler extends RequestHandler {
    @Override
    public void handleRequest(String topic, Object data) {
        if (topic.equals(Topic.COMMAND.toString())) {
            receive(data);
        } else if (nextHandler != null) nextHandler.handleRequest(topic, data);
        else System.out.println("Request cannot be handled");
    }

    @Override
    public void receive(Object command) {
        if (command == null) {
            return;
        }
        System.out.println("command is :" + command);
        Command tempCommand = (Command) command;
        tempCommand.execute();

    }
}
