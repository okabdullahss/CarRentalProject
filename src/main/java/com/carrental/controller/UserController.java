package com.carrental.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.UserDTO;
import com.carrental.dto.request.UpdatePasswordRequest;
import com.carrental.dto.request.UserUpdateRequest;
import com.carrental.dto.response.CRResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

	private UserService userService;


	//-------------------------GET ALL USERS --------------------------------------------------
	@GetMapping("/auth/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		List<UserDTO> users = userService.getAllUsers();
		
		return ResponseEntity.ok(users);
	}
	

	//-------------------------- GET A REGISTERED USER --------------------------------------
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
		Long id = (Long)request.getAttribute("id");  //reminder: we set the id attribute in auth token class with setAttribute
		
		
		UserDTO userDTO = userService.findById(id);//this id comes from the registered user, using getAttribute
		
		return ResponseEntity.ok(userDTO);
		
		 
	}
	
	//--------------------------- GET USER WITH PAGE ----------------------------------------
	
	@GetMapping("/auth/pages")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserDTO>> getAllUserByPage(@RequestParam("page") int page,
			                                              @RequestParam("size") int size,
			                                              @RequestParam("sort") String prop,
			                                              @RequestParam("direction") Direction direction){
	 Pageable pageable = PageRequest.of(page, size, Sort.by(direction,prop));
	 
	 Page<UserDTO> userDTOPage = userService.getUserPage(pageable);
	 
	 return ResponseEntity.ok(userDTOPage);
	
	
	}
	
	//--------------------------- GET A USER WITH PATHVARIABLE BY ADMIN ----------------------------------------
	
	@GetMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUserByIdAdmin(@PathVariable Long id){
		UserDTO user = userService.findById(id);
		
		return ResponseEntity.ok(user);
		
	}
	
	//--------------------------- CHANGE PASSWORD METHOD ----------------------------------------
	@PatchMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<CRResponse> updatePassword(HttpServletRequest httpServletRequest, @RequestBody UpdatePasswordRequest updatePasswordRequest ){
		
		Long id = (Long)httpServletRequest.getAttribute("id");
		
		userService.updatePassword(id, updatePasswordRequest);
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.PASSWORD_CHANGED_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
	}
	
	//------------------------- USER UPDATE  METHOD -------------------------------------------------------------
	
	@PutMapping
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<CRResponse> updateUser(HttpServletRequest httpServletRequest, @Valid @RequestBody UserUpdateRequest userUpdateRequest){
		
		//verify the user logged in
		Long id = (Long)httpServletRequest.getAttribute("id");
		
		userService.updateUser(id, userUpdateRequest);
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.UPDATE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		
	}
	//------------------------- DELETE A USER METHOD -------------------------------------------------------------
	
		@DeleteMapping("/{id}/auth")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<CRResponse>deleteUser(@PathVariable Long id){
			
			userService.removeById(id);
			
			CRResponse response = new CRResponse();
			response.setMessage(ResponseMessage.DELETE_RESPONSE_MESSAGE);
			response.setSuccess(true);
			
			return ResponseEntity.ok(response);

		}
	
	
	

	
}
