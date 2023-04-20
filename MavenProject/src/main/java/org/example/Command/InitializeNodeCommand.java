package org.example.Command;

import org.example.Broadcast.Broadcasting.TcpServer;

import java.io.File;

public class InitializeNodeCommand implements Command<Boolean> {
    private int serverPort;

    public InitializeNodeCommand(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public Boolean execute() {
        File file = new File("Storage");
        File file2 = new File("Storage/Users");
        File file3 = new File("workerNodes");
        TcpServer tcpServer = TcpServer.of(serverPort);
        new Thread(tcpServer).start();
        return file.mkdir() && file2.mkdir() && file3.mkdir();
    }
}
