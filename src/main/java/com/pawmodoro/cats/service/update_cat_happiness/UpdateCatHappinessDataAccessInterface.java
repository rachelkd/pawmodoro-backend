package com.pawmodoro.cats.service.update_cat_happiness;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Data access interface for the Update Cat Happiness use case.
 * Defines the contract for updating cat happiness levels.
 */
public interface UpdateCatHappinessDataAccessInterface {
    /**
     * Updates a cat's happiness level.
     * @param catName the name of the cat
     * @param ownerUsername the username of the owner
     * @param changeAmount the amount to change the happiness level by
     * @return the updated cat entity
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws AuthenticationException if the authentication token is invalid or missing
     */
    Cat updateHappiness(String catName, String ownerUsername, int changeAmount) throws DatabaseAccessException;
}
