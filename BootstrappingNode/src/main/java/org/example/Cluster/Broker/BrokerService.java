package org.example.Cluster.Broker;

import java.io.IOException;

public class BrokerService {
    public static void startTheBroker() {
        try {
            Process process = new ProcessBuilder("docker-compose", "-f", "C:\\Users\\basha\\OneDrive\\Desktop\\Intellij project\\BootstrappingNode\\src\\main\\java\\org\\example\\Cluster\\Broker\\Docker-compose.yml", "up").start();
            Thread.sleep(40000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
