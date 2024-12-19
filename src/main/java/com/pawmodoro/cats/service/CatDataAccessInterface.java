package com.pawmodoro.cats.service;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.NoCatsFoundException;

/**
 * Interface for accessing cat data in the data store.
 * This interface defines the contract for how the application interacts with stored cat data.
 */
public interface CatDataAccessInterface {

    /**
     * Saves a cat to the data store.
     * @param cat the cat to save
     * @return true if save was successful, false otherwise
     */
    boolean saveCat(Cat cat);

    /**
     * Retrieves a cat by its name and owner username.
     * @param name the name of the cat
     * @param ownerUsername the username of the cat's owner
     * @return the cat if found, null otherwise
     */
    Cat getCatByNameAndOwner(String name, String ownerUsername);

    /**
     * Updates an existing cat's data.
     * @param cat the cat with updated data
     * @return true if update was successful, false otherwise
     */
    boolean updateCat(Cat cat);

    /**
     * Checks if a cat name is already taken for a specific owner.
     * @param name the cat name to check
     * @param ownerUsername the username of the owner
     * @return true if name is taken, false otherwise
     */
    boolean existsByNameAndOwner(String name, String ownerUsername);

    /**
     * Gets the number of cats owned by a user.
     * @param ownerUsername the username of the owner
     * @return the number of cats owned by the user
     */
    int getNumberOfCatsByOwner(String ownerUsername);

    /**
     * Removes a cat from a user's ownership (e.g., when cat runs away).
     * @param name the name of the cat to remove
     * @param ownerUsername the username of the owner
     * @return true if removal was successful, false if cat wasn't found
     */
    boolean removeCat(String name, String ownerUsername);

    /**
     * Get the hunger level of a cat.
     * @param name the name of the cat
     * @param ownerUsername the username of the cat's owner
     * @return the hunger level of the cat
     * @throws NoCatsFoundException if the cat is not found
     */
    int getHungerLevel(String name, String ownerUsername);

    /**
     * Get the happiness level of a cat.
     * @param name the name of the cat
     * @param ownerUsername the username of the cat's owner
     * @return the happiness level of the cat
     * @throws NoCatsFoundException if the cat is not found
     */
    int getHappinessLevel(String name, String ownerUsername);
}
