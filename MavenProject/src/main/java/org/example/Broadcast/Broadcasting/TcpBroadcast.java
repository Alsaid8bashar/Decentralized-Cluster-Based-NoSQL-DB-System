package org.example.Broadcast.Broadcasting;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.Cluster.Container;
import org.example.Cluster.NodeManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@JsonTypeName("TcpBroadcast")

public enum TcpBroadcast implements Broadcast {
    TCP_BROADCAST();
    private final ExecutorService pool;

    TcpBroadcast() {
        pool = Executors.newFixedThreadPool(10);
    }

    @Override
    public void sendToAllConsumers(Topic topic, Object message) {
        if (!NodeManager.NODE_MANAGER.getOtherContainers().isEmpty()) {
            for (Container container : NodeManager.NODE_MANAGER.getOtherContainers()) {
                TcpClient tcpClient = TcpClient.createTcpClient(container.getIp(), container.getPort() + 1);
                tcpClient.setData(message);
                tcpClient.setTopic(topic);
                pool.execute(tcpClient);
            }
        }
    }

    @Override
    public void sendToConsumer(Topic topic, Object message, Integer containerId) {
        Container container = NodeManager.NODE_MANAGER.getContainerById(containerId);
        TcpClient tcpClient = TcpClient.createTcpClient(container.getIp(), container.getPort() + 1);
        tcpClient.setData(message);
        tcpClient.setTopic(topic);
        pool.execute(tcpClient);
    }
}
