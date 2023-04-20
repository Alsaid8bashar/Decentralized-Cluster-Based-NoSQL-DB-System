package org.example.Command.FolderCommand;

import org.example.Command.Command;

import java.io.File;

public class CreateFolderCommand implements Command<Boolean> {
    private final String path ;
    public CreateFolderCommand(String path) {
        this.path = path;
    }
    @Override
    public Boolean execute() {
        File file = new File(path);
        return file.mkdir();
    }

    @Override
    public String toString() {
        return "CreateFolderCommand{" +
                "path='" + path + '\'' +
                '}';
    }
}
