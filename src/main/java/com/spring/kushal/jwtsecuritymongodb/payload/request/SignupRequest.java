package com.spring.kushal.jwtsecuritymongodb.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public class SignupRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    @NotBlank
    @Size(max = 10,min = 10)
    private String mobile;
    
   // private String profiePictureString;
    
    @NotBlank
    @Size(min = 3, max=25)
    private String name;
    

    public SignupRequest() {
		super();
	}


	public SignupRequest(@NotBlank @Size(min = 4, max = 20) String username, @Email @NotBlank String email,
			@NotBlank @Size(min = 6, max = 40) String password, @NotBlank @Size(max = 10, min = 10) String mobile,
			@NotBlank @Size(min = 3, max = 25) String name, Set<String> roles) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.mobile = mobile;
		//this.profiePictureString = profiePictureString;
		this.name = name;
		this.roles = roles;
	}
    

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

//	public String getProfiePictureString() {
//		return profiePictureString;
//	}
//
//	public void setProfiePictureString(String profiePictureString) {
//		this.profiePictureString = profiePictureString;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	private Set<String> roles;

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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
