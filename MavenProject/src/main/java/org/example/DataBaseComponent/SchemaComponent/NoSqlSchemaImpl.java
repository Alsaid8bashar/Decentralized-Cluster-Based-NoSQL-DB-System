package org.example.DataBaseComponent.SchemaComponent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Broadcast.Broadcasting.TcpBroadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.Command;
import org.example.Command.CommandExecutor;
import org.example.Command.CompositCommands.CompositeCommand;
import org.example.Command.CompositCommands.CompositeCommandImp;
import org.example.Command.DecoratorCommands.SyncAllNodesCommand;
import org.example.Command.FilesCommand.CreateFileCommand;
import org.example.Command.FilesCommand.InsertDataCommand;
import org.example.Command.SchemaCommand.CreateSchemaCommand;
import org.example.DataBaseComponent.CheckAffinity;
import org.example.Files.FilesInfo;
import org.example.Model.DataBaseInfo;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@JsonTypeName("NoSqlSchemaImpl")
@SuppressWarnings("rawtypes")
public class NoSqlSchemaImpl implements NoSqlSchema {

    @JsonIgnore
    private final ReadWriteLock lock;
    private DataBaseInfo dataBaseInfo;
    @JsonIgnore
    private transient CommandExecutor commandExecutor;

    private NoSqlSchemaImpl(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
        commandExecutor = new CommandExecutor();
        lock = new ReentrantReadWriteLock();
    }

    public NoSqlSchemaImpl() {
        this.lock = new ReentrantReadWriteLock();
        commandExecutor = new CommandExecutor();
    }

    public static NoSqlSchemaImpl createNoSqlSchemaImpl(DataBaseInfo dataBaseInfo) {
        return new NoSqlSchemaImpl(dataBaseInfo);
    }

    @Override
    public boolean create(JSONObject o) {
        lock.writeLock().lock();
        saveTheSchema((JSONObject) commandExecutor.executeCommand(new CreateSchemaCommand(o)));
        lock.writeLock().unlock();
        return true;
    }

    @JsonIgnore
    @Override
    public Integer getAffinity() {
        return dataBaseInfo.getAffinityNode();
    }

    @JsonIgnore
    @Override
    public void setAffinity(Integer affinity) {
        dataBaseInfo.setAffinityNode(affinity);
    }

    private void saveTheSchema(JSONObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(String.valueOf(jsonObject));
        } catch (IOException e) {
            return;
        }
        Object json = mapper.convertValue(jsonNode, Object.class);
        CompositeCommand compositeCommand=new CompositeCommandImp();
        compositeCommand.addCommand(new CreateFileCommand(FilesInfo.schemaPath(dataBaseInfo)));
        compositeCommand.addCommand(new InsertDataCommand(FilesInfo.schemaPath(dataBaseInfo), json));
        Command command = new SyncAllNodesCommand(compositeCommand, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND);
        CheckAffinity checkAffinity = new CheckAffinity();
        checkAffinity.check(command, dataBaseInfo.getAffinityNode());
    }

    public ReadWriteLock getLock() {
        return lock;
    }

}
