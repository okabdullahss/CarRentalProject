package com.carrental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrental.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email); //checks if the user exits by that username(email)
	Boolean existsByEmail(String email);//checks if the new username(email) already exists.if yes, throw exception
	
}
