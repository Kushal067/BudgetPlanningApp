package com.spring.kushal.jwtsecuritymongodb.controllers;

import com.spring.kushal.jwtsecuritymongodb.models.ERole;
import com.spring.kushal.jwtsecuritymongodb.models.Favorites;
import com.spring.kushal.jwtsecuritymongodb.models.ImageModel;
import com.spring.kushal.jwtsecuritymongodb.models.Role;
import com.spring.kushal.jwtsecuritymongodb.models.User;
import com.spring.kushal.jwtsecuritymongodb.payload.request.SigninRequest;
import com.spring.kushal.jwtsecuritymongodb.payload.request.SignupRequest;
import com.spring.kushal.jwtsecuritymongodb.payload.response.JwtResponse;
import com.spring.kushal.jwtsecuritymongodb.payload.response.MessageResponse;
import com.spring.kushal.jwtsecuritymongodb.repository.RoleRepository;
import com.spring.kushal.jwtsecuritymongodb.repository.UserRepository;
import com.spring.kushal.jwtsecuritymongodb.security.jwt.JwtUtils;
import com.spring.kushal.jwtsecuritymongodb.security.service.UserDetailsImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.kushal.jwtsecuritymongodb.service.FavoritesServiceImpl;
import com.spring.kushal.jwtsecuritymongodb.service.ImageModelService;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin( maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private FavoritesServiceImpl favService;
    
    ImageModelService imgModelService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateRequest(@Valid @RequestBody SigninRequest signinRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword())
        );
        System.out.println(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      
        String jwt = jwtUtils.generateJWTToken(authentication);
        
        System.out.println(jwt);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        ImageModel img = new ImageModel( userDetails.getProfilePicture().getName(),  userDetails.getProfilePicture().getType(),
            imgModelService.decompressBytes( userDetails.getProfilePicture().getPicByte()));

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        img
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerRequest(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest()
                    .body(
                            new MessageResponse("Username already exists")
                    );
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest()
                    .body(
                            new MessageResponse("Email id already exists")
                    );
        }

        User user = new User(
        		signupRequest.getName(),
        		signupRequest.getMobile(),
        		
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword())
                //signupRequest.getProfiePictureString()
        );

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
        	System.out.println(ERole.ROLE_USER);
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        }

        strRoles.forEach(role -> {
            switch (role) {
                case "admin": {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(adminRole);
                }
                default: {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(userRole);
                }
            }
        });
        Favorites favorites=new Favorites();
        favorites.setUserId(user.getUsername());
        user.setRoles(roles);
        
       
        String picByteString="eJzsnGVUHF235xt3CxIcggZ39wR39wSXbpzGnRCscUjj7o0G9wR3t";
        byte[] picByte=picByteString.getBytes();
        ImageModel img=new ImageModel("th.jpg","image/jpeg",picByte);
        user.setProfilePicture(img);
        userRepository.save(user);
        favService.CreateFavorite(favorites);

        return ResponseEntity.ok(new MessageResponse(
                "User Registered Successfully"
        ));
    }
}
