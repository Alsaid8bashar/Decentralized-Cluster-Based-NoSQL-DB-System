package org.example.Command.FilesCommand;

import org.example.Command.Command;

import java.io.File;
import java.util.logging.Logger;

public class DeleteFileCommand implements Command<Boolean> {
    private final static Logger logger = Logger.getLogger(Command.class.getName());

    private final String filePath;

    public DeleteFileCommand(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Boolean execute() {
        File file = new File(filePath);
        return deleteFolder(file);

    }

    private boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFolder(f);
                }
            }
        }
        return folder.delete();
    }

    @Override
    public String toString() {
        return "DeleteFileCommand{" + "filePath='" + filePath + '\'' + '}';
    }
}
