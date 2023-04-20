package org.example.DataBaseComponent.Collection;

public interface Collection<T> {
    /**
     * Inserts the specified data into the database.
     *
     * @param data the data to insert
     * @return true if the data was successfully inserted, false otherwise
     */
    boolean insert(Object data);
    /**
     * Reads the collection data
     *
     * @return the collection data
     */
    T read();
}
