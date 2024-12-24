package com.pawmodoro.cats.service.create_cat.interface_adapter;

import com.pawmodoro.constants.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for the create cat request.
 * Contains the cat's name and an optional image file name.
 * If the image file name is null or empty, a random image will be assigned.
 * @param name the name of the cat (required, letters only)
 * @param imageFileName the name of the image file (optional, must be in format "cat-[1-5].png")
 */
public record CreateCatRequestDto(
    @NotBlank(message = "Cat name is required") @Size(min = 1, max = Constants.ValidationConstants.MAX_CAT_NAME_LENGTH,
        message = "Cat name must be between 1 and " + Constants.ValidationConstants.MAX_CAT_NAME_LENGTH
            + " characters") @Pattern(regexp = Constants.ValidationConstants.LETTERS_ONLY_PATTERN,
                message = "Cat name must contain only letters") String name,

    @Pattern(regexp = Constants.ValidationConstants.CAT_IMAGE_PATTERN,
        message = "Image file name must be in format 'cat-[1-5].png'",
        flags = Pattern.Flag.CASE_INSENSITIVE) String imageFileName) {

}
