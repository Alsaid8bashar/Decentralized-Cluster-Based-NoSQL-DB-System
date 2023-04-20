package org.example.Cluster;

import org.example.Authentication.UserManager;
import org.example.BroadCast.Topic;
import org.example.Model.User;
import org.json.JSONObject;

import java.util.List;

public class ClusterManger {
    private Cluster cluster;

    public ClusterManger(Cluster cluster) {
        this.cluster = cluster;
    }

    public void startTheCluster() {
        cluster.startCluster();
    }

    public void sendDataToNode(Topic topic, Object o, Container container) {
        cluster.sendDataToNode(topic, o, container);
    }

    public void sendDataToAllNode(Topic topic, Object o) {
        for (Container container : cluster.containers) {
            sendDataToNode(topic, o, container);
        }
    }

    public JSONObject registration(String username, String password) {
        UserManager userManager = new UserManager();
        User user = userManager.registration(username, password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", user.getToken());
        jsonObject.put("worker node information", assignUserToNode(user).toStringUserCopy());
        return jsonObject;
    }

    public Container assignUserToNode(User user) {
        Container node = cluster.addUser();
        user.setNodeID(node.getContainerId());
        sendDataToAllNode(Topic.USER, user);
        return node;
    }

    public java.util.List<Container> getAllClustersNodes() {
        return (List<Container>) cluster.getAllNodes();
    }

    public void stopTheCluster() {
        cluster.stopCluster();
    }

    public void deleteTheCluster() {
        cluster.deleteTheCluster();
    }

    public String getClusterStatue() {
        return cluster.getClusterStatus();
    }

    public void scaleUp(){
        cluster.scaleUp();
    }

    public void restartNode(String nodeId) {
        cluster.restartContainer(nodeId);
    }

    public void stopNode(String nodeId) {
        cluster.stopContainer(nodeId);
    }

}
