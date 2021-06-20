package com.spring.kushal.jwtsecuritymongodb.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.kushal.jwtsecuritymongodb.exception.UserException;
import com.spring.kushal.jwtsecuritymongodb.models.Favorites;
import com.spring.kushal.jwtsecuritymongodb.models.User;
import com.spring.kushal.jwtsecuritymongodb.repository.FavoritesRepository;

@Service
public class FavoritesServiceImpl implements FavoritesService {
	
	@Autowired
	FavoritesRepository favRepo;
	
	@Autowired
	UserService userService;
	
	@Override
	public Favorites getUserFavorites(String userId) {
		
		return favRepo.findByUserId(userId).get();
	}

	@Override
	public void addFavorites(String userId, Set<User> users) {
		Favorites favorites=favRepo.findByUserId(userId).get();
		if(favorites==null) {
			Favorites favs=new Favorites();
		
			favs.addFavorites(users);
			favs.setUserId(userId);
			favRepo.save(favs);
		}
		else {
			Set<User> favoritesToSave = favorites.getFavoritesSet();
			for(User user :users) {
				boolean contains=false;
				try {
					User u1=userService.getUser(user.getId());
					
					for(User u: favoritesToSave) {
						if(u.getUsername().equals(u1.getUsername())) {
							contains=true;
							break;
						}
					}
					if(!contains)
						favoritesToSave.add(u1);
				} catch (UserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		favorites.setFavoritesSet(favoritesToSave);
		favorites.setUserId(userId);
		favRepo.save(favorites);
		}
	}

	@Override
	public void CreateFavorite(Favorites fav) {
		favRepo.save(fav);
	}

	@Override
	public Set<User> removeFromMyFavorites(User user, String userId) {
		Favorites favRecord=favRepo.findByUserId(userId).get();
		Set<User> favorites=favRecord.getFavoritesSet();
		System.out.println(favorites);
		System.out.println("---------------------------");
		boolean done= false;
		User userToBeRemoved=new  User();
		for(User u:favorites)	{
			//System.out.println("u.getId()"+u.getId()+";user.getId()"+user.getId());
			if(u.getId().equals(user.getId())) {
				userToBeRemoved=u;
				done=true;
				break;
			}
		}
		if(userToBeRemoved!=null)
		favorites.remove(userToBeRemoved);
		favRecord.setFavoritesSet(favorites);
		if(done &&favRepo.save(favRecord)!=null)
		{
			return favorites;
		}
		
		return null;
	}

}
