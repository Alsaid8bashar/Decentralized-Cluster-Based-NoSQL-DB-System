package org.example.Cluster;

import java.util.ArrayList;

public enum AffinityLoadBalancer {
    LOAD_BALANCER;
    private final java.util.List<Integer> serverList;
    private volatile Integer nextAffinity;

    AffinityLoadBalancer() {
        serverList = new ArrayList<>();
    }

    public void addServer(int id) {
        serverList.add(id);
    }

    public void setNextAffinity(int id) {
        nextAffinity = id;

    }

    public Integer getNextServer() {
        System.out.println(serverList.size());
        Integer server = serverList.get(nextAffinity);
        synchronized (AffinityLoadBalancer.class) {
            nextAffinity = (nextAffinity + 1) % serverList.size();
        }
        System.out.println(server);
        return server;
    }

}


