package com.cookbook.data;

import com.cookbook.model.Account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<Account, String> {

    Optional<Account> findByUsername(String username);      // at every login

    boolean existsByUsername(String username);              // at register: is the name taken?
    
    @Query(value = "{ '_id': ?0 }", fields = "{ 'username': 1 }")
    String findUsernameById(Long id);
}
