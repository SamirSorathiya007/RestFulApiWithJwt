package com.samirproject.securingrestapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

	private final String SECRET_KEY = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";
	
	public String extractUsername(String tocken) {
		return extractClaim(tocken, Claims::getSubject);
	}
	
	public Date extractExpiration(String tocken) {
		return extractClaim(tocken, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String tocken, Function<Claims, T> claimResolver){
		final Claims claims = extractAllClaims(tocken);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String tocken) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(tocken).getBody();
	}
	
	private boolean isTockenExpired(String tocken) {
		return extractExpiration(tocken).before(new Date());
	}
	
	public String genrateTocken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createTocken(claims, userDetails.getUsername());
	}

	private String createTocken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(getSignKey(),SignatureAlgorithm.HS256).compact();
	}
	
	public boolean validateTocken(String tocken, UserDetails userDetails) {
		final String userName = extractUsername(tocken);
		return userName.equals(userDetails.getUsername()) && !isTockenExpired(tocken);
	}
	
	private Key getSignKey() {
		byte[] keyBytes = this.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
    }
}
