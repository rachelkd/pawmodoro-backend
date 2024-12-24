package com.pawmodoro.cats.service.delete_cat;

/**
 * Input data for the Delete Cat use case.
 * Contains the name of the cat to delete and the owner's username.
 */
public class DeleteCatInputData {
    private final String catName;
    private final String ownerUsername;

    /**
     * Creates input data for deleting a cat.
     * @param catName the name of the cat to delete
     * @param ownerUsername the username of the cat's owner
     */
    public DeleteCatInputData(final String catName, final String ownerUsername) {
        this.catName = catName;
        this.ownerUsername = ownerUsername;
    }

    public String getCatName() {
        return catName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }
}
