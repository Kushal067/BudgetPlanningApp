package com.spring.kushal.jwtsecuritymongodb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.kushal.jwtsecuritymongodb.models.Tour;
import com.spring.kushal.jwtsecuritymongodb.models.User;

@Repository
public interface TourRepository extends MongoRepository<Tour, String>{
	@Query(value="{_id:'?0'}",fields = "{'createdBy':0,'participants':0}",sort = "{'date' : -1}" )
	Optional<Tour> findTourBrief(String Id); 
}
