package com.pawmodoro.cats.service.update_cat_happiness.interface_adapter;

/**
 * Data Transfer Object for the update cat happiness response.
 * Contains the updated cat's information.
 * @param catName the name of the cat
 * @param ownerUsername the username of the cat's owner
 * @param happinessLevel the new happiness level
 * @param hungerLevel the current hunger level
 * @param imageFileName the name of the image file
 */
public record UpdateCatHappinessResponseDto(
    String catName,
    String ownerUsername,
    int happinessLevel,
    int hungerLevel,
    String imageFileName) {

}
