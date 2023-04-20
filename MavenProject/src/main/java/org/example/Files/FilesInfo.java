package org.example.Files;

import org.example.Model.DataBaseInfo;

import java.nio.file.Path;

public class FilesInfo {
    private FilesInfo() {
    }

    public static String dataBaseInfoPath(int userId, String dbName) {
        return Path.of(FoldersInfo.getDataBase(userId, dbName), dbName + ".Json").toString();
    }

    public static String indexPath(DataBaseInfo dataBaseInfo) {
        return Path.of(FoldersInfo.indexPath(dataBaseInfo), dataBaseInfo.getPropertyName() + ".Json").toString();
    }

    public static String documentPath(DataBaseInfo dataBaseInfo, String docId) {
        return Path.of(FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName()), docId + ".json").toString();
    }

    public static String userInfoPath(long userId) {
        return Path.of(FoldersInfo.userPath(userId), userId + ".Json").toString();
    }



    public static String schemaPath(DataBaseInfo dataBaseInfo) {
        return Path.of(FoldersInfo.schemaPath(dataBaseInfo), dataBaseInfo.getCollName() + ".Json").toString();
    }

    public static String nodesPath(String nodeId) {
        return Path.of("workerNodes", nodeId + ".Json").toString();
    }
}
