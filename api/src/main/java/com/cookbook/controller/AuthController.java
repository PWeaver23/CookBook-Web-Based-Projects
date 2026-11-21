package com.cookbook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * POST /api/auth/register   anyone   201 {"id":1,"username":"ada","role":"USER"} · 400 · 409
 * POST /api/auth/login      anyone   200 {"token":"eyJ...","expiresInMinutes":60,"username":"ada","role":"USER"} · 401
 * GET  /api/auth/me         token    200 who the token belongs to · 401
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

}
