package org.example.Cluster;

import org.example.BroadCast.Broadcast;
import org.example.BroadCast.Topic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Cluster {
    private static final AtomicLong idGenerator = new AtomicLong(1);
    protected  int numberOfNode;
    protected  int initialPort;
    protected final String imageName;
    protected final String dockerFilePath;
    protected final String networkName;
    protected final String clusterWorkerNames;
    protected final long id;
    protected Broadcast tcpBroadcast;
    protected Queue<Container> containers;
    protected boolean isRunning;
    protected int maximumNumberOfUsers;


    public Cluster(int numberOfNode, int initialPort, String imageName, String dockerFilePath, String networkName, String clusterWorkerNames, Broadcast tcpBroadcast,int maximumNumberOfUsers) {
        this.numberOfNode = numberOfNode;
        this.initialPort = initialPort;
        this.imageName = imageName;
        this.dockerFilePath = dockerFilePath;
        this.networkName = networkName;
        this.clusterWorkerNames = clusterWorkerNames;
        this.tcpBroadcast = tcpBroadcast;
        this.maximumNumberOfUsers = maximumNumberOfUsers;
        id = idGenerator.getAndIncrement();

        isRunning=false;
    }


    public abstract void startCluster();

    /**
     * Stops the Docker containers that make up the cluster.
     */

    public abstract void stopCluster();

    /**
     * Deletes all the information associated with the Docker cluster, including all containers, images and networks.
     */

    public abstract void deleteTheCluster();



    /**
     * Increases the number of containers in the cluster.
     */

    public abstract void scaleUp();

    /**
     * Decreases the number of containers in the cluster.
     */
    public abstract void scaleDown();

    /**
     * Gets a collection of all nodes in the cluster.
     *
     * @return a collection of all nodes in the cluster
     */
    public Collection getAllNodes() {
        return Arrays.asList(containers.toArray());
    }

    /**
     * Gets the current status of the Docker containers that make up the cluster.
     *
     * @return a string representing the current status of the cluster
     */
    public abstract String getClusterStatus();

    /**
     * Adds a new user to the cluster.
     *
     * @return the new user's container
     */
    public abstract Container addUser();

    /**
     * Sends data to a specific node in the cluster.
     *
     * @param topic     the topic to send the data to
     * @param o         the data to send
     * @param container the container to send the data to
     */

    public void sendDataToNode(Topic topic, Object o, Container container) {
        tcpBroadcast.send(topic, o, container);
    }

    public abstract void restartContainer(String containerId);

    public abstract void deleteContainer(String containerId);

    public abstract void stopContainer(String containerId);

    /**
     * Gets the ID of the cluster.
     *
     * @return the ID of the cluster
     */

    public long getId() {
        return id;
    }

}
