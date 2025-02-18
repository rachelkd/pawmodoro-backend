package com.pawmodoro.cats.service.get_all_cats;

import java.util.List;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interface for data access operations related to retrieving all cats owned by a user.
 */
public interface GetAllCatsDataAccessInterface {
    /**
     * Retrieves all cats owned by a specific user.
     * @param ownerUsername the username of the owner
     * @return list of cats owned by the user
     * @throws DatabaseAccessException if the database access fails
     */
    List<Cat> getCatsByOwner(String ownerUsername) throws DatabaseAccessException;
}
