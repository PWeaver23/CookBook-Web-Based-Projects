package com.cookbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * What a client sends to register: {"username":"ada","password":"mypass123"}
 * BCrypt only uses the first 72 bytes of a password, so 72 is the maximum.
 */
public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 8, max = 72) String password) {
}
