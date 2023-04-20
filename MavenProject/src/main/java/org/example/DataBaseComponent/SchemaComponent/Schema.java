package org.example.DataBaseComponent.SchemaComponent;

import org.json.JSONObject;

public interface Schema {
    /**
     * Creates a new schema using the specified JSON object.
     *
     * @param jsonObject the JSON object containing the schema definition
     * @return true if the schema was successfully created, false otherwise
     */
    boolean create(JSONObject jsonObject);
}
