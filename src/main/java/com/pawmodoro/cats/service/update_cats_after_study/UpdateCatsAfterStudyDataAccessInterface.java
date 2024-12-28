package com.pawmodoro.cats.service.update_cats_after_study;

import java.util.List;
import java.util.Map;
import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.data_access.CatUpdateDataAccess.CatUpdateResult;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interface for accessing cat data for the update after study use case
 */
public interface UpdateCatsAfterStudyDataAccessInterface {
    /**
     * Gets the username associated with the given authentication token
     * @param token the authentication token
     * @return the username associated with the token
     * @throws AuthenticationException if the token is invalid or expired
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    String getUsernameFromToken(String token) throws DatabaseAccessException;

    /**
     * Gets all cats owned by a user
     * @param ownerUsername the username of the owner
     * @return list of cats owned by the user
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    List<Cat> getCatsByOwner(String ownerUsername) throws DatabaseAccessException;

    /**
     * Updates happiness levels for multiple cats in a single transaction
     * @param catUpdates mapping of cats to new happiness levels
     * @return result containing updated cats and any failures
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    CatUpdateResult updateCatsHappiness(Map<Cat, Integer> catUpdates) throws DatabaseAccessException;
}
