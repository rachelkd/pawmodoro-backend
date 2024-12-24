package com.pawmodoro.cats.service.create_cat;

import com.pawmodoro.cats.entity.Cat;
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
     */
    Cat saveCat(Cat cat) throws DatabaseAccessException;
}
