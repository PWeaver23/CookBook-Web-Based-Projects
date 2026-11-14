package com.cookbook.controller;

import com.cookbook.data.AccountStore;
import com.cookbook.model.Account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

//API endpoint for accounts, currently using in-memory storage. Will be updated to use database once database is hooked up.
//Supports HTTP GET by id and POST requests for accounts.
@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private final AccountStore accounts;

    public AccountController(AccountStore accounts) {
        this.accounts = accounts;
    }

    // Represents the request body for creating a new account, with validation annotations to ensure required fields are present and valid.
    public record AccountRequest(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Password is required") String password
    ) { }

    // GET /api/accounts/{id} returns the account with that id, or 404 if not found
    @GetMapping("/{id}")
    public ResponseEntity<Account> getOne(@PathVariable int id) {
        return ResponseEntity.of(accounts.findById(id));
    }

    // POST /api/accounts creates a new account, returning 201 Created with a Location header pointing at the new entry
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AccountRequest request) {
        Account created = accounts.add(request.email(), request.password());
        return ResponseEntity.created(URI.create("/api/accounts/" + created.getId())).body(created);
    }
}
