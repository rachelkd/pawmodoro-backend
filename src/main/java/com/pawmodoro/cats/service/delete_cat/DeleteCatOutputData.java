package com.pawmodoro.cats.service.delete_cat;

/**
 * Output data for the Delete Cat use case.
 * Contains the name of the deleted cat.
 */
public class DeleteCatOutputData {
    private final String message;
    private final String catName;

    /**
     * Creates output data for the deleted cat.
     * @param message the message to be displayed to the user
     * @param catName the name of the deleted cat
     */
    public DeleteCatOutputData(final String message, final String catName) {
        this.message = message;
        this.catName = catName;
    }

    public String getCatName() {
        return catName;
    }

    public String getMessage() {
        return message;
    }
}
