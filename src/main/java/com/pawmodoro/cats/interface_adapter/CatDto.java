package com.pawmodoro.cats.interface_adapter;

/**
 * DTO for cat data
 * @param name The name of the cat
 * @param ownerUsername The username of the owner of the cat
 * @param happinessLevel The current happiness level of the cat
 * @param hungerLevel The current hunger level of the cat
 * @param imageFileName The image file name for the cat's avatar
 */
public record CatDto(
    String name,
    String ownerUsername,
    int happinessLevel,
    int hungerLevel,
    String imageFileName) {

}
