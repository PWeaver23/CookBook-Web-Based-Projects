package com.cookbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookbook.data.UserRepository;
import com.cookbook.model.Account;

/**
 
POST /api/auth/register   anyone   201 {"id":1,"username":"ada","role":"USER"} · 400 · 409
POST /api/auth/login      anyone   200 {"token":"eyJ...","expiresInMinutes":60,"username":"ada","role":"USER"} · 401
GET  /api/auth/me         token    200 who the token belongs to · 401*/
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;

    public AuthController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("name/{id}")
    public String getUser(@PathVariable Long id) {
        return users.findUsernameById(id);
    }
}
// Account account = new Account(username, hash, role, new HashSet<>());
// account.setId(sequenceService.nextAccountId());
// accountRepository.save(account);