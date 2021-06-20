package com.spring.kushal.jwtsecuritymongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.spring.kushal.jwtsecuritymongodb.models.Favorites;


public interface FavoritesRepository extends MongoRepository<Favorites, String>{
	
	   Optional<Favorites> findByUserId(String userId);
}
