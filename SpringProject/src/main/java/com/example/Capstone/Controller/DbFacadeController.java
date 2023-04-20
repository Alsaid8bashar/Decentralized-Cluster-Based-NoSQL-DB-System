package com.example.Capstone.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.example.Facade.DataBaseFacade;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
public class DbFacadeController<T> {
    final HttpSession session;

    public DbFacadeController(HttpSession session) {
        this.session = session;
    }

    public DataBaseFacade<Object> getDb() {
        if (session.getAttribute("userDb") == null)
            throw new IllegalArgumentException("No user found");
        return (DataBaseFacade<Object>) session.getAttribute("userDb");
    }

    @PostMapping("/createDatabase")
    public ResponseEntity<String> createDatabase(@RequestParam String name) {
        if (getDb().createDataBase(name)) {
            return ResponseEntity.ok("Database created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating Database with name: " + name);
        }
    }

    @PostMapping("/useDatabase")
    public ResponseEntity<String> useDatabase(@RequestParam String name) {
        if (getDb().useDb(name)) {
            return ResponseEntity.ok("The current Db is " + name);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while using Database with name: " + name);
        }
    }

    @PostMapping("/createCollection")
    public ResponseEntity<String> createCollection(@RequestParam String name) {
        if (getDb().createCollection(name)) {
            return ResponseEntity.ok("Collection created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating Collection with name: " + name);
        }
    }

    @PostMapping("/createSchema")
    public ResponseEntity<String> createSchema(@RequestParam String collName, @RequestBody Object jsonObject) {
        Gson gson = new Gson();
        JSONObject jsonObject1 = new JSONObject(gson.toJson(jsonObject));
        if (getDb().createSchema(collName, jsonObject1)) {
            return ResponseEntity.ok("Schema created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating Schema");
        }
    }

    @CacheEvict(value = "readQuery", allEntries = true)
    @PostMapping("/insert")
    public ResponseEntity<String> insert(@RequestParam String collName, @RequestBody java.util.List<Object> data) {
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        for (Object o : data) {
            jsonArray.put(new JSONObject(gson.toJson(o)));
        }
        if (getDb().insertToCollection(collName, jsonArray)) {
            return ResponseEntity.ok("Done");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while inserting data");
        }
    }

    @PostMapping("/createIndex")
    public ResponseEntity<String> createIndex(@RequestParam String collName, @RequestParam String name) {
        if (getDb().createIndex(collName, name)) {
            return ResponseEntity.ok("Done");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating Index");
        }
    }

    @Cacheable("readQuery")
    @GetMapping("/find")
    public ResponseEntity<String> find(@RequestParam String collName, @RequestParam String property, @RequestParam T value) throws JsonProcessingException {
        int number;
        JSONArray jsonArray;
        try {
            number = Integer.parseInt((String) value);
            jsonArray = getDb().find(collName, property, number);
        } catch (NumberFormatException e) {
            jsonArray = getDb().find(collName, property, value);
        }
        return getStringResponseEntity(jsonArray);
    }

    @Cacheable("readQuery")
    @GetMapping("/readAll")
    public ResponseEntity<String> readAll(@RequestParam String collName) throws JsonProcessingException {
        JSONArray jsonArray;
        jsonArray = getDb().readALl(collName);
        return getStringResponseEntity(jsonArray);
    }

    private ResponseEntity<String> getStringResponseEntity(JSONArray jsonArray) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> jsonList;
        try {
            jsonList = objectMapper.readValue(jsonArray.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok().headers(headers).body(objectMapper.writeValueAsString(jsonList));
    }

    @CacheEvict(value = "readQuery", allEntries = true)
    @PostMapping("/deleteDocument")
    public void deleteDocument(@RequestParam String collName, @RequestParam String property, @RequestParam Object value) {
        int number;
        try {
            number = Integer.parseInt((String) value);
            getDb().deleteDocument(collName, property, number);

        } catch (NumberFormatException e) {
            getDb().deleteDocument(collName, property, value);
        }
    }


    @CacheEvict(value = "readQuery", allEntries = true)
    @PostMapping("/updateDocument")
    public void updateDocument(@RequestParam String collName, @RequestParam String docId, @RequestParam String property, @RequestParam Object value) {
        int number;
        try {
            number = Integer.parseInt((String) value);
            getDb().updateDocument(collName, docId, property, number);
        } catch (NumberFormatException e) {
            getDb().updateDocument(collName, docId, property, value);
        }
    }


    @CacheEvict(value = "readQuery", allEntries = true)
    @PostMapping("/deleteDatabase")
    public ResponseEntity<String> deleteDatabase(@RequestParam String databaseName) {
        if (getDb().deleteDb(databaseName)) {
            return ResponseEntity.ok("Database deleted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while trying to delete the database");
        }
    }

    @CacheEvict(value = "readQuery", allEntries = true)
    @PostMapping("/deleteCollection")
    public ResponseEntity<String> deleteCollection(@RequestParam String collName) {
        if (getDb().deleteCollection(collName)) {
            return ResponseEntity.ok("collection  deleted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while trying to delete the collection");
        }
    }


}
