package com.spring.kushal.jwtsecuritymongodb.service;

import java.util.Set;

import com.spring.kushal.jwtsecuritymongodb.models.Favorites;
import com.spring.kushal.jwtsecuritymongodb.models.User;

public interface FavoritesService {
	public Favorites getUserFavorites(String userId);
	public void addFavorites(String userId,Set<User> users);
	public void CreateFavorite(Favorites fav);
	public Set<User> removeFromMyFavorites(User user,String userId);

}
