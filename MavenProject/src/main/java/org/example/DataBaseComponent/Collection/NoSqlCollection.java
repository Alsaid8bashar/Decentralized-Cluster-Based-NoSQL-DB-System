package org.example.DataBaseComponent.Collection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.DataBaseComponent.IAffinity;
import org.json.JSONObject;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NoSqlCollectionImp.class, name = "NoSqlCollectionImp"),
})
public interface NoSqlCollection<T> extends Collection, IQuery, IAffinity, Serializable {

    /**
     * Updates the specified property of a document with the given ID to the specified value.
     *
     * @param id       the ID of the document to update
     * @param property the property of the document to update
     * @param value    the new value for the property
     */
    boolean updateDocument(String id, String property, T value);

    boolean deleteDocument(String property,Object value);

    /**
     * Searches for documents that have the specified property and value.
     *
     * @param property the property to search for
     * @param value    the value to search for
     * @return an object representing the search results
     */
    Object find(String property, T value);

    /**
     * Creates a new schema using the specified JSON object.
     *
     * @param jsonObject the JSON object containing the schema definition
     * @return true if the schema was successfully created, false otherwise
     */
    boolean createSchema(JSONObject jsonObject);


}
