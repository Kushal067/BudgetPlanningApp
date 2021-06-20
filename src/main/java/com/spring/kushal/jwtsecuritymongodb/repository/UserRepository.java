package com.spring.kushal.jwtsecuritymongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.kushal.jwtsecuritymongodb.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    
    @Query(fields = "{'tours':0 , 'roles':0, 'profilePicture':0}")
    Optional<List<User>> findByUsernameOrNameIsLike(String username,String name); 
    
}
