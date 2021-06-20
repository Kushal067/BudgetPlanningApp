package com.spring.kushal.jwtsecuritymongodb.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.kushal.jwtsecuritymongodb.security.service.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerRestController {

    @GetMapping("/public")
    public String publicContent(){
        return "Public Content";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminContent(){
    	UserDetailsImpl userDetails =
    			(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Admin Content:"+userDetails.getUsername();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userContent(){
        return "User Content";
    }

    @GetMapping("/both")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String bothContent(){
        return "Both Content";
    }
}
