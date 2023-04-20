package org.example.Files;

import java.nio.file.Path;

public interface FileInfo {

    Path userInfoPath(long userId);

    Path nodeInfoPath(String nodeId);
}

