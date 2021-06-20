package com.spring.kushal.jwtsecuritymongodb.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.spring.kushal.jwtsecuritymongodb.security.service.UserDetailsImpl;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${deepak.app.jwtSecret}")
    private String jwtSecret;

    @Value("${deepak.app.jwtExpirationMs}")
    private String jwtExpirationMs;

    public String generateJWTToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
               // .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .setExpiration(new Date(System.currentTimeMillis() + 36000000))
                //.signWith(SignatureAlgorithm.ES512, jwtSecret)
                .signWith(SignatureAlgorithm.HS512, "ghfgujhcvifddsiohbvjhdsvboicvjhcdfweisfbjcxvgcuifvgdskjfhoi")
                .compact();
    }

    public String getUsernameFromJWTToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJWTToken(String authToken){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }catch (ExpiredJwtException e){
            logger.error("JWT Token Expired : {}", e.getMessage());
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT Token : {}", e.getMessage());
        }catch (SignatureException e){
            logger.error("Invalid JWT Signature : {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT Token : {}", e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("JWT Token is empty : {}", e.getMessage());
        }

        return false;
    }
}
