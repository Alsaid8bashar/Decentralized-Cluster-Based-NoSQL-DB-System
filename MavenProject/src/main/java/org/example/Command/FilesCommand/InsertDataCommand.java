package org.example.Command.FilesCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Command.Command;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertDataCommand implements Command<Boolean> {
    private final static Logger logger = Logger.getLogger(Command.class.getName());

    private final String filePath;
    private final Object data;

    public InsertDataCommand(String filePath, Object data) {
        this.filePath = filePath;
        this.data = data;
    }
    public Boolean execute() {
        ObjectMapper mapper = new ObjectMapper();
        if (data == null)
            throw new NullPointerException();
        String jsonStr;
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            jsonStr = mapper.writeValueAsString(data);
            outputStream.write(jsonStr.getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Failed to execute operation",e);
        }
        return false;
    }

    @Override
    public String toString() {
        return "InsertDataCommand{" +
                "filePath='" + filePath + '\'' +
                ", data=" + data +
                '}';
    }
}
