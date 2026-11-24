package com.cookbook.controller;

import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;


import com.cookbook.data.UserRepository;
import com.cookbook.dto.LoginRequest;
import com.cookbook.dto.LoginResponse;
import com.cookbook.dto.RegisterRequest;
import com.cookbook.dto.UserResponse;
import com.cookbook.model.Account;
import com.cookbook.services.TokenService;
import com.cookbook.services.SequenceService;

import jakarta.validation.Valid;

/**
 
POST /api/auth/register   anyone   201 {"id":1,"username":"ada","role":"USER"} · 400 · 409
POST /api/auth/login      anyone   200 {"token":"eyJ...","expiresInMinutes":60,"username":"ada","role":"USER"} · 401
GET  /api/auth/me         token    200 who the token belongs to · 401*/
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokens;
    private final SequenceService sequenceService;

    public AuthController(UserRepository users, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, TokenService tokens, SequenceService sequenceService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.sequenceService = sequenceService;
        this.tokens = tokens;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        if (users.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "The username " + request.username() + " is taken");
        }
        String hash = passwordEncoder.encode(request.password());
        Account account = new Account(request.username(), hash, "USER");
        account.setId(sequenceService.nextAccountId());
        users.save(account);
        return UserResponse.from(account);
    }

    /** NEW: check the password ONCE, here, and hand back a signed token. */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication checked;
        try {
            checked = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
        } catch (BadCredentialsException wrong) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        }
        Account user = users.findByUsername(checked.getName()).orElseThrow();
        return new LoginResponse(tokens.issue(checked), tokens.minutesValid(), user.getUsername(), user.getRole());
    }

    /** caller.getName() is now the token's "sub": the username. */
    @GetMapping("/me")
    public UserResponse me(Authentication caller) {
        Account user = users.findByUsername(caller.getName()).orElseThrow();
        return UserResponse.from(user);
    }


    @GetMapping("name/{id}")
    public String getUser(@PathVariable Long id) {
        return users.findUsernameById(id);
    }
}
