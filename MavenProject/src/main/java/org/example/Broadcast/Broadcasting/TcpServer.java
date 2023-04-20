package org.example.Broadcast.Broadcasting;

import org.example.Broadcast.ServerHandlers.CommandHandler;
import org.example.Broadcast.ServerHandlers.ContainerHandler;
import org.example.Broadcast.ServerHandlers.RequestHandler;
import org.example.Broadcast.ServerHandlers.UsersHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpServer implements Runnable {
    private final static Logger logger = Logger.getLogger(TcpServer.class.getName());
    private static volatile TcpServer tcpServer;
    private final int PORT;
    private RequestHandler handler1;
    private boolean isRunning;

    private TcpServer(int PORT) {
        this.PORT = PORT;
        handler1 = new CommandHandler();
        RequestHandler handler2 = new ContainerHandler();
        RequestHandler handler3 = new UsersHandler();
        handler1.setNextHandler(handler2);
        handler2.setNextHandler(handler3);
    }

    public static TcpServer of(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port number: " + port);
        }
        if (tcpServer == null) {
            synchronized (TcpServer.class) {
                tcpServer = new TcpServer(port);
            }
        }
        return tcpServer;
    }

    @Override
    public void run() {
        if (isRunning) {
            return;
        }
        isRunning = true;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            ExecutorService pool = Executors.newCachedThreadPool();
            while (isRunning) {
                Socket client = serverSocket.accept();
                Thread connectionHandler = new Thread(new Runnable() {
                    private Socket client;
                    private String topic;
                    private Object data;

                    public Runnable connectionHandler(Socket client) {
                        this.client = client;
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
                            DataInputStream inputStream = new DataInputStream(client.getInputStream());
                            topic = inputStream.readUTF();
                            data = objectInputStream.readObject();
                            if (topic == null)
                                System.out.println(data.toString());
                        } catch (IOException | ClassNotFoundException e) {
                            logger.log(Level.SEVERE, "server error", e);
                        }
                        return this;
                    }

                    @Override
                    public void run() {
                        try {
                            handler1.handleRequest(topic, data);
                        } finally {
                            try {
                                client.close();
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "server error", e);
                            }
                        }
                    }
                }.connectionHandler(client));
                pool.execute(connectionHandler);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "server error", e);
        }
    }

    @Override
    public String toString() {
        return "Server{" +
                "port=" + PORT +
                '}';
    }
}
