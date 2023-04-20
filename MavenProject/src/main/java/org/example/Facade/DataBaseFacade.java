package org.example.Facade;

import org.example.Broadcast.Broadcasting.TcpBroadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.Command;
import org.example.Command.CommandExecutor;
import org.example.Command.CompositCommands.CompositeCommand;
import org.example.Command.CompositCommands.CompositeCommandImp;
import org.example.Command.DecoratorCommands.SyncAllNodesCommand;
import org.example.Command.FilesCommand.DeleteFileCommand;
import org.example.Command.FilesCommand.InsertDataCommand;
import org.example.Command.FilesCommand.ReadFileCommand;
import org.example.Command.FolderCommand.DataBaseFolderCommand;
import org.example.DataBaseComponent.DataBase;
import org.example.DataBaseComponent.IndexComponent.HashIndex;
import org.example.DataBaseComponent.NoSqlDataBase;
import org.example.Files.FilesInfo;
import org.example.Files.FoldersInfo;
import org.example.Model.DataBaseInfo;
import org.example.Model.DataBaseInfoBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataBaseFacade<T> {
    private final Integer userId;
    private final CommandExecutor<Object> commandExecutor;
    private DataBase currentDataBase;

    public DataBaseFacade(Integer userId) {
        commandExecutor = new CommandExecutor<>();
        this.userId = userId;
    }

    public boolean createDataBase(String dataBaseName) {
        DataBase dataBase = NoSqlDataBase.createNoSqlDataBase(new DataBaseInfoBuilder().setUserId(userId).setDbName(dataBaseName).createDataBaseInfo());
        CompositeCommand compositeCommand = new CompositeCommandImp();
        compositeCommand.addCommand(new DataBaseFolderCommand(FoldersInfo.getDataBase(userId, dataBaseName)));
        compositeCommand.addCommand(new InsertDataCommand(FilesInfo.dataBaseInfoPath(userId, dataBaseName), dataBase));
        boolean flag;
        synchronized (this) {
            flag = (boolean) commandExecutor.executeCommand(new SyncAllNodesCommand(compositeCommand, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND));
        }
        return flag;
    }


    public boolean useDb(String dbName) {
        currentDataBase = (DataBase) commandExecutor.executeCommand(new ReadFileCommand(FilesInfo.dataBaseInfoPath(userId, dbName), DataBase.class));
        if (currentDataBase != null)
            return true;
        return false;
    }

    public boolean createCollection(String collName) {
        checkCurrentDb();
        boolean flag = currentDataBase.createCollection(collName);
        updateDataBaseInfo();
        return flag;
    }

    public boolean createSchema(String collName, JSONObject jsonObject) {
        checkCurrentDb();
        checkCollection(collName);


        boolean flag = currentDataBase.getCollection(collName).createSchema(jsonObject);
        updateDataBaseInfo();
        return flag;

    }

    public boolean insertToCollection(String collName, Object data) {
        checkCurrentDb();
        checkCollection(collName);

        boolean flag = currentDataBase.getCollection(collName).insert(data);
        updateDataBaseInfo();
            createIndex(collName, "_id");
        return flag;
    }


    public boolean createIndex(String collName, String property) {
        checkCurrentDb();
        checkCollection(collName);

        Integer affinity = currentDataBase.getCollection(collName).getAffinity();
        DataBaseInfo dataBaseInfo = new DataBaseInfoBuilder().setDbName(currentDataBase.getDbname()).setCollName(collName).setUserId(userId)
                .setPropertyName(property).setAffinityNode(affinity).createDataBaseInfo();
        boolean flag = currentDataBase.getCollection(collName).createIndex(HashIndex.createHashIndex(dataBaseInfo));
        updateDataBaseInfo();
        return flag;

    }

    public JSONArray readALl(String collName) {
        checkCurrentDb();
        checkCollection(collName);

        if (currentDataBase.getCollection(collName) == null)
            return new JSONArray();
        return (JSONArray) currentDataBase.getCollection(collName).read();
    }

    public JSONArray find(String collName, String property, T value) {
        checkCurrentDb();
        checkCollection(collName);

        if (!currentDataBase.getCollection(collName).hasIndex(property))
            createIndex(collName, property);
        return (JSONArray) currentDataBase.getCollection(collName).find(property, value);
    }

    public boolean deleteDb(String dbName) {
        checkCurrentDb();
        Command command = new DeleteFileCommand(FoldersInfo.getDataBase(userId, dbName));
        return (boolean) commandExecutor.executeCommand(new SyncAllNodesCommand(command, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND));
    }

    public boolean deleteCollection(String collName) {
        checkCurrentDb();
        checkCollection(collName);
        return currentDataBase.deleteCollection(collName);
    }

    public void updateDocument(String collName, String id, String property, Object newValue) {
        checkCurrentDb();
        checkCollection(collName);
        currentDataBase.getCollection(collName).updateDocument(id, property, newValue);
    }

    public void deleteDocument(String collName, String property, Object value) {
        checkCurrentDb();
        if (currentDataBase.getCollection(collName) == null)
            return;
        if (!currentDataBase.getCollection(collName).hasIndex(property))
            createIndex(collName, property);
        currentDataBase.getCollection(collName).deleteDocument(property, value);
    }

    private void checkCurrentDb() {
        if (currentDataBase == null)
            throw new IllegalArgumentException("Error: No database selected.");
    }

    private void checkCollection(String collName) {
        if (currentDataBase.getCollection(collName) == null)
            throw new IllegalArgumentException("Error: No collection with name +" + currentDataBase + "found .");

    }

    private void updateDataBaseInfo() {
        synchronized (this) {
            commandExecutor.executeCommand(new SyncAllNodesCommand(new InsertDataCommand(FilesInfo.dataBaseInfoPath(currentDataBase.getUserId(), currentDataBase.getDbname()), currentDataBase), TcpBroadcast.TCP_BROADCAST, Topic.COMMAND));
        }
    }
}
