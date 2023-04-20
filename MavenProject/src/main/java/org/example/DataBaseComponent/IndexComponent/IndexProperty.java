package org.example.DataBaseComponent.IndexComponent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type") @JsonSubTypes({

        @JsonSubTypes.Type(value = HashIndexIndexProperty.class, name = "HashIndexProperty"),
})
public interface IndexProperty extends Serializable {
     Map<Integer, List<Reference>> getReferences();
     void setReferences(Map<Integer, List<Reference>> references);
}
