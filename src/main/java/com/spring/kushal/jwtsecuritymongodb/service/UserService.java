package com.spring.kushal.jwtsecuritymongodb.service;



import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.Query;

import com.spring.kushal.jwtsecuritymongodb.exception.UserException;
import com.spring.kushal.jwtsecuritymongodb.models.Tour;
import com.spring.kushal.jwtsecuritymongodb.models.User;

public interface UserService {

	public User getUser(String id) throws UserException;
	
	public List<User> getAllUsers() throws UserException;
	
	public void assignTour(String id,Tour tour) throws UserException;
	
	public Set<Tour> getToursByUser(String id) throws UserException;
	
	public void updateUserDetails(String id,User user) throws UserException;
	
	@Query(fields = "{'name':1,'username':1,''tours':0}")
	public List<User> findUserByUsernameOrNameIsLike(String username,String name);
}
