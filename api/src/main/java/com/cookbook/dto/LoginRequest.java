package com.cookbook.dto;

import jakarta.validation.constraints.NotBlank;

/** What a client sends to log in: {"username":"ada","password":"mypass123"} */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
