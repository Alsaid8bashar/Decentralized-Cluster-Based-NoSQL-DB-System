package org.example.Command.CollectionCommand;

import org.example.Command.Command;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InsertCollectionDataCommand implements Command<Boolean> {
    private final String fileData;
    private final String path;

    public InsertCollectionDataCommand(String fileData, String path) {
        this.fileData = fileData;
        this.path = path;
    }

    @Override
    public Boolean execute() {
        Object temp = new JSONArray(fileData);
        JSONArray jsonArray = (JSONArray) temp;
        for (int i = 0; i < jsonArray.length(); i++) {
            write((JSONObject) jsonArray.get(i), path);
        }
        return true;
    }

    private void write(JSONObject doc, String path) {
        BufferedOutputStream outputStream;
        String tempId = (String) doc.get("_id");
        int underscoreIndex = tempId.indexOf("_");
        String docId = tempId.substring(0, underscoreIndex);
        String jsonStr = doc.toString();
        File file = new File(path + "/" + docId + ".json");
        try {
            file.createNewFile();
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(jsonStr.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String toString() {
        return "InsertDataCommand{" +
                "fileData='" + fileData + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
