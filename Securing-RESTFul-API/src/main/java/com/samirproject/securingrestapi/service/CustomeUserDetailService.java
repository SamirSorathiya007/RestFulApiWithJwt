package com.samirproject.securingrestapi.service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.samirproject.securingrestapi.entity.User;
import com.samirproject.securingrestapi.repository.UserRepository;

@Service
public class CustomeUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	public User registerUser(User user) throws ClassNotFoundException, SQLException {
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(userName);
		System.out.println("user :"+user);
		
		if(user == null) {
			throw new UsernameNotFoundException("User Not Found!!");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
}
