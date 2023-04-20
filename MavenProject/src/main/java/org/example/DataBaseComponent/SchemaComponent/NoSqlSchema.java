package org.example.DataBaseComponent.SchemaComponent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.DataBaseComponent.Collection.NoSqlCollectionImp;
import org.example.DataBaseComponent.IAffinity;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NoSqlCollectionImp.class, name = "NoSqlSchemaImpl"),
})
public interface NoSqlSchema extends Schema, IAffinity, Serializable {


}
