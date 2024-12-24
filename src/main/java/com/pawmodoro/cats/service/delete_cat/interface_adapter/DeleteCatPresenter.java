package com.pawmodoro.cats.service.delete_cat.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.service.delete_cat.DeleteCatOutputBoundary;
import com.pawmodoro.cats.service.delete_cat.DeleteCatOutputData;

/**
 * Presenter for the Delete Cat use case.
 */
@Component
public class DeleteCatPresenter implements DeleteCatOutputBoundary {
    @Override
    public DeleteCatResponseDto prepareResponse(DeleteCatOutputData outputData) {
        return new DeleteCatResponseDto(outputData.getMessage(), outputData.getCatName());
    }
}
