package org.example.Broadcast.ServerHandlers;

import org.example.Broadcast.Broadcasting.Topic;
import org.example.Cluster.Container;
import org.example.Cluster.NodeManager;
import org.example.Command.FilesCommand.InsertDataCommand;
import org.example.Files.FilesInfo;

import java.util.List;

public class ContainerHandler extends RequestHandler {
    @Override
    public void handleRequest(String topic, Object data) {
        if (topic.equals(Topic.WORKER.toString())) {
            receive(data);
        } else if (nextHandler != null) nextHandler.handleRequest(topic, data);
        else System.out.println("Request cannot be handled");
    }

    @Override
    public void receive(Object nodes) {
        if (nodes instanceof List<?>) {
            List<Container> containers = (List<Container>) nodes;
            for (Container node : containers) {
                saveContainer(node);
            }
        } else {
            Container node = (Container) nodes;
            saveContainer(node);
        }
    }

    private void saveContainer(Container container) {
        System.out.println(container.toString());
        new InsertDataCommand(FilesInfo.nodesPath(container.getContainerId()), container).execute();
        NodeManager.NODE_MANAGER.addContainer(container);
    }
}