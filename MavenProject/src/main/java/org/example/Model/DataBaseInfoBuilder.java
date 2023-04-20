package org.example.Model;

public class DataBaseInfoBuilder {
    private int userId;
    private String dbName;
    private String collName;
    private String propertyName;
    private int affinityNode;

    public DataBaseInfoBuilder setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public DataBaseInfoBuilder setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public DataBaseInfoBuilder setCollName(String collName) {
        this.collName = collName;
        return this;
    }

    public DataBaseInfoBuilder setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public DataBaseInfoBuilder setAffinityNode(int affinityNode) {
        this.affinityNode = affinityNode;
        return this;
    }

    public DataBaseInfo createDataBaseInfo() {
        return new DataBaseInfo(userId, dbName, collName, propertyName, affinityNode);
    }
}