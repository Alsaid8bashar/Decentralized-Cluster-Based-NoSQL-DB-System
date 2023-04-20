package org.example.DataBaseComponent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.Broadcast.Broadcasting.TcpBroadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Cluster.AffinityLoadBalancer;
import org.example.Command.Command;
import org.example.Command.CompositCommands.CompositeCommand;
import org.example.Command.CompositCommands.CompositeCommandImp;
import org.example.Command.DecoratorCommands.SyncAllNodesCommand;
import org.example.Command.FilesCommand.DeleteFileCommand;
import org.example.Command.FolderCommand.CreateFolderCommand;
import org.example.DataBaseComponent.Collection.NoSqlCollection;
import org.example.DataBaseComponent.Collection.NoSqlCollectionImp;
import org.example.Files.FilesInfo;
import org.example.Files.FoldersInfo;
import org.example.Model.DataBaseInfo;
import org.example.Model.DataBaseInfoBuilder;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@JsonTypeName("NoSqlDataBase")
public class NoSqlDataBase implements DataBase {
    @JsonIgnore
    private final Lock lock = new ReentrantLock();
    private DataBaseInfo dataBaseInfo;
    private Map<String, NoSqlCollection> collectionMap;
    @JsonIgnore
    private transient CheckAffinity checkAffinity = new CheckAffinity();


    private NoSqlDataBase(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
        collectionMap = new Hashtable<>();
    }

    private NoSqlDataBase() {
        collectionMap = new Hashtable<>();
    }

    public static NoSqlDataBase createNoSqlDataBase(DataBaseInfo dataBaseInfo) {
        return new NoSqlDataBase(dataBaseInfo);
    }

    @Override
    public boolean createCollection(String collName) {
        int affinityNodeId = AffinityLoadBalancer.LOAD_BALANCER.getNextServer();
        DataBaseInfo tempDataBaseInfo = new DataBaseInfoBuilder().setUserId(dataBaseInfo.getUserId()).setCollName(collName).setDbName(dataBaseInfo.getDbName()).
                setAffinityNode(affinityNodeId).createDataBaseInfo();
        NoSqlCollection collection = NoSqlCollectionImp.createNoSqlCollectionImp(tempDataBaseInfo);
        collectionMap.put(collName, collection);
        Command<Boolean> createFolderCommand = new CreateFolderCommand(FoldersInfo.collectionPath(dataBaseInfo, collName));
        Command syncAllNodesCommand = new SyncAllNodesCommand(createFolderCommand, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND);
        lock.lock();
        boolean flag = checkAffinity.check(syncAllNodesCommand, affinityNodeId);
        lock.unlock();
        return flag;
    }


    @Override
    public boolean deleteCollection(String collName) {
        dataBaseInfo.setCollName(collName);
        CompositeCommand compositeCommand = new CompositeCommandImp();
        compositeCommand.addCommand(new DeleteFileCommand(FoldersInfo.collectionPath(dataBaseInfo, collName)));
        compositeCommand.addCommand(new DeleteFileCommand(FoldersInfo.indexPath(dataBaseInfo)));
        compositeCommand.addCommand(new DeleteFileCommand(FilesInfo.schemaPath(dataBaseInfo)));
        Command syncAllNodesCommand = new SyncAllNodesCommand(compositeCommand, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND);
        lock.lock();
        boolean flag = checkAffinity.check(syncAllNodesCommand, collectionMap.get(collName).getAffinity());
        collectionMap.remove(collName);
        lock.unlock();
        return flag;

    }

    @Override
    public NoSqlCollection getCollection(String collName) {
        return collectionMap.get(collName);
    }

    @JsonIgnore
    @Override
    public String getDbname() {
        return dataBaseInfo.getDbName();
    }

    @JsonIgnore
    @Override
    public Integer getUserId() {
        return dataBaseInfo.getUserId();
    }


    public Map<String, NoSqlCollection> getCollectionMap() {
        return collectionMap;
    }

    public void setCollectionMap(Map<String, NoSqlCollection> collectionMap) {
        this.collectionMap = collectionMap;
    }

    public DataBaseInfo getDataBaseInfo() {
        return dataBaseInfo;
    }

    public void setDataBaseInfo(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
    }

    @Override
    public String toString() {
        return "NoSqlDataBase{" +
                "dataBaseInfo=" + dataBaseInfo +
                ", collectionMap=" + collectionMap +
                '}';
    }
}
