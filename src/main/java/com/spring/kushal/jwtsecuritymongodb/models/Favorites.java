package com.spring.kushal.jwtsecuritymongodb.models;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "favorites")
@CompoundIndexes({
@CompoundIndex(name = "aid_bid_idx", def = "{'id': 1, 'favoritesSet.username': 1}")
})
public class Favorites {
	
	@Id
	private String id;
	
	@NotNull
	private String userId;
	
	@DBRef
	@JsonIgnoreProperties(value = "tours")
    private Set<User> favoritesSet=new HashSet<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Set<User> getFavoritesSet() {
		return favoritesSet;
	}

	public void addFavorites(Set<User> users) {
		for(User user:users) {
			this.favoritesSet.add(user);
		}
		
	}
	public void setFavoritesSet(Set<User> favoritesSet) {
		this.favoritesSet = favoritesSet;
	}
	

}
