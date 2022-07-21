package com.carrental.exception.message;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
/* This custom class provides an error message when an exception is thrown */
public class ApiResponseError {

	private HttpStatus status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String requestURI;
	
	private ApiResponseError() { //this private constructor is to determine the time when error is occured
								 //and hence, we dont have setTimestamp() setter method
		timestamp = LocalDateTime.now();
	}
	
	public ApiResponseError(HttpStatus status) {
		this();
		this.status=status;
	}
	
	public ApiResponseError(HttpStatus status, String message, String requestURI) {
		this(status);
		this.message=message;
		this.requestURI=requestURI;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	
	
	
}
