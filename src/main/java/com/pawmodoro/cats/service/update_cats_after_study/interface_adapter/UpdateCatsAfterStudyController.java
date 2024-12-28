package com.pawmodoro.cats.service.update_cats_after_study.interface_adapter;

import com.pawmodoro.cats.service.update_cats_after_study.UpdateCatsAfterStudyInputBoundary;
import com.pawmodoro.cats.service.update_cats_after_study.UpdateCatsAfterStudyInputData;
import com.pawmodoro.core.DatabaseAccessException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling requests to update cats after study completion
 */
@RestController
@RequestMapping("/api/cats")
public class UpdateCatsAfterStudyController {
    private final UpdateCatsAfterStudyInputBoundary updateCatsInteractor;

    public UpdateCatsAfterStudyController(UpdateCatsAfterStudyInputBoundary updateCatsInteractor) {
        this.updateCatsInteractor = updateCatsInteractor;
    }

    @PostMapping("/update-after-study")
    @ResponseStatus(HttpStatus.OK)
    public UpdateCatsAfterStudyResponseDto updateCatsAfterStudy(
        @RequestHeader("Authorization") String authHeader) throws DatabaseAccessException {

        // Extract token from Authorization header
        String token = authHeader.replace("Bearer ", "");

        return updateCatsInteractor.execute(new UpdateCatsAfterStudyInputData(token));
    }
}
