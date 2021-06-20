package com.spring.kushal.jwtsecuritymongodb.payload.response;

import java.util.List;
import java.util.Set;

import com.spring.kushal.jwtsecuritymongodb.models.ImageModel;
import com.spring.kushal.jwtsecuritymongodb.models.Role;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private ImageModel profilePicture;
   

	private List<String> roles;

    public JwtResponse(String token, String id, String username, String email, List<String> roles, ImageModel profilePicture) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.profilePicture=profilePicture;
    }
    
    public ImageModel getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(ImageModel profilePicture) {
		this.profilePicture = profilePicture;
	}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
