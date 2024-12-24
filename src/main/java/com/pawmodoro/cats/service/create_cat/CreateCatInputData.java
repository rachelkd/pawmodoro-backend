package com.pawmodoro.cats.service.create_cat;

/**
 * Input data for the Create Cat use case.
 * Contains the username of the owner, cat name, and optional image file name.
 */
public class CreateCatInputData {
    private final String ownerUsername;
    private final String catName;
    private final String imageFileName;

    /**
     * Creates input data for creating a cat with a specific image.
     * @param ownerUsername the username of the cat's owner
     * @param catName the name of the cat
     * @param imageFileName the name of the image file (can be null)
     */
    public CreateCatInputData(
        final String catName,
        final String ownerUsername,
        final String imageFileName) {
        this.catName = catName;
        this.ownerUsername = ownerUsername;
        this.imageFileName = imageFileName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getCatName() {
        return catName;
    }

    public String getImageFileName() {
        return imageFileName;
    }
}
