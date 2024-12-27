package com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter;

import com.pawmodoro.cats.interface_adapter.CatDto;

/**
 * Data Transfer Object for the decrease cat stats on skip response.
 * Contains the updated cat's information and deletion status.
 * @param updatedCat the updated cat information (null if no cats or cat was deleted)
 * @param isDeleted whether the cat was deleted due to low stats
 * @param message a message describing what happened
 */
public record DecreaseCatStatsOnSkipResponseDto(
    CatDto updatedCat,
    boolean isDeleted,
    String message) {

}
