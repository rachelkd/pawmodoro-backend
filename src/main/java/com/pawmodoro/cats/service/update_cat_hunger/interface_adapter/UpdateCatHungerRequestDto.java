package com.pawmodoro.cats.service.update_cat_hunger.interface_adapter;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for the update cat hunger request.
 * Contains the amount to change the hunger level by.
 * @param changeAmount the amount to change the hunger level by (positive or negative)
 */
public record UpdateCatHungerRequestDto(
    @NotNull(message = "Change amount is required") int changeAmount) {

}
