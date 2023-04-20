package org.example.Authentication;

import org.example.Cluster.NodeManager;
import org.example.Command.FilesCommand.ReadFileCommand;
import org.example.Files.FilesInfo;
import org.example.Model.User;

import java.nio.file.Files;
import java.nio.file.Path;

public class UserAuthentication {
   private final TokenService tokenService = new TokenService();
    public int isUserAuthenticate(String token) {
        long id = tokenService.getUserId(token);
        Path path = Path.of(FilesInfo.userInfoPath(id));
        if (Files.exists(path)) {
            User user = (User) new ReadFileCommand(path.toString(), User.class).execute();
            if (token.equals(user.getToken())&&user.getNodeID().equals(NodeManager.NODE_MANAGER.getThisContainer().getContainerId()))
                return Math.toIntExact(id);
        }
        return 0;
    }


}
