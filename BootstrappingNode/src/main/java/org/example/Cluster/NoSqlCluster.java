package org.example.Cluster;

import org.example.BroadCast.Broadcast;
import org.example.BroadCast.Topic;
import org.example.Docker.DockerService;
import org.example.Docker.DockerServiceImp;

import java.util.PriorityQueue;

public class NoSqlCluster extends Cluster {
    private final DockerService dockerService;

    public NoSqlCluster(int numberOfNode, int initialPort, String imageName, String dockerFilePath, String networkName, String clusterWorkerNames, Broadcast broadCast,int maximaNumberOfUser) {
        super(numberOfNode, initialPort, imageName, dockerFilePath, networkName, clusterWorkerNames, broadCast,maximaNumberOfUser);
        containers = new PriorityQueue<>();
        dockerService = DockerServiceImp.getInstance();
    }

    @Override
    public void startCluster() {
        if (!isRunning) {
            dockerService.createNetwork(networkName);
            dockerService.createImage(dockerFilePath, imageName);
            for (int i = 0; i < numberOfNode; i++) {
                Container workerNode = Container.createContainer(i, initialPort++, "worker" + (i), networkName, imageName, initialPort++);
                createTheContainer(workerNode);
            }

            java.util.List<Container> workerNodes = (java.util.List<Container>) getAllNodes();
            for (Container container : workerNodes) {
                System.out.println(container);
                publishNodesInformation(container);
            }

        }
        isRunning = true;
    }

    private void startTheContainer(String containerId) {
        dockerService.startContainer(containerId);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void createTheContainer(Container workerNode) {
        dockerService.createContainer(workerNode);
        this.containers.add(workerNode);
        startTheContainer(workerNode.getContainerId());
        workerNode.setIp(dockerService.getIpaddress(workerNode.getContainerId(), workerNode.getNetworkName()));
    }

    private void publishNodesInformation(Container container) {
        java.util.List<Container> workerNodes = (java.util.List<Container>) getAllNodes();
        container.setCurrent();
        sendDataToNode(Topic.WORKER, workerNodes, container);
        container.setCurrent();
    }

    @Override
    public Container addUser() {
        if(maximumNumberOfUsers ==containers.peek().getNumberOfUsers())
            scaleUp();

        Container temp = containers.peek();
        assert containers.peek() != null;
        containers.peek().addUser();
        containers.add(containers.poll());
        return temp;
    }

    @Override
    public void restartContainer(String containerId) {
        dockerService.restartContainer(containerId);
    }

    @Override
    public void stopContainer(String containerId) {
        dockerService.stopContainer(containerId);
    }

    @Override
    public void deleteContainer(String containerId) {
        containers.remove(containerId);
        dockerService.deleteContainer(containerId);
    }

    @Override
    public void stopCluster() {
        for (Container container : containers) {
            dockerService.stopContainer(container.getContainerId());
        }
        isRunning = false;
    }

    @Override
    public void deleteTheCluster() {
        while (!containers.isEmpty()) {
            deleteContainer(containers.poll().getContainerId());
        }
        dockerService.deleteNetwork(networkName);
        dockerService.deleteImage(imageName);
        isRunning = false;
    }

    @Override
    public String getClusterStatus() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Container container : containers) {
            stringBuilder.append(container.toString());
            stringBuilder.append(dockerService.getContainerStatus(container.getContainerId()));
            stringBuilder.append("\n ----------------\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void scaleUp() {
        Container newContainer = Container.createContainer(numberOfNode, initialPort++, "worker" + (numberOfNode), networkName, imageName, initialPort++);
        numberOfNode++;
        createTheContainer(newContainer);
        publishNodesInformation(newContainer);
        java.util.List<Container> workerNodes = (java.util.List<Container>) getAllNodes();
        for (Container worker : workerNodes) {
            if (!worker.equals(newContainer)) sendDataToNode(Topic.WORKER, newContainer, worker);
        }
    }

    // TODO

    @Override
    public void scaleDown() {
    }
}
