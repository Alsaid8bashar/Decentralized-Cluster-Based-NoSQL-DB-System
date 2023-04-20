package org.example.DataBaseComponent.Collection;

import org.example.DataBaseComponent.IndexComponent.Index;

public interface IQuery {

    /**
     * Checks if an index exists for the specified property.
     *
     * @param property the property to check for an index
     * @return true if an index exists, false otherwise
     */
    boolean hasIndex(String property);

    /**
     * Creates a new index for the specified property.
     *
     * @param index the index to create
     * @return true if the index was successfully created, false otherwise
     */
    boolean createIndex(Index index);
}
