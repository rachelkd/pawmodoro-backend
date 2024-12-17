package com.pawmodoro.cats.service.get_all_cats.interface_adapter;

import java.util.Collection;
import com.pawmodoro.cats.entity.Cat;

/**
 * Data Transfer Object for get all cats response.
 * This record encapsulates the data that will be sent back to the client.
 * @param success whether the operation was successful
 * @param cats collection of cats (null if operation failed)
 * @param message success or error message
 */
public record GetAllCatsResponseDTO(
    boolean success,
    Collection<Cat> cats,
    String message) {}
