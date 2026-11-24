package com.cookbook.dto;

import com.cookbook.model.Account;

/** A user as the API shows it: {"id":1,"username":"ada","role":"USER"}. Never the hash. */
public record UserResponse(long id, String username, String role) {

    public static UserResponse from(Account user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
