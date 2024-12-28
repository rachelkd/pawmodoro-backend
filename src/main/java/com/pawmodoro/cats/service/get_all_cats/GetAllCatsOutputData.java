package com.pawmodoro.cats.service.get_all_cats;

import java.util.List;

import com.pawmodoro.cats.entity.Cat;

/**
 * Output data for the Get All Cats use case.
 * Contains the list of cats owned by the user and success/error information.
 */
public class GetAllCatsOutputData {
    private final List<Cat> cats;
    private final boolean success;
    private final String errorMessage;

    public GetAllCatsOutputData(List<Cat> cats, boolean success, String errorMessage) {
        this.cats = cats;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
