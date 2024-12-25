package com.pawmodoro.cats.service.update_cat_happiness;

/**
 * Input data for the Update Cat Happiness use case.
 * Contains the cat name, owner username, and the amount to change happiness by.
 */
public class UpdateCatHappinessInputData {
    private final String catName;
    private final String ownerUsername;
    private final int changeAmount;

    /**
     * Creates input data for updating a cat's happiness level.
     * @param catName the name of the cat
     * @param ownerUsername the username of the cat's owner
     * @param changeAmount the amount to change the happiness level by (positive or negative)
     */
    public UpdateCatHappinessInputData(
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
