package com.pawmodoro.cats.service.update_cats_after_study.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.cats.service.update_cats_after_study.UpdateCatsAfterStudyInputBoundary;
import com.pawmodoro.cats.service.update_cats_after_study.UpdateCatsAfterStudyInputData;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Controller for handling requests to update cats after study completion.
 */
@RestController
@RequestMapping("/api/cats")
public class UpdateCatsAfterStudyController {
    private final UpdateCatsAfterStudyInputBoundary updateCatsInteractor;

    public UpdateCatsAfterStudyController(UpdateCatsAfterStudyInputBoundary updateCatsInteractor) {
        this.updateCatsInteractor = updateCatsInteractor;
    }

    /**
     * Updates cats' status after a study session is completed.
     * @param authHeader The Authorization header containing the JWT token
     * @return UpdateCatsAfterStudyResponseDto containing the updated cats' status
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PostMapping("/update-after-study")
    @ResponseStatus(HttpStatus.OK)
    public UpdateCatsAfterStudyResponseDto updateCatsAfterStudy(
        @RequestHeader("Authorization") String authHeader) throws DatabaseAccessException {

        // Extract token from Authorization header
        final String token = authHeader.replace("Bearer ", "");

        return updateCatsInteractor.execute(new UpdateCatsAfterStudyInputData(token));
    }
}
