package org.example.Broadcast.Broadcasting;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpClient implements Runnable  {
    private final static Logger logger = Logger.getLogger(TcpClient.class.getName());

    private DataOutputStream outputToServer;
    private Object data;
    private Topic topic;
    private ObjectOutputStream objectOutputStream;
    private final String hostIp;
    private final int serverPort;

    private TcpClient(String hostIp, int serverPort) {
        this.hostIp = hostIp;
        this.serverPort = serverPort;
    }

    public static TcpClient createTcpClient(String hostIp, int serverPort) {
        return new TcpClient(hostIp, serverPort);
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
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Tcp client error", e);

        }
    }

    private void send() {
        try {
            if (data != null) {
                outputToServer.writeUTF(topic.toString());
                objectOutputStream.writeObject(data);
                System.out.println("message sent");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Tcp client error", e);
        }
    }

    @Override
    public String toString() {
        return "TcpClient{" +
                "data=" + data +
                ", topic=" + topic +
                ", hostIp='" + hostIp + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }
}

