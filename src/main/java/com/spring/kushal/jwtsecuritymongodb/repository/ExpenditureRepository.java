package com.spring.kushal.jwtsecuritymongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.spring.kushal.jwtsecuritymongodb.models.Expenditure;

@Repository
public interface ExpenditureRepository extends MongoRepository<Expenditure, String>{
	
	

}
