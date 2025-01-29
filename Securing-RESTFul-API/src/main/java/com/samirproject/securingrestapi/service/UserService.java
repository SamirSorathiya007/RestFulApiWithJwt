package com.samirproject.securingrestapi.service;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.samirproject.securingrestapi.entity.User;

public interface UserService {
	
	public User registerUser(User user) throws ClassNotFoundException, SQLException;

	public UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException;
}
