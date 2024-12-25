package com.pawmodoro.cats.service.update_cat_hunger;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Data access interface for the Update Cat Hunger use case.
 * Defines the contract for updating cat hunger levels.
 */
public interface UpdateCatHungerDataAccessInterface {
    /**
     * Updates a cat's hunger level.
     * @param catName the name of the cat
     * @param ownerUsername the username of the owner
     * @param changeAmount the amount to change the hunger level by
     * @return the updated cat entity
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws AuthenticationException if the authentication token is invalid or missing
     */
    Cat updateHunger(String catName, String ownerUsername, int changeAmount) throws DatabaseAccessException;
}
