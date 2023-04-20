package org.example.Command.CollectionCommand;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.Command.Command;
import org.example.Command.FilesCommand.ReadFileCommand;
import org.example.DataBaseComponent.SchemaComponent.SchemaValidator;
import org.example.Files.FilesInfo;
import org.example.Model.DataBaseInfo;
import org.json.JSONArray;
import org.json.JSONObject;

public class ValidateCollectionDataCommand implements Command<JSONArray> {
    private final Object fileData;
    private DataBaseInfo dataBaseInfo;

    public ValidateCollectionDataCommand(Object fileData, DataBaseInfo dataBaseInfo) {
        this.fileData = fileData;
        this.dataBaseInfo = dataBaseInfo;
    }

    @Override
    public JSONArray execute() {
        JsonNode jsonNode = (JsonNode) new ReadFileCommand(FilesInfo.schemaPath(dataBaseInfo), JsonNode.class).execute();
        if (jsonNode == null) {
            return new JSONArray();
        }
        JSONObject jsonObject = new JSONObject(jsonNode.toString());
        if (jsonObject.isEmpty())
            throw new IllegalArgumentException("this collection does not have a schema");
        return SchemaValidator.isValid(jsonObject, (JSONArray) fileData);
    }

    @Override
    public String toString() {
        return "CheckCollectionData{" +
                "fileData=" + fileData +
                ", dataBaseInfo=" + dataBaseInfo +
                '}';
    }
}
