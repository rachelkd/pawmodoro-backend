package com.pawmodoro.cats.service.delete_cat;

import com.pawmodoro.core.DatabaseAccessException;

/**
 * Data access interface for the Delete Cat use case.
 * Defines the contract for deleting a cat from the database.
 */
public interface DeleteCatDataAccessInterface {
    /**
     * Deletes a cat from the database.
     * @param catName the name of the cat to delete
     * @param ownerUsername the username of the cat's owner
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    void deleteCat(String catName, String ownerUsername) throws DatabaseAccessException;
}
