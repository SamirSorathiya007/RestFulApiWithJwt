package com.samirproject.securingrestapi.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.samirproject.securingrestapi.entity.User;
import com.samirproject.securingrestapi.repository.UserRepository;
import com.samirproject.securingrestapi.service.UserService;

@Service
public class CustomeUserDetailService implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User registerUser(User user) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(userName);
		
		if(user == null) {
			throw new UsernameNotFoundException("User Not Found!!");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
}
