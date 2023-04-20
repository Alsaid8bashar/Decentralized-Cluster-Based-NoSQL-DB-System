package org.example.Cluster;


import java.io.Serializable;
import java.util.Objects;

public class Container implements Comparable<Container>, Serializable {
    private String containerId;
    private int id;
    private String ip;
    private int port;
    private String name;
    private int numberOfUsers;
    private String networkName;
    private String imageName;
    private boolean current;
    private int tcpPort;

    private Container(int id, int port, String name, String networkName, String imageName, int tcpPort) {
        this.port = port;
        this.name = name;
        this.networkName = networkName;
        this.imageName = imageName;
        this.tcpPort = tcpPort;
        this.id = id;
        current = false;

    }

    private Container() {
    }

    public static Container createContainer(int id, int port, String name, String networkName, String imageName, int tcpPort) {
        return new Container(id, port, name, networkName, imageName, tcpPort);
    }

    public int getId() {
        return id;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent() {
        this.current = !current;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void addUser() {
        numberOfUsers++;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", numberOfUsers=" + numberOfUsers +
                "}\n";
    }


    public String toStringUserCopy() {
        return "Container{" +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Container container)) return false;
        return getContainerId().equals(container.getContainerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContainerId());
    }

    @Override
    public int compareTo(Container o) {
        if (o.getNumberOfUsers() > getNumberOfUsers()) return -1;
        else if (o.getNumberOfUsers() < getNumberOfUsers()) return 1;
        return 0;
    }
}
