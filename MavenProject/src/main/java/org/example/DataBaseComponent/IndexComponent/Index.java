package org.example.DataBaseComponent.IndexComponent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.json.JSONArray;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type") @JsonSubTypes({

        @JsonSubTypes.Type(value = HashIndex.class, name = "HashIndex"),
})
public interface Index<T> extends Serializable {
    boolean createIndex();
    String getPropertyName();
    void updateAfterDelete(JSONArray jsonArray);
    void updateAfterInsert(JSONArray jsonArray);
}
