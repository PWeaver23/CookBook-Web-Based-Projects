package com.cookbook.data;

import com.cookbook.model.Account;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<Account, String> {

    Optional<Account> findByUsername(String username);      // at every login

    boolean existsByUsername(String username);              // at register: is the name taken?
    
    Optional<Account> findById(long id); 
}
