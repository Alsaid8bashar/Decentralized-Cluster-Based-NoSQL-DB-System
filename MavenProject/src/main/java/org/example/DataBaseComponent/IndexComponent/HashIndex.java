package org.example.DataBaseComponent.IndexComponent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.Broadcast.Broadcasting.TcpBroadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.Command;
import org.example.Command.CommandExecutor;
import org.example.Command.DecoratorCommands.SyncAllNodesCommand;
import org.example.Command.FilesCommand.InsertDataCommand;
import org.example.Command.FilesCommand.ReadFileCommand;
import org.example.Command.FolderCommand.CreateFolderCommand;
import org.example.DataBaseComponent.CheckAffinity;
import org.example.DataBaseComponent.IAffinity;
import org.example.Files.FilesInfo;
import org.example.Files.FoldersInfo;
import org.example.Model.DataBaseInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@JsonTypeName("HashIndex")
public class HashIndex implements Index, IAffinity {
    @JsonIgnore
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private DataBaseInfo dataBaseInfo;
    @JsonIgnore
    private transient CommandExecutor<Object> commandExecutor;

    private HashIndex(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
        commandExecutor = new CommandExecutor();
    }

    private HashIndex() {
        commandExecutor = new CommandExecutor();
    }

    public static HashIndex createHashIndex(DataBaseInfo dataBaseInfo) {
        return new HashIndex(dataBaseInfo);
    }

    @Override
    public boolean createIndex() {
        IndexProperty indexProperty = new HashIndexIndexProperty(dataBaseInfo.getPropertyName());
        commandExecutor.executeCommand(new SyncAllNodesCommand(new CreateFolderCommand(FoldersInfo.indexPath(dataBaseInfo)), TcpBroadcast.TCP_BROADCAST, Topic.COMMAND));
        indexProperty.setReferences(createReferences(indexProperty.getReferences()));
        save(indexProperty);
        return true;
    }

    @Override
    public void updateAfterDelete(JSONArray jsonArray) {
        lock.readLock().lock();
        IndexProperty indexProperty = (IndexProperty) commandExecutor.executeCommand(new ReadFileCommand(FilesInfo.indexPath(dataBaseInfo), IndexProperty.class));
        lock.readLock().unlock();
        Map<Integer, List<Reference>> indexMap = indexProperty.getReferences();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = new JSONObject(jsonArray.optJSONObject(i).toString());
            Object property = jsonObject.get(dataBaseInfo.getPropertyName());
            indexMap.remove(property.hashCode());
        }
        indexProperty.setReferences(indexMap);
        save(indexProperty);
    }

    @Override
    public void updateAfterInsert(JSONArray jsonArray) {
        lock.readLock().lock();
        IndexProperty indexProperty = (IndexProperty) commandExecutor.executeCommand(new ReadFileCommand(FilesInfo.indexPath(dataBaseInfo), IndexProperty.class));
        lock.readLock().unlock();
        Map<Integer, List<Reference>> references = indexProperty.getReferences();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Object property = jsonObject.get(dataBaseInfo.getPropertyName());
            String tempId = jsonObject.get("_id").toString();
            addReferences(references, property, tempId);
        }
        indexProperty.setReferences(references);
        save(indexProperty);
    }

    private Map createReferences(Map<Integer, List<Reference>> references) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        File file = new File(FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName()));
        File[] files = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            JsonNode jsonNode = (JsonNode) new ReadFileCommand(files[i].getPath(), JsonNode.class).execute();
            Object property = jsonNode.get(dataBaseInfo.getPropertyName());
            String tempId = jsonNode.get("_id").asText();
            addReferences(references, property, tempId);
        }
        return references;
    }

    private void addReferences(Map<Integer, List<Reference>> references, Object property, String tempId) {
        int underscoreIndex = tempId.indexOf("_");
        String docId = tempId.substring(0, underscoreIndex);
        if (references.containsKey(property.hashCode())) {
            references.get(property.hashCode()).add(new HashIndexReference(docId));
        } else {
            List<Reference> tempList = new ArrayList<>();
            tempList.add(new HashIndexReference(docId));
            references.put(property.hashCode(), tempList);
        }
    }

    private void save(IndexProperty indexProperty) {
        Command<Boolean> command = new SyncAllNodesCommand(new InsertDataCommand(FilesInfo.indexPath(dataBaseInfo), indexProperty),
                TcpBroadcast.TCP_BROADCAST, Topic.COMMAND);
        CheckAffinity checkAffinity = new CheckAffinity();
        lock.writeLock().lock();
        checkAffinity.check(command, dataBaseInfo.getAffinityNode());
        lock.writeLock().unlock();
    }

    @JsonIgnore
    @Override
    public String getPropertyName() {
        return dataBaseInfo.getPropertyName();
    }

    @JsonIgnore
    @Override
    public Integer getAffinity() {
        return dataBaseInfo.getAffinityNode();
    }

    @JsonIgnore
    @Override
    public void setAffinity(Integer affinity) {
        this.dataBaseInfo.setAffinityNode(affinity);
    }

    public ReadWriteLock getLock() {
        return lock;
    }


    public DataBaseInfo getDataBaseInfo() {
        return dataBaseInfo;
    }

    public void setDataBaseInfo(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
    }

    @Override
    public String toString() {
        return "HashIndex{" +
                "dataBaseInfo=" + dataBaseInfo +
                '}';
    }
}
