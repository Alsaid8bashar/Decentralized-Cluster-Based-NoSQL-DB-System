package org.example.Command.CollectionCommand;

import org.example.Command.Command;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;


public class InsertDocumentId implements Command {
    private final String fileData;

    public InsertDocumentId(String fileData) {
        this.fileData = fileData;
    }

    @Override
    public Object execute() {
        JSONArray temp = new JSONArray(fileData);
        JSONArray temoJsonArray = new JSONArray();
        for (int i = 0; i < temp.length(); i++) {
            temoJsonArray.put(write((JSONObject) temp.get(i)));
        }

        return temoJsonArray;
    }
    private JSONObject write(JSONObject doc) {
        StringBuilder tempId = new StringBuilder();
        String  start;
        if (!doc.keySet().contains("_id")) {
            start= String.valueOf(UUID.randomUUID());
            tempId.append(start);
        } else {
            String s = (String) doc.get("_id");
            int underscoreIndex = s.indexOf("_");
            tempId.append(s, 0, underscoreIndex);
        }
        tempId.append("_");
        tempId.append(System.currentTimeMillis());
        doc.put("_id", tempId);
        return doc;
    }
}
