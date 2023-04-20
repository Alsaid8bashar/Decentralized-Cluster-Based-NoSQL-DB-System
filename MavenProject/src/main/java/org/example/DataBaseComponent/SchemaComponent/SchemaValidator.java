package org.example.DataBaseComponent.SchemaComponent;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;

public class SchemaValidator {
    private SchemaValidator() {
    }

    private static boolean isValidObject(JSONObject schemaObj, JSONObject jsonObject) {
        Schema schema = SchemaLoader.load(schemaObj);
        try {
            schema.validate(jsonObject);
            return true;
        } catch (ValidationException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public static JSONArray isValid(JSONObject schemaObj, JSONArray jsonArray) {
        JSONArray jsonArray1 = new JSONArray();
        for (Object object : jsonArray) {
            JSONObject temp = (JSONObject) object;
            if (isValidObject(schemaObj, temp))
                jsonArray1.put(temp);
        }
        return jsonArray1;
    }
}
