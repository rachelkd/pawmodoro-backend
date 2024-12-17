package com.pawmodoro.cats.entity;

import org.springframework.stereotype.Component;

/**
 * Factory for creating Cat entities.
 */
@Component
public class CatFactory {

    /**
     * Creates a new Cat with the given name and owner username.
     * @param name The name for the cat
     * @param ownerUsername The username of the cat's owner
     * @return A new Cat instance
     */
    public Cat create(String name, String ownerUsername) {
        return new Cat(name, ownerUsername);
    }

    /**
     * Creates a new Cat with the given name, owner username, happiness level, hunger level, and image file name.
     * @param name The name for the cat
     * @param ownerUsername The username of the cat's owner
     * @param happinessLevel The happiness level of the cat
     * @param hungerLevel The hunger level of the cat
     * @param imageFileName The image file name of the cat
     * @return A new Cat instance
     */
    public Cat create(String name, String ownerUsername, int happinessLevel, int hungerLevel, String imageFileName) {
        return new Cat(name, ownerUsername, happinessLevel, hungerLevel, imageFileName);
    }
}
