package com.pawmodoro.cats.service.delete_cat;

import com.pawmodoro.cats.service.delete_cat.interface_adapter.DeleteCatResponseDto;

/**
 * Output boundary for the Delete Cat use case.
 * Defines the contract for presenting the result of deleting a cat.
 */
public interface DeleteCatOutputBoundary {
    /**
     * Prepares the response for a successful cat deletion.
     * @param outputData the output data containing the deleted cat's name
     * @return the response DTO
     */
    DeleteCatResponseDto prepareResponse(DeleteCatOutputData outputData);
}
