package org.example.DataBaseComponent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.DataBaseComponent.Collection.NoSqlCollection;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NoSqlDataBase.class, name = "NoSqlDataBase"),
})
public interface DataBase extends Serializable {

    /**
     * Returns the NoSqlCollection with the specified name.
     *
     * @param collName the name of the collection to retrieve
     * @return the NoSqlCollection with the specified name
     */
    NoSqlCollection getCollection(String collName);



    /**
     * Creates a new NoSqlCollection with the specified name.
     *
     * @param collName the name of the new collection to create
     * @return true if the collection was successfully created, false otherwise
     */
    boolean createCollection(String collName);


    /**
     * Deletes the NoSqlCollection with the specified name.
     *
     * @param collName the name of the collection to delete
     * @return true if the collection was successfully deleted, false otherwise
     */
    boolean deleteCollection(String collName);


    /**
     * Returns the name of the database.
     *
     * @return the name of the database
     */
    String getDbname();

    Integer getUserId();
}
