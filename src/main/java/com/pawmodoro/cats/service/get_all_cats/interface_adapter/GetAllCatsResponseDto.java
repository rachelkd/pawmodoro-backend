package com.pawmodoro.cats.service.get_all_cats.interface_adapter;

import java.util.List;
import com.pawmodoro.cats.interface_adapter.CatDto;

/**
 * Data Transfer Object for get all cats response.
 * This record encapsulates the data that will be sent back to the client.
 * @param success whether the operation was successful
 * @param cats list of cats (null if operation failed)
 * @param message success or error message
 */
public record GetAllCatsResponseDto(
    boolean success,
    List<CatDto> cats,
    String message) {}
