package org.example.Docker;

import org.example.Cluster.Container;

public interface DockerService {

    /**
     * Creates a Docker image from the specified Dockerfile and assigns the specified name to the image.
     *
     * @param dockerFilePath the path to the Dockerfile
     * @param imageName      the name to be assigned to the new image
     */
    void createImage(String dockerFilePath, String imageName);

    /**
     * Deletes the Docker image with the specified name.
     *
     * @param imageName the name of the image to delete
     */
    public void deleteImage(String imageName);

    /**
     * Creates a new Docker network with the specified name.
     *
     * @param networkName the name to be assigned to the new network
     */
    void createNetwork(String networkName);

    /**
     * Deletes the Docker network with the specified ID.
     *
     * @param networkName the ID of the network to delete
     */
    public void deleteNetwork(String networkName);


    /**
     * Creates a new Docker container based on the provided Container object.
     *
     * @param node the Container object representing the new container
     */
    void createContainer(Container node);


    /**
     * Starts the Docker container with the specified ID.
     *
     * @param containerId the ID of the container to be started
     */
    void startContainer(String containerId);

    /**
     * Stops the Docker container with the specified ID.
     *
     * @param containerId the ID of the container to be stopped
     */
    void stopContainer(String containerId);

    /**
     * Restarts the Docker container with the specified ID.
     *
     * @param containerId the ID of the container to be restarted
     */

    void restartContainer(String containerId);

    /**
     * Deletes the Docker container with the specified ID.
     *
     * @param containerId the ID of the container to delete
     */
    public void deleteContainer(String containerId);


    /**
     * Gets the status of the Docker container with the specified ID.
     *
     * @param containerId the ID of the container to query
     * @return a string representing the status of the container
     */

    String getContainerStatus(String containerId);

    /**
     * Retrieves the IP address of a Docker container with the specified ID that is connected to the specified network.
     *
     * @param containerId the ID of the container for which to retrieve the IP address
     * @param networkName the name of the network to which the container is connected
     * @return the IP address of the container on the specified network
     */
    String getIpaddress(String containerId, String networkName);
}
