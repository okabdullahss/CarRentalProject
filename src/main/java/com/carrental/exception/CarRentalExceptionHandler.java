package com.carrental.exception;

import java.nio.file.AccessDeniedException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
	
	@ExceptionHandler(ConflictException.class)
	protected ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request){
		ApiResponseError error = new ApiResponseError(HttpStatus.CONFLICT,ex.getMessage(),request.getDescription(false));
		
		return buildResponseEntity(error);				
	}
	
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<Object> handleBadRequestException(ConflictException ex, WebRequest request){
		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,ex.getMessage(),request.getDescription(false));
		
		return buildResponseEntity(error);				
	}
	
	@ExceptionHandler(ImageFileException.class)
	protected ResponseEntity<Object> handleImageFileException(ImageFileException ex, WebRequest request){
		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,ex.getMessage(),request.getDescription(false));
		
		return buildResponseEntity(error);				
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,ex.getMessage(),request.getDescription(false));
		return buildResponseEntity(error);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){
		ApiResponseError error = new ApiResponseError(HttpStatus.FORBIDDEN,ex.getMessage(),request.getDescription(false));
		
		return buildResponseEntity(error);				
	}
	
	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request){
		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,ex.getMessage(),request.getDescription(false));
		
		return buildResponseEntity(error);				
	}
	
	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<Object> handleGeneralException(RuntimeException ex, WebRequest request){
		ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(),request.getDescription(false));
		
		return buildResponseEntity(error);				
	}
	
	
}
