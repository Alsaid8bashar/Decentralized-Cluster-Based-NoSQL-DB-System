package org.example.Command.CollectionCommand;

import org.example.Command.Command;
import org.example.Command.FilesCommand.ReadFileCommand;
import org.json.JSONArray;

import java.io.File;
import java.util.Objects;

public class ReadAllCommand implements Command<JSONArray> {
    private final String path;

    public ReadAllCommand(String path) {
        this.path = path;
    }

    @Override
    public JSONArray execute() {
        File file = new File(path);
        File[] files = file.listFiles();
        JSONArray jsonArray = new JSONArray();
        System.out.println(path);
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            Object jsonString = new ReadFileCommand(files[i].getPath(), Object.class).execute();
            if (jsonString != null) jsonArray.put(jsonString);
        }
        return jsonArray;
    }

    @Override
    public String toString() {
        return "ReadAllCommand{" + "path='" + path + '\'' + '}';
    }
}
