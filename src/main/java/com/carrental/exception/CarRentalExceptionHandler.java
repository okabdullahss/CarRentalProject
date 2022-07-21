package com.carrental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.carrental.exception.message.ApiResponseError;

@ControllerAdvice//this annotaion is to centerialize all exception handlers in one class
public class CarRentalExceptionHandler extends ResponseEntityExceptionHandler {

	//this method will be used repeteadly for every single exceptionhandling 
	private ResponseEntity<Object> buildResponseEntity(ApiResponseError error){
		return new ResponseEntity<>(error,error.getStatus());
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
		ApiResponseError error = new ApiResponseError(HttpStatus.NOT_FOUND,ex.getMessage(),request.getDescription(false));
		
		return buildResponseEntity(error);				
	}
	
	
	
	
	
	
	
}
