package com.spring.kushal.jwtsecuritymongodb.service;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import com.spring.kushal.jwtsecuritymongodb.exception.TourException;
import com.spring.kushal.jwtsecuritymongodb.models.Balance;
import com.spring.kushal.jwtsecuritymongodb.models.Tour;
import com.spring.kushal.jwtsecuritymongodb.models.User;


public interface TourService {
	
	public Tour getTourByID(String id) throws TourException;
	
	public List<Tour> getAllTours() throws TourException;
	
	
	public void createTour(Tour tour)throws  ConstraintViolationException;
	
	public void addBill(User spentBy,double amountSpent,String tourId, Set<User> spenton, String billNote) throws TourException,ConstraintViolationException;

	public Tour findTourBrief(String id) throws TourException;
	
	public boolean endTour(String id) throws TourException;
}
