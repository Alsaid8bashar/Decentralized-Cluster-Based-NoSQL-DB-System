package org.example.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.example.Cluster.Container;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DockerServiceImp implements DockerService {
    private final static Logger logger = Logger.getLogger(DockerServiceImp.class.getName());
    private static DockerServiceImp dockerServiceImp;
    private final DockerClient dockerClient;

    private DockerServiceImp() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://localhost:2375").build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    public static DockerServiceImp getInstance() {
        if (dockerServiceImp == null) {
            dockerServiceImp = new DockerServiceImp();
        }
        return dockerServiceImp;
    }

    @Override
    public void createImage(String dockerFilePath, String imageName) {
        File dockerfile = new File(dockerFilePath);
        BuildImageCmd buildCmd = dockerClient.buildImageCmd(dockerfile).withTag(imageName);
        buildCmd.start().awaitImageId();
    }

    @Override
    public void deleteImage(String imageName) {
        java.util.List<Image> images = dockerClient.listImagesCmd().withImageNameFilter(imageName).exec();
        if (!images.isEmpty()) {
            dockerClient.removeImageCmd(images.get(0).getId()).exec();
        } else {
            logger.log(Level.SEVERE, "Image does not exist");
        }
    }

    @Override
    public void createNetwork(String networkName) {
        if (!isNetworkExist(networkName)) {
            CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd().withName(networkName).withDriver("bridge").exec();
        }
    }

    @Override
    public void deleteNetwork(String networkName) {
        java.util.List<Network> networks = dockerClient.listNetworksCmd().withNameFilter(networkName).exec();
        if (!networks.isEmpty()) {
            dockerClient.removeNetworkCmd(networks.get(0).getId()).exec();
        } else {
            logger.log(Level.SEVERE, "Network is not exist");
        }
    }

    @Override
    public void createContainer(Container node) {
        if (isContainerExistsByName(node.getName())) {
            return;
        }

        if (!isNetworkExist(node.getNetworkName())) {
            return;
        }

        ExposedPort springPort = ExposedPort.tcp(node.getPort());
        ExposedPort socketPort = ExposedPort.tcp(node.getTcpPort());
        Ports portBindings = new Ports();
        portBindings.bind(springPort, Ports.Binding.bindPort(node.getPort()));
        portBindings.bind(socketPort, Ports.Binding.bindPort(node.getTcpPort()));

        CreateContainerCmd createCmd = dockerClient.createContainerCmd(node.getImageName()).withName(node.getName()).withExposedPorts(springPort, socketPort)
                .withHostConfig(HostConfig.newHostConfig().withNetworkMode(node.getNetworkName()))
                .withPortBindings(portBindings).withEnv("server.port=" + node.getPort());

        String containerId = createCmd.exec().getId();
        node.setContainerId(containerId);
    }


    @Override
    public String getContainerStatus(String containerId) {
        if (isContainerExistsById(containerId)) {
            InspectContainerResponse nodeInfo = dockerClient.inspectContainerCmd(containerId).exec();
            return String.valueOf(nodeInfo.getState());
        } else {
            logger.log(Level.SEVERE, "Container '{}' does not exist", containerId);
            return "";
        }
    }

    @Override
    public String getIpaddress(String containerId, String networkName) {
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerId).exec();
        return inspectContainerResponse.getNetworkSettings().getNetworks().get(networkName).getIpAddress();
    }

    @Override
    public void startContainer(String containerId) {
        if (isContainerExistsById(containerId)) {
            dockerClient.startContainerCmd(containerId).exec();
        } else {
            logger.log(Level.SEVERE, "Container '{}' does not exist", containerId);
        }
    }

    @Override
    public void stopContainer(String containerId) {
        if (isContainerExistsById(containerId)) {
            dockerClient.stopContainerCmd(containerId).exec();
        } else {
            logger.log(Level.SEVERE, "Container '{}' does not exist", containerId);
        }

    }

    @Override
    public void restartContainer(String containerId) {
        if (isContainerExistsById(containerId)) {
            dockerClient.restartContainerCmd(containerId).exec();
        } else {
            logger.log(Level.SEVERE, "Container '{}' does not exist", containerId);
        }
    }


    @Override
    public void deleteContainer(String containerId) {
        if (isContainerExistsById(containerId)) {
            RemoveContainerCmd removeCmd = dockerClient.removeContainerCmd(containerId);
            removeCmd.withForce(true).exec();
        } else {
            logger.log(Level.SEVERE, "Container '{}' does not exist", containerId);
        }
    }

    private boolean isNetworkExist(String networkName) {
        java.util.List<Network> networks = dockerClient.listNetworksCmd().withNameFilter(networkName).exec();
        if (networks.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isContainerExistsByName(String containerName) {
        try {
            dockerClient.inspectContainerCmd(containerName).exec();
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private boolean isContainerExistsById(String containerId) {
        List<com.github.dockerjava.api.model.Container> containers = dockerClient
                .listContainersCmd().withIdFilter(Collections.singleton(containerId))
                .withStatusFilter(Arrays.asList("created", "restarting", "running", "paused", "exited"))
                .exec();
        return !containers.isEmpty();
    }
}
