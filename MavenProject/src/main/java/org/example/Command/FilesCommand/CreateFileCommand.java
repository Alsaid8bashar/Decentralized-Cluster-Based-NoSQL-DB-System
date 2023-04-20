package org.example.Command.FilesCommand;

import org.example.Command.Command;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateFileCommand implements Command<Boolean> {
    private final static Logger logger = Logger.getLogger(Command.class.getName());

    private final String filePath;
    public CreateFileCommand(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public Boolean execute() {
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                return true;
            }
        } catch (IOException e) {

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to execute operation", e);
        }
        return false;

    }

    @Override
    public String toString() {
        return "CreateFileCommand{" +
                "filePath='" + filePath + '\'' +
                '}';
    }
}
