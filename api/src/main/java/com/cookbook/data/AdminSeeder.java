package com.cookbook.data;

import com.cookbook.model.Account;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Registering always makes a USER, so nobody can make themselves an admin.
 * The first admin is created here, once, when the app starts.
 * CommandLineRunner: Spring calls run() right after startup.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;

    public AdminSeeder(UserRepository users, PasswordEncoder passwordEncoder,
                       @Value("${cookbook.admin-password}") String adminPassword) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (!users.existsByUsername("admin")) {
            users.save(new Account(0L, "admin", passwordEncoder.encode(adminPassword), "ADMIN", new HashSet<>()));
        }
    }
}
