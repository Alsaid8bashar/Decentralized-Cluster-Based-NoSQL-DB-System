package org.example.Files;

import java.nio.file.Path;

public class BootstrappingNodeFileInfo implements FileInfo {

    @Override
    public Path userInfoPath(long userId) {
        return Path.of("Users" + "/" + userId + ".Json");
    }

    @Override
    public Path nodeInfoPath(String nodeId) {
        return Path.of("workerNodes" + "/" + nodeId + ".Json");
    }
}
