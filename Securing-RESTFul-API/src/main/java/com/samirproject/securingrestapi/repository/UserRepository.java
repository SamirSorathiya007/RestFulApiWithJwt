package com.samirproject.securingrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samirproject.securingrestapi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public User findByUsername(String username);
}
