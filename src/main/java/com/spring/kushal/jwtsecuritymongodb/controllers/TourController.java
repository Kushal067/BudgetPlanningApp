package com.spring.kushal.jwtsecuritymongodb.controllers;

import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.kushal.jwtsecuritymongodb.exception.TourException;
import com.spring.kushal.jwtsecuritymongodb.exception.UserException;
import com.spring.kushal.jwtsecuritymongodb.models.Expenditure;
import com.spring.kushal.jwtsecuritymongodb.models.Tour;
import com.spring.kushal.jwtsecuritymongodb.models.User;
import com.spring.kushal.jwtsecuritymongodb.security.service.UserDetailsImpl;
import com.spring.kushal.jwtsecuritymongodb.service.TourService;
import com.spring.kushal.jwtsecuritymongodb.service.UserService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/v1/tour")
public class TourController {
	@Autowired
	TourService tourService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/{tourId}")
	 @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> getTourById(@PathVariable("tourId") String tourId){		
		try {
			Tour tour=tourService.getTourByID(tourId);
			System.out.println(tour);
			return new ResponseEntity<>(tour,HttpStatus.OK);
		} catch (TourException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/{tourId}/addBill")
	 @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> addBill(@PathVariable("tourId") String tourId, @RequestBody Expenditure exp) {
		try {
		UserDetailsImpl userDetails =
    			(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			User userLoggedIn= userService.getUser(userDetails.getId());
			try {
				tourService.addBill(userLoggedIn, exp.getAmountSpent(),tourId,exp.getSplitBillOn(),exp.getBillNote());
				//addBill(User spentBy, double amountSpent, String tourId, Set<User> billSpenton, String billNote)
				return new ResponseEntity<>("bill added succesfully",HttpStatus.OK);
			} catch (ConstraintViolationException e) {
				return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
			} catch (TourException e) {
				return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
			}
			
		} catch (UserException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping("/{tourId}/endTour")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public boolean endTour(@PathVariable("tourId") String tourId) {
		try {
			return tourService.endTour(tourId);
		} catch (TourException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	
	}
	
	@PostMapping("/")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> createTour(@RequestBody Tour tour){
		try {
			UserDetailsImpl userDetails =
	    			(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User userLoggedIn= userService.getUser(userDetails.getId());
			tour.setCreatedBy(userLoggedIn);
			tourService.createTour(tour);
			User user= tour.getCreatedBy();
			user.setTours(null);
			Set<User> part= tour.getParticipants();
			for(User u:part) {
				u.setTours(null);
			}
			tour.setParticipants(part);
			return new ResponseEntity<> (tour,HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
		}

	}

}
