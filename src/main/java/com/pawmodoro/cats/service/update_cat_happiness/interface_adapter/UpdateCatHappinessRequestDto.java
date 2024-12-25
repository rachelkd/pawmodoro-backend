package com.pawmodoro.cats.service.update_cat_happiness.interface_adapter;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for the update cat happiness request.
 * Contains the amount to change the happiness level by.
 * @param changeAmount the amount to change the happiness level by (positive or negative)
 */
public record UpdateCatHappinessRequestDto(
    @NotNull(message = "Change amount is required") int changeAmount) {

}
