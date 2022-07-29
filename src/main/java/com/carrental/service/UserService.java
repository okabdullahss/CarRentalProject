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
import com.carrental.dto.request.AdminUserUpdateRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.request.UserUpdateRequest;
import com.carrental.dto.response.CRResponse;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ConflictException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.exception.message.ErrorMessage;
import com.carrental.repository.ReservationRepository;
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
	private ReservationRepository reservationRepository;
	
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
		
		//this part ensures to return more readable error message to the user, in stead of INTERNAL SERVER ERROR
		//wheh we try to use pre-flight methods and have CORST interception in browser
		 boolean exists = reservationRepository.existsByUserId(user);
        if(exists) {
            throw new BadRequestException(ErrorMessage.USER_USED_BY_RESERVATION_MESSAGE);
        }
		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		userRepository.deleteById(id);
		
	}
	
	//------------------------- UPDATE A USER BY ADMIN METHOD -------------------------------------------------------------
	
	public void updateUserAuth(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {
		
		//check if the email exists
		boolean existsByEmail = userRepository.existsByEmail(adminUserUpdateRequest.getEmail());
		
		//check if the user exists
		User user = userRepository.findById(id).
				orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		
		//check if the user is builtIn or not
		if(user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		
		//dont allow to use email that already exists
		if (existsByEmail && !adminUserUpdateRequest.getEmail().equals(user.getEmail())) {
			throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXISTS, user.getEmail()));
		}
		
		//if new request does not include new password(forgotten), set the old password as new one
		if(adminUserUpdateRequest.getPassword()==null) {
			
			String encodedPassword = passwordEncoder.encode(adminUserUpdateRequest.getPassword());
			adminUserUpdateRequest.setPassword(encodedPassword);
		} else {
			String encodedPassowrd =passwordEncoder.encode(adminUserUpdateRequest.getPassword());
			adminUserUpdateRequest.setPassword(encodedPassowrd);
		}
		
		//convert "String type roles" into  "Role type entities"
		Set<String> userStrRoles = adminUserUpdateRequest.getRoles();
		Set<Role> roles = convertRoles(userStrRoles);
		
		User updateUser = userMapper.adminUserUpdateRequestToUser(adminUserUpdateRequest);
		
		updateUser.setId(user.getId()); // during DTO convertion, we had ignored both id and roles,
		updateUser.setRoles(roles); // now we setting(updating) the id and the roles
		
		userRepository.save(updateUser); 
		
	}
	
	public Set<Role> convertRoles(Set<String> pRoles){
		Set<Role> roles = new HashSet<>();
		
		if(pRoles == null) {
			Role userRole = roleRepository.findByName(RoleType.ROLE_CUSTOMER)
			.orElseThrow(
			()-> new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));
			
			 roles.add(userRole);
		}else {
			pRoles.forEach(role-> {
				switch(role) {
				case "Administrator":
					Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
					.orElseThrow(() -> new ResourceNotFoundException(
							String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_ADMIN.name())));

			roles.add(adminRole);
			break;
			
			default: 
				
				Role userRole = roleRepository.findByName(RoleType.ROLE_CUSTOMER)
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));

		        roles.add(userRole);
				
				}
			});
		}
		
		return roles;
	}
	
	
	
	
}
