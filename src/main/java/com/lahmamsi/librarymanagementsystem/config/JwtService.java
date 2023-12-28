package com.lahmamsi.librarymanagementsystem.config;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
	
	@Value("${jwt.secretKey}")
	private String key; 
	
    /**
     * Extracts the username from the JWT token.
     * @param jwt JWT token
     * @return String representing the username
     */
	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}
	
	/**
	 * Generates a JWT token for a given UserDetails.
     * @param userDetails UserDetails of the user
     * @return String representing the generated token
	 */
	public String generateToken(UserDetails userDetails) {
		return generatToken(new HashMap<>(), userDetails);
	}
	
	public String generatToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts
				.builder()
				.setClaims(claims)
				.signWith(getSignInKey())
				.setSubject(userDetails.getUsername())
				.setExpiration(Date.from(Instant.now().plusSeconds(60 * 30)))
				.setIssuedAt(new Date())
//				.setIssuer("Self")
				.compact();
				
	}
	//Validate Token
	boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equalsIgnoreCase(userDetails.getUsername()) && isTokenExpired(token);
	}
	
	
	//Validate whether Token is expired
	private boolean isTokenExpired(String token) {
		Date date = extractExpiration(token);
		return date.after(new Date());
	}
	
    // Extracts the expiration date from the token.
	private  Date  extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
		
	}
	
	//Extract a Claim from the token 
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractClaims(token);
		return claimResolver.apply(claims);
	}
	
	//Extract the Claims from the token 
	private Claims extractClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

    // Retrieves the secret key used for signing.
	private SecretKey getSignInKey() {
		byte[] keyByte = Decoders.BASE64.decode(key);
		
		return Keys.hmacShaKeyFor(keyByte);
	}

	
}

