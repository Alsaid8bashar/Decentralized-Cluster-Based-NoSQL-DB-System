package org.example.DataBaseComponent.IndexComponent;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type") @JsonSubTypes({

        @JsonSubTypes.Type(value = HashIndexReference.class, name = "HashIndexReference"),
})
public interface Reference extends Serializable {
     String getReference();
}
