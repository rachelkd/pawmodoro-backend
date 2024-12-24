package com.pawmodoro.cats.service.create_cat.interface_adapter;

/**
 * Data Transfer Object for the create cat response.
 * Contains the newly created cat.
 * @param catName the name of the cat
 * @param ownerUsername the username of the cat's owner
 * @param imageFileName the name of the image file (can be null)
 */
public record CreateCatResponseDto(String catName, String ownerUsername, String imageFileName) {

}
