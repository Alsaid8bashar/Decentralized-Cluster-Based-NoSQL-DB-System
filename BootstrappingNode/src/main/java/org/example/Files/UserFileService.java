package org.example.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.example.Model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class UserFileService {

    public boolean insertUser(User user, String path) {
        if (user == null)
            throw new NullPointerException();
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr;
        try {
            jsonStr = mapper.writeValueAsString(user);
            FileUtils.writeStringToFile(new File(path), jsonStr, "UTF-8");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User readUser(Path path) {
        ObjectMapper mapper = new ObjectMapper();
        User obj = null;
        try {
            File file = new File(path.toString());
            if (file.exists()) {
                obj = mapper.readValue(new File(path.toString()), User.class);
            }
            System.out.println(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}

