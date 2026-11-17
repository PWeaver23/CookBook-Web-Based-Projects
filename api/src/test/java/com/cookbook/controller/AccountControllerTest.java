package com.cookbook.controller;

import com.cookbook.data.AccountStore;
import com.cookbook.model.Account;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the 2 account endpoints in AccountController.
 *
 * Works the same way as RecipeControllerTest: @WebMvcTest starts only the web layer,
 * MockMvc sends fake HTTP requests, and @MockitoBean replaces the AccountStore
 * with a mock so each test controls exactly what the "storage" returns.
 */
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    AccountStore accounts;

    // ---------------------------------------------------------------------
    // 1. GET /api/accounts/{id}  (one account)
    // ---------------------------------------------------------------------
    @Nested
    class GetAccountById {

        @Test
        void returnsTheAccountWhenItExists() throws Exception {
            when(accounts.findById(1)).thenReturn(Optional.of(new Account(1, "test@example.com", "password123")));

            mvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
        }

        @Test
        void returns404WhenTheAccountDoesNotExist() throws Exception {
            when(accounts.findById(99)).thenReturn(Optional.empty());

            mvc.perform(get("/api/accounts/99"))
                .andExpect(status().isNotFound());
        }

        @Test
        void returns400WhenTheIdIsNotANumber() throws Exception {
            mvc.perform(get("/api/accounts/abc"))
                .andExpect(status().isBadRequest());

            verify(accounts, never()).findById(anyInt());
        }
    }

    // ---------------------------------------------------------------------
    // 2. POST /api/accounts  (create an account)
    // ---------------------------------------------------------------------
    @Nested
    class CreateAccount {

        @Test
        void createsTheAccountAndReturns201WithLocation() throws Exception {
            when(accounts.add("new@example.com", "secret"))
                .thenReturn(new Account(2, "new@example.com", "secret"));

            mvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"email": "new@example.com", "password": "secret"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/accounts/2"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("new@example.com"));

            verify(accounts).add("new@example.com", "secret");
        }

        @Test
        void returns400WhenEmailIsMissing() throws Exception {
            mvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"password": "secret"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email is required"));

            verify(accounts, never()).add(anyString(), anyString());
        }

        @Test
        void returns400WhenEmailIsBlank() throws Exception {
            mvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"email": "   ", "password": "secret"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email is required"));

            verify(accounts, never()).add(anyString(), anyString());
        }

        @Test
        void returns400WhenPasswordIsMissing() throws Exception {
            mvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"email": "new@example.com"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Password is required"));

            verify(accounts, never()).add(anyString(), anyString());
        }

        @Test
        void returns400ListingBothProblemsWhenEverythingIsMissing() throws Exception {
            mvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Email is required")))
                .andExpect(jsonPath("$.error", containsString("Password is required")));

            verify(accounts, never()).add(anyString(), anyString());
        }

        @Test
        void returns400ForBrokenJson() throws Exception {
            mvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ this is not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The request body isn't valid JSON for this endpoint"));

            verify(accounts, never()).add(anyString(), anyString());
        }
    }
}
