package org.example.DataBaseComponent.IndexComponent;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@JsonTypeName("HashIndexProperty")

public class HashIndexIndexProperty implements IndexProperty {
    private String propertyName;
    private Map<Integer, List<Reference>> references;

    public HashIndexIndexProperty(String propertyName) {
        this.propertyName = propertyName;
        references = new Hashtable<>();
    }

    public HashIndexIndexProperty() {
    }


    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Map<Integer, List<Reference>> getReferences() {
        return references;
    }

    @Override
    public void setReferences(Map<Integer, List<Reference>> references) {
        this.references = references;
    }

    @Override
    public String toString() {
        return "PropertyV1{" +
                "propertyName='" + propertyName + '\'' +
                ", references=" + references +
                '}';
    }
}
