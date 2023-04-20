package org.example.DataBaseComponent.IndexComponent;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("HashIndexReference")

public class HashIndexReference implements Reference {
    private String reference;

    public HashIndexReference(String start) {
        this.reference = start;
    }
    public HashIndexReference() {
    }

    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "ReferenceV1{" +
                "index='" + reference + '\'' +
                '}';
    }
}
