package com.pawmodoro.cats.service.update_cat_hunger.interface_adapter;

/**
 * Data Transfer Object for the update cat hunger response.
 * Contains the updated cat's information.
 * @param catName the name of the cat
 * @param ownerUsername the username of the cat's owner
 * @param happinessLevel the current happiness level
 * @param hungerLevel the new hunger level
 * @param imageFileName the name of the image file
 */
public record UpdateCatHungerResponseDto(
    String catName,
    String ownerUsername,
    int happinessLevel,
    int hungerLevel,
    String imageFileName) {

}
