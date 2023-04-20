package org.example.Command.FolderCommand;

import org.example.Command.Command;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataBaseFolderCommand implements Command<Boolean> {
    private  String path;
    public DataBaseFolderCommand(String path) {
        this.path = path;
    }
    @Override
    public Boolean execute() {
        path=Paths.get(path).toString();
        File db = new File(path);
        File coll = new File(Path.of(path, "/Collections").toString());
        File indexes = new File(Path.of(path, "/Indexes").toString());
        File schema = new File(Path.of(path, "/Schema").toString());
        if (db.mkdir()) {
            System.out.println("is exist "+ db.exists());
            if (coll.mkdir()) System.out.println("coll created");
            if (indexes.mkdir()) System.out.println("index created");
            if (schema.mkdir()) System.out.println("schema created");
            return true;
        }
        return false;
    }
}
