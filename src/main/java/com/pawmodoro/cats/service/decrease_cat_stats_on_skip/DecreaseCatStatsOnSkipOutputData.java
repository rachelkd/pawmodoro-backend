package com.pawmodoro.cats.service.decrease_cat_stats_on_skip;

/**
 * Output data for the decrease cat stats on skip use case.
 * Contains the updated cat information and deletion status.
 */
public class DecreaseCatStatsOnSkipOutputData {
    private final String catName;
    private final String ownerUsername;
    private final int happinessLevel;
    private final int hungerLevel;
    private final String imageFileName;
    private final boolean isDeleted;
    private final String message;

    /**
     * Creates a new DecreaseCatStatsOnSkipOutputData.
     * @param catName the name of the cat (null if deleted)
     * @param ownerUsername the username of the cat's owner (null if deleted)
     * @param happinessLevel the happiness level (0 if deleted)
     * @param hungerLevel the hunger level (0 if deleted)
     * @param imageFileName the name of the image file (null if deleted)
     * @param isDeleted whether the cat was deleted
     * @param message a message describing what happened
     */
    public DecreaseCatStatsOnSkipOutputData(
        String catName,
        String ownerUsername,
        int happinessLevel,
        int hungerLevel,
        String imageFileName,
        boolean isDeleted,
        String message) {
        this.catName = catName;
        this.ownerUsername = ownerUsername;
        this.happinessLevel = happinessLevel;
        this.hungerLevel = hungerLevel;
        this.imageFileName = imageFileName;
        this.isDeleted = isDeleted;
        this.message = message;
    }

    public String getCatName() {
        return catName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getMessage() {
        return message;
    }
}
