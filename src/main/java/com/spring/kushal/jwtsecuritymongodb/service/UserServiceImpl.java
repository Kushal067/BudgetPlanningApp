package com.spring.kushal.jwtsecuritymongodb.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.kushal.jwtsecuritymongodb.exception.UserException;
import com.spring.kushal.jwtsecuritymongodb.models.Tour;
import com.spring.kushal.jwtsecuritymongodb.models.User;
import com.spring.kushal.jwtsecuritymongodb.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User getUser(String id) throws UserException {
	Optional<User> user=userRepository.findById(id);
	if(user.isPresent()) {
		return user.get();
	}
	else {
		throw new UserException("User with id:"+id+" not found");
	}
		
	}

	@Override
	public List<User> getAllUsers() throws UserException {
		
		List<User> allUsers= userRepository.findAll();
		if(allUsers.size()>0) {
			return allUsers;
		}
		else {
			throw new UserException("no users found");
		}
	}

	@Override
	public void assignTour(String id, Tour tour) throws UserException {
		User user= getUser(id);
		user.addTours(tour);
		userRepository.save(user);
	}

	@Override
	public Set<Tour> getToursByUser(String id) throws UserException {
		User user= getUser(id);
		if(user.getTours().size()>0) {
			return user.getTours();
		}
		else {
			throw new UserException("no tours found");
		}
	
	}

	@Override
	public void updateUserDetails(String id, User user) throws UserException {
		User user2= getUser(id);
		if(user2==null) {
			throw new UserException("user not found with id:"+id);
		}
		else {
		//user2.setEmail(user.getEmail()!=null?user.getEmail():user2.getEmail());
		user2.setMobile(user.getMobile()!=null?user.getMobile():user2.getMobile());
		user2.setName(user.getName()!=null?user.getName():user2.getName());
		user2.setProfilePicture(user.getProfilePicture()!=null?user.getProfilePicture():user2.getProfilePicture());
		userRepository.save(user2);
		}
	}
	
	@Override
	public List<User> findUserByUsernameOrNameIsLike(String username, String name) {
		Pageable paging = PageRequest.of(0, 10);
		return userRepository.findByUsernameOrNameIsLike(username, name, paging).get();
	}
}
