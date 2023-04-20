package org.example.DataBaseComponent.SchemaComponent;

import org.json.JSONArray;
import org.json.JSONObject;
@SuppressWarnings("VulnerableCodeUsages")
public class SchemaBuilder {
    private final JSONObject properties;
    private final JSONObject schema;
    private final JSONArray requiredValue;

    public SchemaBuilder() {
        properties = new JSONObject();
        requiredValue = new JSONArray();
        schema = new JSONObject();
        schema.put("required", requiredValue);
        schema.put("properties", properties);
    }

    public SchemaBuilder addTitle(String value) {
        schema.put("title", value);
        return this;
    }

    public SchemaBuilder addDescription(String value) {
        schema.put("description", value);
        return this;

    }

    public SchemaBuilder addType(String value) {
        schema.put("type", value);
        return this;

    }

    public SchemaBuilder addProperty(String key, String type) {
        properties.put( key,new JSONObject().put("type", type));
        return this;
    }

    public SchemaBuilder addRequiredValue(String key) {
        requiredValue.put(key);
        return this;
    }

    public SchemaBuilder doNotAllowAdditionalProperties() {
        schema.put("additionalProperties", false);
        return this;
    }

    public SchemaBuilder allowAdditionalProperties() {
        schema.put("additionalProperties", true);
        return this;
    }

    public JSONObject build() {
        return schema;
    }


}
