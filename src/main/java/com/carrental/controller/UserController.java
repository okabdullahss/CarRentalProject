package com.carrental.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.UserDTO;
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
	

	//------------------------- KULLANICILARIN "KENDİ BİLGİELRİNİ" GETİRME METHODU-------------------------------------
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
		Long id = (Long)request.getAttribute("id");  //reminder: we set the id attribute in auth token class with setAttribute
		
		
		UserDTO userDTO = userService.findById(id);//this id comes from the registered user, using getAttribute
		
		return ResponseEntity.ok(userDTO);
		
		 
	}
	
}
