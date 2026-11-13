package com.cookbook.data;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.cookbook.model.Account;

// In-memory store for accounts, with a temporary seeder for a test account.
// Will be updated to use database once database is hooked up.
@Component 
public class AccountStore {
    
    private final List<Account> accounts = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    //temporary seeder for a test account
    //remove later once database is hooked up
    public AccountStore() {
        add("test@example.com", "password123");
    }

    // Returns a copy of all accounts in the store.
    public List<Account> findAll() {
        return List.copyOf(accounts);
    }

    /** Optional: either the entry, or "empty" when there is no entry with that id. */
    public Optional<Account> findById(int id) {
        return accounts.stream().filter(account -> account.getId() == id).findFirst();
    }
    
    // Add a new account to the store, returning the created account with its new id.
    public Account add(String email, String password) {
        Account account = new Account(nextId.getAndIncrement(), email, password);
        accounts.add(account);
        return account;
    }
}
