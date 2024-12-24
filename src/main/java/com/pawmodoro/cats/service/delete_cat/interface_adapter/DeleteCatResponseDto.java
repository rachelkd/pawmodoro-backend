package com.pawmodoro.cats.service.delete_cat.interface_adapter;

/**
 * Response DTO for the Delete Cat use case.
 * @param message the message to be displayed to the user
 * @param deletedCatName the name of the cat that was deleted
 */
public record DeleteCatResponseDto(String message, String deletedCatName) {

}
