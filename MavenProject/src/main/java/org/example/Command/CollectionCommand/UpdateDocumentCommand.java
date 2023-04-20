package org.example.Command.CollectionCommand;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.Broadcast.Broadcasting.TcpBroadcast;
import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.Command;
import org.example.Command.DecoratorCommands.SyncAllNodesCommand;
import org.example.Command.FilesCommand.ReadFileCommand;
import org.example.Files.FoldersInfo;
import org.example.Model.DataBaseInfo;
import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateDocumentCommand implements Command<Boolean> {
    private Object fileData;

    private DataBaseInfo dataBaseInfo;

    public UpdateDocumentCommand(Object fileData, DataBaseInfo dataBaseInfo) {
        this.fileData = fileData;
        this.dataBaseInfo = dataBaseInfo;
    }


    @Override
    public Boolean execute() {
        if (checkUpdate()) {
            JSONObject jsonObject = new JSONObject(fileData.toString());
            JSONArray temp = new JSONArray();
            temp.put(jsonObject);
            JSONArray jsonArray2 = (JSONArray) new InsertDocumentId(temp.toString()).execute();
            Command command = new InsertCollectionDataCommand(jsonArray2.toString(),
                FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName()));
            new SyncAllNodesCommand(command, TcpBroadcast.TCP_BROADCAST, Topic.COMMAND).execute();
        }

        return true;
    }

    private boolean checkUpdate(){
        System.out.println(fileData.toString());
        JSONObject jsonObject = new JSONObject(fileData.toString());
        String fileName = FoldersInfo.collectionPath(dataBaseInfo, dataBaseInfo.getCollName());
        String tempId = (String) jsonObject.get("_id");
        int underscoreIndex = tempId.indexOf("_");
        String id = tempId.substring(0, underscoreIndex);
        JsonNode jsonNode = (JsonNode) new ReadFileCommand(fileName + "/" + id + ".json", JsonNode.class).execute();
        if (tempId.equals(jsonNode.get("_id").asText())) {
            return true;
        }

        return false;
    }
}
