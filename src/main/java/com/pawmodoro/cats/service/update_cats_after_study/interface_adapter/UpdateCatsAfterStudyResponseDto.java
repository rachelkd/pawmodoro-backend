package com.pawmodoro.cats.service.update_cats_after_study.interface_adapter;

import java.util.List;
import com.pawmodoro.cats.interface_adapter.CatDto;

/**
 * DTO for the response after updating cats' happiness levels
 * @param updatedCats List of updated cats with their new stats
 * @param failures List of error messages for cats that failed to update
 */
public record UpdateCatsAfterStudyResponseDto(
    List<CatDto> updatedCats,
    List<String> failures) {}
