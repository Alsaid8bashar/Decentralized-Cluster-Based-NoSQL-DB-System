package org.example.Model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class User implements Serializable {
    private static final AtomicLong idGenerator = new AtomicLong(1);
    private  String token;
    private  long id;
    private String nodeID;

    public User(String token, long id) {
        this.token = token;
        this.id = id;
    }


    public User() {
    }

    public static long getTheNextId() {
        return idGenerator.getAndIncrement();

    }

    public String getToken() {
        return token;
    }


    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public long getId() {
        return id;
    }
}
