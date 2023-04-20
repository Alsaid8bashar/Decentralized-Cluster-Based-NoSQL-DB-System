package org.example.BroadCast;

import org.example.Cluster.Container;

public interface Broadcast {
     void send(Topic topic,Object data, Container container);
}
