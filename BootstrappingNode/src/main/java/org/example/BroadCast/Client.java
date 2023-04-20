package org.example.BroadCast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable  {
    private DataOutputStream outputToServer;
    private Object data;
    private Topic topic;
    private ObjectOutputStream objectOutputStream;
    private final String hostIp;
    private final int serverPort;

    public Client(String hostIp, int serverPort) {
        this.hostIp = hostIp;
        this.serverPort = serverPort;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public void run() {
        try ( Socket socket = new Socket(hostIp, serverPort) ){
            outputToServer = new DataOutputStream(socket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            send();
            outputToServer.close();
            objectOutputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void send() {
        try {
            if (data != null) {
                outputToServer.writeUTF(topic.toString());
                objectOutputStream.writeObject(data);
                System.out.println("message sent");
                data = null;
                topic=null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

