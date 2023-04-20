package org.example.Command.FilesCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Command.Command;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadFileCommand implements Command<Object> {
    private final static Logger logger = Logger.getLogger(Command.class.getName());
    private final String path;
    private final Class type;
    public ReadFileCommand(String path, Class type) {
        this.path = path;
        this.type = type;
    }

    @Override
    public Object execute() {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = null;
        File file = new File(path);
        if (file.exists()) {
            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path))) {
                byte[] buffer = new byte[(int) file.length()];
                inputStream.read(buffer);
                obj = mapper.readValue(buffer, type);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to execute operation", e.toString());
            }
        }
        return obj;
    }

    @Override
    public String toString() {
        return "ReadFileCommand{" +
                "path='" + path + '\'' +
                ", type=" + type +
                '}';
    }
}
