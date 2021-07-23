package com.spring.kushal.jwtsecuritymongodb.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.kushal.jwtsecuritymongodb.exception.TourException;
import com.spring.kushal.jwtsecuritymongodb.exception.UserException;
import com.spring.kushal.jwtsecuritymongodb.models.Favorites;
import com.spring.kushal.jwtsecuritymongodb.models.ImageModel;
import com.spring.kushal.jwtsecuritymongodb.models.Tour;
import com.spring.kushal.jwtsecuritymongodb.models.User;
import com.spring.kushal.jwtsecuritymongodb.security.service.UserDetailsImpl;
import com.spring.kushal.jwtsecuritymongodb.service.FavoritesServiceImpl;
import com.spring.kushal.jwtsecuritymongodb.service.ImageModelService;
import com.spring.kushal.jwtsecuritymongodb.service.TourService;
import com.spring.kushal.jwtsecuritymongodb.service.UserService;



@CrossOrigin()
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	FavoritesServiceImpl favService;
	
	@Autowired
	TourService tourService;
	
	ImageModelService imageModelService;
	
		
	@GetMapping("/getMyDetails" )
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getMyDetails() throws TourException{
		UserDetailsImpl userDetails =
				(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			User userLoggedIn= userService.getUser(userDetails.getId());
			   ImageModel img = new ImageModel(userLoggedIn.getProfilePicture().getName(), userLoggedIn.getProfilePicture().getType(),
					                 imageModelService.decompressBytes(userLoggedIn.getProfilePicture().getPicByte()));
			//System.out.print(userLoggedIn.toString());
			  
			Set<Tour> tours=userLoggedIn.getTours();
			Set<Tour> tours1=new HashSet<Tour>(); 
			for(Tour tour:tours) {
				tours1.add(tourService.findTourBrief(tour.getId()));
			}
			userLoggedIn.setTours(tours1);
			userLoggedIn.setProfilePicture(img);
			return new ResponseEntity<>(userLoggedIn, HttpStatus.OK);
		} catch (UserException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);		
		}
	}
	
	@PostMapping("/addToMyFavorites")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addToMyFavorite(@RequestBody Set<User> users){
		UserDetailsImpl userDetails =
				(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			User userLoggedIn = userService.getUser(userDetails.getId());
			userLoggedIn.setTours(null);
			favService.addFavorites(userLoggedIn.getUsername(), users);
			return new ResponseEntity<>("users added to your favorites",HttpStatus.OK);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_GATEWAY);
		}
		//return new ResponseEntity<>("users added to your favorites",HttpStatus.OK);
	}
	
	@PostMapping("/removeFromMyFavorites")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> removeFromMyFavorites(@RequestBody User user) throws UserException{
		UserDetailsImpl userDetails =
				(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userTobeRemoved= userService.getUser(user.getId());
		//System.out.println(userTobeRemoved);
	Set<User> favorites= favService.removeFromMyFavorites(userTobeRemoved, userDetails.getUsername());
	
		return favorites.size()>0? new ResponseEntity<>("done",HttpStatus.OK):new ResponseEntity<>("something went wrong;(",HttpStatus.CONFLICT);
	}
	
	@GetMapping("/myFavorites")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getMyFavorites(){
		UserDetailsImpl userDetails =
				(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
	Favorites myFav=	favService.getUserFavorites(userDetails.getUsername());
		for(User user:myFav.getFavoritesSet()) {
			 ImageModel img = new ImageModel(user.getProfilePicture().getName(), user.getProfilePicture().getType(),
	                  imageModelService.decompressBytes(user.getProfilePicture().getPicByte()));
			 user.setProfilePicture(img);
		}
		  
	return new ResponseEntity<>(myFav,HttpStatus.OK);
	}
	
	@PostMapping("/uploadProfilePicture")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addPicture(@RequestParam("profilePicture") MultipartFile profilePictue) throws IOException,UserException{
		UserDetailsImpl userDetails =
				(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			  System.out.println("Original Image Byte Size - " + profilePictue.getBytes().length);
			  ImageModel img = new ImageModel(profilePictue.getOriginalFilename(), profilePictue.getContentType(),
			                  imageModelService.compressBytes(profilePictue.getBytes()));
			User user= userService.getUser(userDetails.getId());
			user.setProfilePicture(img);
			userService.updateUserDetails(user.getId(), user);
			return new ResponseEntity<>("profile picture uploaded successfully",HttpStatus.OK);
		} catch (UserException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_GATEWAY);
		}
		
	}
	
	@GetMapping("/findUsers")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> findUsersbyUsernameOrName(@RequestParam String userInfo){
		List<User> users= userService.findUserByUsernameOrNameIsLike(userInfo, userInfo);
		//System.out.println(users);
		return new ResponseEntity<>(users.size()>0?users:"no users found :(",users.size()>0?HttpStatus.OK:HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/getUserById/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") String id) throws UserException{
		User user= userService.getUser(id);
		user.setTours(null);
		 ImageModel img = new ImageModel(user.getProfilePicture().getName(), user.getProfilePicture().getType(),
                 imageModelService.decompressBytes(user.getProfilePicture().getPicByte()));
		 user.setProfilePicture(img);
		return new ResponseEntity<>(user!=null?user:"no users found :(",user!=null?HttpStatus.OK:HttpStatus.NOT_FOUND);
	}
}
