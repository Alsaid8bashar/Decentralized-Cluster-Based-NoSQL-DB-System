package org.example.Command.SchemaCommand;

import org.example.Command.Command;
import org.example.DataBaseComponent.SchemaComponent.DataType;
import org.example.DataBaseComponent.SchemaComponent.SchemaBuilder;
import org.json.JSONObject;

public class CreateSchemaCommand implements Command<JSONObject> {

    private final JSONObject exObject;

    public CreateSchemaCommand(JSONObject exObject) {
        this.exObject = exObject;
    }

    @Override
    public JSONObject execute() {
        SchemaBuilder schemaBuilder = new SchemaBuilder();
        for (String key : exObject.keySet()) {
            Class<?> temp = exObject.get(key).getClass();
            if (temp == Number.class || temp == Integer.class || temp == Double.class || temp == Long.class)
                schemaBuilder.addProperty(key, DataType.number.toString());
            else if (temp == String.class) {
                schemaBuilder.addProperty(key, DataType.string.toString());
            } else if (temp == Boolean.class) {
                schemaBuilder.addProperty(key, DataType.string.toString());
            }
        }
        for (String k : exObject.keySet()) {
            schemaBuilder.addRequiredValue(k);
        }
        schemaBuilder.allowAdditionalProperties();
        return  schemaBuilder.build();
    }
}
