package org.example.Model;

import java.io.Serializable;

public class DataBaseInfo implements Serializable {

    private int userId;
    private String dbName;
    private String collName;
    private String propertyName;
    private int affinityNode;

    public DataBaseInfo(int userId, String dbName, String collName, String propertyName, int affinityNode) {
        this.userId = userId;
        this.dbName = dbName;
        this.collName = collName;
        this.propertyName = propertyName;
        this.affinityNode = affinityNode;
    }

    public DataBaseInfo() {
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getAffinityNode() {
        return affinityNode;
    }

    public void setAffinityNode(int affinityNode) {
        this.affinityNode = affinityNode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getCollName() {
        return collName;
    }

    public void setCollName(String collName) {
        this.collName = collName;
    }

    @Override
    public String toString() {
        return "DataBaseInfo{" +
                "userId=" + userId +
                ", dbName='" + dbName + '\'' +
                ", collName='" + collName + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", affinityNode=" + affinityNode +
                '}';
    }
}
