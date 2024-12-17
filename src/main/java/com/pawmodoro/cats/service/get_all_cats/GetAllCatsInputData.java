package com.pawmodoro.cats.service.get_all_cats;

/**
 * Input data for the Get All Cats use case.
 * Contains the username of the owner whose cats we want to retrieve.
 */
public class GetAllCatsInputData {
    private final String ownerUsername;

    public GetAllCatsInputData(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }
}
