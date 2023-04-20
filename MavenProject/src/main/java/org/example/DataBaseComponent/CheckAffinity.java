package org.example.DataBaseComponent;

import org.example.Cluster.NodeManager;
import org.example.Broadcast.Broadcasting.TcpBroadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.Command;
import org.example.Command.CommandExecutor;
import org.example.Command.SyncAffinityCommand;

public class CheckAffinity {
    private CommandExecutor<Boolean> commandExecutor;

    public CheckAffinity() {
        commandExecutor = new CommandExecutor();
    }

    public boolean check(Command command, int affinityNodeId) {
        if (affinityNodeId == (NodeManager.NODE_MANAGER.getThisContainer().getId())) {
            return commandExecutor.executeCommand(command);
        } else {
          return redirect(command,affinityNodeId);
        }
    }

    private boolean redirect(Command command, int affinityNodeId ){
        return commandExecutor.executeCommand(new SyncAffinityCommand(command, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND, affinityNodeId));
    }
}
