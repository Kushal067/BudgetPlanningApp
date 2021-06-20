package com.spring.kushal.jwtsecuritymongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.kushal.jwtsecuritymongodb.models.ERole;
import com.spring.kushal.jwtsecuritymongodb.models.Role;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);
}
