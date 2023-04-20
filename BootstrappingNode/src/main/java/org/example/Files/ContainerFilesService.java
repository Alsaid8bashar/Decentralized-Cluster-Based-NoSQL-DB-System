package org.example.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.example.Cluster.Container;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ContainerFilesService {

    public Container read(Path path) {
        ObjectMapper mapper = new ObjectMapper();
        Container obj = null;
        try {
            File file = new File(path.toString());
            if (file.exists()) {
                obj = mapper.readValue(new File(path.toString()), Container.class);
            }
            System.out.println(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public boolean insertContainer(Container container, String path) {
        if (container == null)
            throw new NullPointerException();
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr;
        try {
            jsonStr = mapper.writeValueAsString(container);
            FileUtils.writeStringToFile(new File(path), jsonStr, "UTF-8");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
