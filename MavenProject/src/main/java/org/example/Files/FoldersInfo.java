package org.example.Files;

import org.example.Model.DataBaseInfo;

import java.nio.file.Path;

public class FoldersInfo {

    private FoldersInfo() {
    }
    private static final String dbFolder = "Storage/Users";

    public static String getDataBase(int userId, String dbName) {
        return Path.of(dbFolder + "/" + userId, "/DataBases", dbName).toString();
    }
    public static String getDataBase(DataBaseInfo dataBaseInfo) {
        return Path.of(dbFolder + "/" + dataBaseInfo.getUserId(), "/DataBases", dataBaseInfo.getDbName()).toString();
    }

    public static String collectionPath(DataBaseInfo dataBaseInfo, String collectionName) {
        return Path.of(dbFolder + "/" + dataBaseInfo.getUserId(), "/DataBases", dataBaseInfo.getDbName(), "Collections", collectionName).toString();
    }

    public static String indexPath(DataBaseInfo dataBaseInfo) {
        return Path.of(dbFolder + "/" + dataBaseInfo.getUserId(), "/DataBases", dataBaseInfo.getDbName(), "Indexes", dataBaseInfo.getCollName() + "Indexes").toString();
    }
    public static String userPath(long userId) {
        return Path.of(dbFolder, "/" + userId).toString();}


    public static String schemaPath(DataBaseInfo dataBaseInfo) {
        return Path.of(dbFolder + "/" + dataBaseInfo.getUserId(), "/DataBases", dataBaseInfo.getDbName(), "Schema").toString();
    }
}
