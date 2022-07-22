package com.carrental.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carrental.domain.Role;
import com.carrental.domain.User;
import com.carrental.domain.enums.RoleType;
import com.carrental.dto.UserDTO;
import com.carrental.dto.mapper.UserMapper;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.exception.ConflictException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.exception.message.ErrorMessage;
import com.carrental.repository.RoleRepository;
import com.carrental.repository.UserRepository;

import lombok.AllArgsConstructor;

 

@Service
@AllArgsConstructor
public class UserService {


	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private UserMapper userMapper;
	
//------------------------- REGISTER  METHOD  -------------------------------------------------------------	
	
	public void register(RegisterRequest registerRequest) {
		if(userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXISTS, registerRequest.getEmail()));
		}
		
		String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
		
		Role role = roleRepository.findByName(RoleType.ROLE_CUSTOMER).orElseThrow(
				()-> new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));
		
		
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		
		User user = new User();

		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(encodedPassword);
		user.setPhoneNumber(registerRequest.getPhoneNumber());
		user.setAddress(registerRequest.getAddress());
		user.setZipCode(registerRequest.getZipCode());
		user.setRoles(roles);

		userRepository.save(user);
	}
	
//----------------------------GET ALL USERS -----------------------------------------------------------	
	
	public List<UserDTO> getAllUsers() {// bu method ile User'larımı UserDTO'ya çevireceğim

		List<User> users = userRepository.findAll(); 
													 
		return userMapper.map(users);

	}
	
	
	public UserDTO findById(Long id) {
		User user =userRepository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
		
		return userMapper.userToUserDTO(user);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
