package com.pawmodoro.cats.service.create_cat;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatAlreadyExistsException;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Data access interface for the Create Cats use case.
 * Defines the contract for persisting cat entities.
 */
public interface CreateCatDataAccessInterface {
    /**
     * Saves a cat entity to the database.
     * @param cat the cat entity to save
     * @return the saved cat entity
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws CatAlreadyExistsException if the cat already exists
     */
    Cat saveCat(Cat cat) throws DatabaseAccessException, CatAlreadyExistsException;

    /**
     * Checks if a cat exists by name and owner username.
     * @param catName the name of the cat
     * @param ownerUsername the username of the owner
     * @return true if the cat exists, false otherwise
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    boolean catExistsByNameAndOwnerUsername(String catName, String ownerUsername) throws DatabaseAccessException;
}
