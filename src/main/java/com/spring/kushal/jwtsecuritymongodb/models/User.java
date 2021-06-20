package com.spring.kushal.jwtsecuritymongodb.models;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    
    @NotBlank
    @Size(min = 2,max = 20)
    private String name;
    
    @Size(min = 10,max = 10)
    @Indexed(unique = true)
    private String mobile;

    @NotBlank
    @Size(min=3,max = 20)
    @Indexed(unique = true)
    private String username;

    @Email
    @NotBlank
    @Size(max = 40)
    private String email;

    @NotBlank
    @Size(max = 60)
    @JsonIgnore
    private String password;

    @DBRef
    @JsonIgnoreProperties(value = "id")
    private Set<Role> roles = new HashSet<>();
    

    
    @DBRef(lazy = true)
	@JsonIgnoreProperties()
    private Set<Tour> tours=new HashSet<>();
   
    private ImageModel profilePicture;
    //constructor
    public User() {
    }

    public User(@NotBlank @Size(max = 20) String username, @Email @NotBlank @Size(max = 40) String email, @NotBlank @Size(max = 60) String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    

    public User(@NotBlank @Size(min = 3, max = 20) String name, @Size(min = 10, max = 10) String mobile,
			@NotBlank @Size(max = 20) String username, @Email @NotBlank @Size(max = 40) String email,
			@NotBlank @Size(max = 60) String password, Set<Role> roles, Set<Tour> tours) {
		super();
	
		this.name = name;
		this.mobile = mobile;
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
		
		this.tours = tours;
	}
    
    
    public User(@NotBlank @Size(min = 3, max = 20) String name, @Size(min = 10, max = 10) String mobile,
			@NotBlank @Size(max = 20) String username, @Email @NotBlank @Size(max = 40) String email,
			@NotBlank @Size(max = 60) String password) {
		super();
		this.name = name;
		this.mobile = mobile;
		this.username = username;
		this.email = email;
		this.password = password;
		
	}

	//Getters & Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	

	public ImageModel getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(ImageModel profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Set<Tour> getTours() {
		return tours;
	}

	public void setTours(Set<Tour> tours) {
		this.tours = tours;
	}
    
	public void addTours(Tour tour) {
		this.tours.add(tour);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", mobile=" + mobile + ", username=" + username + ", email="
				+ email + ", password=" + password + ", roles=" + roles + ", profilePicture=" + profilePicture
				+ ", tours=" + tours + "]";
	}
	
}
