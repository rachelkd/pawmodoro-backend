package com.pawmodoro.cats.service.decrease_cat_stats_on_skip;

import java.util.List;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Data access interface for the Decrease Cat Stats On Skip use case.
 * Defines the contract for retrieving and updating cats.
 */
public interface DecreaseCatStatsOnSkipDataAccessInterface {
    /**
     * Gets the username associated with the given authentication token.
     * @param token the authentication token
     * @return the username associated with the token
     * @throws AuthenticationException if the token is invalid or expired
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    String getUsernameFromToken(String token) throws DatabaseAccessException;

    /**
     * Gets all cats owned by a user.
     * @param ownerUsername the username of the owner
     * @return a list of cats owned by the user
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    List<Cat> getCatsByOwner(String ownerUsername) throws DatabaseAccessException;

    /**
     * Updates a cat's hunger level.
     * @param catName the name of the cat
     * @param ownerUsername the username of the owner
     * @param changeAmount the amount to change the hunger level by
     * @return the updated cat entity
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    Cat updateHunger(String catName, String ownerUsername, int changeAmount) throws DatabaseAccessException;

    /**
     * Updates a cat's happiness level.
     * @param catName the name of the cat
     * @param ownerUsername the username of the owner
     * @param changeAmount the amount to change the happiness level by
     * @return the updated cat entity
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    Cat updateHappiness(String catName, String ownerUsername, int changeAmount) throws DatabaseAccessException;

    /**
     * Deletes a cat from the database.
     * @param catName the name of the cat to delete
     * @param ownerUsername the username of the cat's owner
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    void deleteCat(String catName, String ownerUsername) throws DatabaseAccessException;
}
