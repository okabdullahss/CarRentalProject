package com.carrental.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carrental.domain.Role;
import com.carrental.domain.User;
import com.carrental.domain.enums.RoleType;
import com.carrental.dto.UserDTO;
import com.carrental.dto.mapper.UserMapper;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.request.UserUpdateRequest;
import com.carrental.dto.response.CRResponse;
import com.carrental.exception.BadRequestException;
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
	
//----------------------------GET ALL USERS BY PAGE -----------------------------------------------------------		
	
	public Page<UserDTO> getUserPage(Pageable pageable){
		Page<User> users = userRepository.findAll(pageable);
		Page<UserDTO> dtoPage=  users.map(new Function<User, UserDTO>() {
		
			   @Override
			public UserDTO apply(User user) {
				return userMapper.userToUserDTO(user);
			}
			  
		  });
		
		return dtoPage;
	}
	
	 
//----------------------------UPDATE PASSWORD -----------------------------------------------------------
	
	public void updatePassword(Long id, com.carrental.dto.request.UpdatePasswordRequest passwordRequest) {
		Optional<User> userOpt = userRepository.findById(id);
		User user = userOpt.get();
		
		//check to see if the user is built in. if yes, dont allow to change password
		if(user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		
		//check to see if passwords of currently logged in user and password is being changed are the same
		if(!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
			throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
		}
		
		//encode the new password
		String hashedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
		user.setPassword(hashedPassword);
		
		userRepository.save(user);
	}
	
	
	//------------------------- USER UPDATE  METHOD -------------------------------------------------------------
	
	@Transactional//post method throws "lob excp" wihtout transactional annotation
	public void updateUser(Long id, UserUpdateRequest userUpdateRequest) {
		
		//e-mail must not be already taken or must belong to the user logged in
		boolean emailExists = userRepository.existsByEmail(userUpdateRequest.getEmail());
		
		User user = userRepository.findById(id).get();
		
		//not allow update if the user is builtIn
		if(user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		
		//mail of user being updated must exists  &&  should only update its own user infos
		if(emailExists && !userUpdateRequest.getEmail().equals(user.getEmail())) {
			throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXISTS, userUpdateRequest.getEmail()));
		}
		
		userRepository.update(id, userUpdateRequest.getFirstName(), userUpdateRequest.getLastName(),
				userUpdateRequest.getPhoneNumber(), userUpdateRequest.getEmail(),
				userUpdateRequest.getAddress(), userUpdateRequest.getZipCode());
		
	}
	
	//------------------------- DELETE A USER METHOD -------------------------------------------------------------
	public void removeById(Long id) {
		User user=userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		
		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		userRepository.deleteById(id);
		
	}
	
	
}
