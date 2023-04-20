package org.example.BroadCast;


import org.example.Cluster.Container;

public class TcpBroadcast implements Broadcast {
    public void send(Topic topic,Object o, Container container) {
            Client client = new Client("localhost", container.getPort() + 1);
            client.setTopic(topic);
            client.setData(o);
            client.run();
    }
}
