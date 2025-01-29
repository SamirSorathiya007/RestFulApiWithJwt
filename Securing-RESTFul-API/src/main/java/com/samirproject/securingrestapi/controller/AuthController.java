package com.samirproject.securingrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samirproject.securingrestapi.entity.AuthenticateUser;
import com.samirproject.securingrestapi.entity.User;
import com.samirproject.securingrestapi.service.CustomeUserDetailService;
import com.samirproject.securingrestapi.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomeUserDetailService customeUserDetailService;
		
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User user1 = null;
		try {
			user1 = customeUserDetailService.registerUser(user);
			return new ResponseEntity<User>(user1, HttpStatus.CREATED);
		}
		catch(Exception ex) {
			return new ResponseEntity<User>(user1, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody AuthenticateUser authenticateUser){
		System.out.println("log1...");
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateUser.getUsername(), authenticateUser.getPassword()));
			System.out.println("authentication :"+authentication);
		}
		catch(BadCredentialsException ex) {
			return new ResponseEntity<String>("Invalid username or password", HttpStatus.BAD_REQUEST);
		}
		System.out.println("log2...");
		final UserDetails userDetails = customeUserDetailService.loadUserByUsername(authenticateUser.getUsername());
		System.out.println("userDetails :"+userDetails);
		final String jwt = jwtUtil.genrateTocken(userDetails);
		return new ResponseEntity<String>(jwt, HttpStatus.OK);
	}
	
	@GetMapping("/dashBord")
	public ResponseEntity<String> welcomePage(){
		return new ResponseEntity<String>("Welcome to DashBord!!", HttpStatus.OK);
	}
}
