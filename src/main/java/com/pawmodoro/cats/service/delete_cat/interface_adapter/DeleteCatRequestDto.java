package com.pawmodoro.cats.service.delete_cat.interface_adapter;

import com.pawmodoro.constants.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for the delete cat request.
 * Contains the name of the cat to delete.
 * @param name the name of the cat to delete (required, letters only)
 */
public record DeleteCatRequestDto(
    @NotBlank(message = "Cat name is required") @Size(min = 1, max = Constants.ValidationConstants.MAX_CAT_NAME_LENGTH,
        message = "Cat name must be between 1 and " + Constants.ValidationConstants.MAX_CAT_NAME_LENGTH
            + " characters")
    @Pattern(regexp = Constants.ValidationConstants.LETTERS_ONLY_PATTERN, 
            message = "Cat name must contain only letters")
    String name) {

}
