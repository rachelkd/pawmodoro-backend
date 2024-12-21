package com.pawmodoro.users.service.logout.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.users.service.logout.LogoutInputBoundary;

/**
 * Controller handling user logout requests.
 */
@RestController
@RequestMapping("/api/users/logout")
public class LogoutController {
    private final LogoutInputBoundary logoutInteractor;

    public LogoutController(LogoutInputBoundary logoutInteractor) {
        this.logoutInteractor = logoutInteractor;
    }

    /**
     * Handles POST requests for user logout.
     * @param token the authentication token from the Authorization header
     */
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if present
        logoutInteractor.execute(token.replace("Bearer ", ""));
    }
}
