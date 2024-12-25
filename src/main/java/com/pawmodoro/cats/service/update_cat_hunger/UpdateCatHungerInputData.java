package com.pawmodoro.cats.service.update_cat_hunger;

/**
 * Input data for the Update Cat Hunger use case.
 * Contains the cat name, owner username, and the amount to change hunger by.
 */
public class UpdateCatHungerInputData {
    private final String catName;
    private final String ownerUsername;
    private final int changeAmount;

    /**
     * Creates input data for updating a cat's hunger level.
     * @param catName the name of the cat
     * @param ownerUsername the username of the cat's owner
     * @param changeAmount the amount to change the hunger level by (positive or negative)
     */
    public UpdateCatHungerInputData(
        final String catName,
        final String ownerUsername,
        final int changeAmount) {
        this.catName = catName;
        this.ownerUsername = ownerUsername;
        this.changeAmount = changeAmount;
    }

    public String getCatName() {
        return catName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public int getChangeAmount() {
        return changeAmount;
    }
}
