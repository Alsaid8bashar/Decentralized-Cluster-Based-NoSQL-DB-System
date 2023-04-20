package org.example.DataBaseComponent.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.Broadcast.Broadcasting.TcpBroadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.CollectionCommand.*;
import org.example.Command.Command;
import org.example.Command.CommandExecutor;
import org.example.Command.CompositCommands.CompositeCommand;
import org.example.Command.CompositCommands.CompositeCommandImp;
import org.example.Command.DecoratorCommands.SyncAllNodesCommand;
import org.example.Command.FilesCommand.DeleteFileCommand;
import org.example.Command.FilesCommand.ReadFileCommand;
import org.example.DataBaseComponent.CheckAffinity;
import org.example.DataBaseComponent.IndexComponent.Index;
import org.example.DataBaseComponent.IndexComponent.IndexProperty;
import org.example.DataBaseComponent.IndexComponent.Reference;
import org.example.DataBaseComponent.SchemaComponent.NoSqlSchemaImpl;
import org.example.Files.FilesInfo;
import org.example.Files.FoldersInfo;
import org.example.Model.DataBaseInfo;
import org.example.Model.DataBaseInfoBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@JsonTypeName("NoSqlCollectionImp")
public class NoSqlCollectionImp implements NoSqlCollection {
    @JsonIgnore
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    @JsonIgnore
    private transient CommandExecutor<JSONArray> commandExecutor;
    private DataBaseInfo dataBaseInfo;
    private Map<String, Index> indexMap;
    private NoSqlSchemaImpl noSqlSchemaImpl;
    @JsonIgnore
    private transient CheckAffinity checkAffinity = new CheckAffinity();


    private NoSqlCollectionImp(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
        indexMap = new Hashtable<>();
        commandExecutor = new CommandExecutor<>();
    }

    private NoSqlCollectionImp() {
        commandExecutor = new CommandExecutor<>();
    }

    public static NoSqlCollectionImp createNoSqlCollectionImp(DataBaseInfo dataBaseInfo) {
        return new NoSqlCollectionImp(dataBaseInfo);
    }

    @Override
    public boolean createSchema(JSONObject jsonObject) {
        DataBaseInfo dataBaseInfo1 = new DataBaseInfoBuilder().setCollName(dataBaseInfo.getCollName()).setDbName(dataBaseInfo.getDbName()).setAffinityNode(dataBaseInfo.getAffinityNode()).setUserId(dataBaseInfo.getUserId()).createDataBaseInfo();
        NoSqlSchemaImpl noSqlSchemaImpl = NoSqlSchemaImpl.createNoSqlSchemaImpl(dataBaseInfo1);
        this.noSqlSchemaImpl = noSqlSchemaImpl;
        return noSqlSchemaImpl.create(jsonObject);
    }

    @Override
    public boolean insert(Object data) {
        JSONArray checkData = new ValidateCollectionDataCommand(data, dataBaseInfo).execute();
        if (checkData.length() == 0)
            return false;
        JSONArray jsonArray = commandExecutor.executeCommand(new InsertDocumentId(checkData.toString()));
        SyncAllNodesCommand command = new SyncAllNodesCommand(new InsertCollectionDataCommand(jsonArray.toString(), FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName())), TcpBroadcast.TCP_BROADCAST, Topic.COMMAND);
        lock.writeLock().lock();
        boolean flag = checkAffinity.check(command, dataBaseInfo.getAffinityNode());
        lock.writeLock().unlock();
        updateIndexesAfterInsert(jsonArray);
        return flag;
    }

    @Override
    public Object read() {
        lock.readLock().lock();
        JSONArray jsonArray = commandExecutor.executeCommand(new ReadAllCommand(FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName())));
        lock.readLock().unlock();
        return jsonArray;
    }

    @Override
    public boolean createIndex(Index index) {
        boolean flag = index.createIndex();
        indexMap.put(index.getPropertyName(), index);
        return flag;
    }

    @Override
    public Object find(String property, Object value) {
        dataBaseInfo.setPropertyName(property);
        IndexProperty tempIndexProperty = (IndexProperty) new ReadFileCommand(FilesInfo.indexPath(dataBaseInfo), IndexProperty.class).execute();
        java.util.List<Reference> referenceList;
        JSONArray jsonArray = new JSONArray();
        if (tempIndexProperty == null || tempIndexProperty.getReferences().get(value.hashCode()) == null) {
            return jsonArray;
        } else {
            referenceList = tempIndexProperty.getReferences().get(value.hashCode());
        }
        String fileName = FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName());
        lock.readLock().lock();
        for (Reference reference : referenceList) {
            String tempPath = fileName + "/" + reference.getReference() + ".json";
            Object jsonString = new ReadFileCommand(tempPath, Object.class).execute();
            if (jsonString != null) jsonArray.put(jsonString);
        }
        lock.readLock().unlock();
        return jsonArray;
    }


    @Override
    public boolean deleteDocument(String property, Object value) {
        JSONArray jsonArray = new JSONArray(find(property, value).toString());
        if (!jsonArray.isEmpty()) {
            CompositeCommand compositeCommand = new CompositeCommandImp();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.optJSONObject(i).toString());
                String tempId = (String) jsonObject.get("_id");
                int underscoreIndex = tempId.indexOf("_");
                String id = tempId.substring(0, underscoreIndex);
                compositeCommand.addCommand(new DeleteFileCommand(FilesInfo.documentPath(dataBaseInfo, id)));
            }
            updateIndexesAfterDelete(jsonArray);
            SyncAllNodesCommand command = new SyncAllNodesCommand(compositeCommand, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND);
            lock.writeLock().lock();
            boolean flag = checkAffinity.check(command, dataBaseInfo.getAffinityNode());
            lock.writeLock().unlock();
            return flag;
        }
        return true;
    }

    @Override
    public boolean updateDocument(String id, String property, Object newValue) {
        JSONArray jsonArray = new JSONArray(find("_id", id).toString());
        if (jsonArray.isEmpty())
            return false;
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        jsonObject.put(property, newValue);
        Command command = new UpdateDocumentCommand(jsonObject.toString(), dataBaseInfo);
        lock.writeLock().lock();
        boolean isPublished = checkAffinity.check(command, dataBaseInfo.getAffinityNode());
        lock.writeLock().unlock();
        if (isPublished) {
            JSONObject temp = isDocumentUpdated(jsonObject);
            if (!temp.isEmpty()) {
                updateIndexesAfterDelete(new JSONArray().put(jsonObject));
                updateIndexesAfterInsert(new JSONArray().put(temp));
                return true;
            }
            else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                updateDocument(id, property, newValue);
            }
        }
        return false;
    }

    private JSONObject isDocumentUpdated(JSONObject jsonObject) {
        String fileName = FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName());
        String tempId = (String) jsonObject.get("_id");
        int underscoreIndex = tempId.indexOf("_");
        String id = tempId.substring(0, underscoreIndex);
        JsonNode jsonNode = (JsonNode) new ReadFileCommand(fileName + "/" + id + ".json", JsonNode.class).execute();
        if (!jsonNode.get("_id").asText().equals(tempId)) {
            jsonObject.put("_id", jsonNode.get("_id").asText());
            System.out.println("jsonObject.isEmpty() = " + jsonObject.isEmpty());
            return jsonObject;
        }
        return new JSONObject();

    }

    private void updateIndexesAfterInsert(JSONArray jsonArray) {
        for (Index index : indexMap.values()) {
            index.updateAfterInsert(jsonArray);
        }
    }

    private void updateIndexesAfterDelete(JSONArray jsonArray) {
        for (Index index : indexMap.values()) {
            index.updateAfterDelete(jsonArray);
        }
    }


    @Override
    public boolean hasIndex(String property) {
        return indexMap.containsKey(property);
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

    public DataBaseInfo getDataBaseInfo() {
        return dataBaseInfo;
    }

    public void setDataBaseInfo(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
    }

    @Override
    public String toString() {
        return "NoSqlCollectionImp{" + "dataBaseInfo=" + dataBaseInfo + ", indexMap=" + indexMap + ", noSqlSchemaImpl=" + noSqlSchemaImpl + '}';
    }

    public NoSqlSchemaImpl getSchema() {
        return noSqlSchemaImpl;
    }


    public void setSchema(NoSqlSchemaImpl noSqlSchemaImpl) {
        this.noSqlSchemaImpl = noSqlSchemaImpl;
    }


    public Map<String, Index> getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(Map<String, Index> indexMap) {
        this.indexMap = indexMap;
    }


}
