package org.example.Cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum NodeManager {
    NODE_MANAGER;
    private final Map<Integer, Container> otherContainers;
    private Container thisContainer;

    NodeManager() {
        otherContainers = new HashMap<>();
    }

    public void addContainer(Container container) {
        if (!container.isCurrent()) {
            otherContainers.put(container.getId(), container);
        } else {
            thisContainer = container;
            AffinityLoadBalancer.LOAD_BALANCER.setNextAffinity(container.getId());
        }
        AffinityLoadBalancer.LOAD_BALANCER.addServer(container.getId());

    }

    public Container getThisContainer() {
        if (thisContainer == null) {
            throw new IllegalArgumentException();
        }
        return thisContainer;
    }

    public Collection<Container> getOtherContainers() {
        if (otherContainers == null) return new ArrayList<>();
        return otherContainers.values();
    }

    public Container getContainerById(int id) {
        if (otherContainers == null || !otherContainers.containsKey(id)) throw new IllegalArgumentException();
        return otherContainers.get(id);
    }
}
