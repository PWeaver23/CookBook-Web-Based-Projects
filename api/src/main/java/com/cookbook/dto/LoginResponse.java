package com.cookbook.dto;

/** {"token":"eyJ...","expiresInMinutes":60,"username":"ada","role":"USER"} */
public record LoginResponse(String token, long expiresInMinutes, String username, String role) {
}
