package com.carrental.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.CRResponse;
import com.carrental.dto.response.LoginResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.security.jwt.JwtUtils;
import com.carrental.service.UserService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping
@AllArgsConstructor
public class UserJwtController {
	
	private UserService userService;
	
	private AuthenticationManager authenticationManager;
	
	private JwtUtils jwtUtils;
	
	 @PostMapping("/register")
	 public ResponseEntity<CRResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
	
		 userService.register(registerRequest);
		 
		 CRResponse crResponse = new CRResponse();
		 crResponse.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
		 crResponse.setSuccess(true);
		 
		 return new ResponseEntity<>(crResponse,HttpStatus.CREATED);
		 
		 
	 }
	 
	 @PostMapping("/login")
	 public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
	
		 Authentication authentication  = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		  
		 String token = jwtUtils.generateJwtToken(authentication);
		 
		 LoginResponse response = new LoginResponse();
		 response.setToken(token);
		 
		 return new ResponseEntity<>(response,HttpStatus.OK);
	 }

}
