package org.example.Cluster;

import java.util.Scanner;

public class ClusterCLI {
    private final ClusterManger clusterManger;

    public ClusterCLI(ClusterManger clusterManger) {
        this.clusterManger = clusterManger;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Enter a command (type 'help' for a list of available commands):");
            String command = scanner.nextLine();
            switch (command) {
                case "help":
                    printHelp();
                    break;
                case "start":
                    clusterManger.startTheCluster();
                    break;
                case "register":
                    System.out.println("Enter the first username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter the password:");
                    String password = scanner.nextLine();
                    System.out.println(clusterManger.registration(username, password));
                    break;
                case "get-nodes":
                    System.out.println(clusterManger.getAllClustersNodes());
                    break;
                case "scale-up":
                    clusterManger.scaleUp();
                    break;
                case "get-status":
                    System.out.println(clusterManger.getClusterStatue());
                    break;
                case "stop":
                    clusterManger.stopTheCluster();
                    break;
                case "delete":
                    clusterManger.deleteTheCluster();
                    break;
                case "restart":
                    System.out.println("Enter the node ID to restart:");
                    String nodeId = scanner.nextLine();
                    clusterManger.restartNode(nodeId);
                    break;
                case "stop-node":
                    System.out.println("Enter the node ID to stop:");
                    nodeId = scanner.nextLine();
                    clusterManger.stopNode(nodeId);
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' for a list of available commands.");
                    break;
            }
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("help: display this help message");
        System.out.println("start: start the cluster");
        System.out.println("register: register a new user");
        System.out.println("get-nodes: get all nodes in the cluster");
        System.out.println("get-status: get the status of the cluster");
        System.out.println("scale-up: add new container to the cluster");
        System.out.println("stop: stop the cluster");
        System.out.println("delete: delete the cluster");
        System.out.println("restart: restart a node");
        System.out.println("stop-node: stop a node");
        System.out.println("exit: exit the program");
    }
}