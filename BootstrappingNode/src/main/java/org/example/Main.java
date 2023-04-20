package org.example;

import org.example.BroadCast.TcpBroadcast;
import org.example.Cluster.Cluster;
import org.example.Cluster.ClusterCLI;
import org.example.Cluster.ClusterManger;
import org.example.Cluster.NoSqlCluster;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Cluster cluster = new NoSqlCluster(4, 8081, "workers-image",
                Paths.get("CapstoneSpringProject/DockerFile").toString(),
                "cluster-network", "worker", new TcpBroadcast(),1);
        ClusterManger clusterManger = new ClusterManger(cluster);
        ClusterCLI clusterCLI = new ClusterCLI(clusterManger);
        clusterCLI.start();
    }
}

