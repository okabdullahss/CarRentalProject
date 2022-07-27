package com.carrental.exception.message;

public class ErrorMessage {

	public final static String RESOURCE_NOT_FOUND_EXCEPTION="Resource not found with id %d ";
	public final static String RESOURCE_NOT_FOUND_MESSAGE="Resource with id %d not found";
	
	public final static String USER_NOT_FOUND_EXCEPTION="User not found with email %s ";
	
	public final static String EMAIL_ALREADY_EXISTS="Email already exists: %s";
	
	public final static String ROLE_NOT_FOUND_MESSAGE="Role with name %s not found";
	
	public final static String NOT_PERMITTED_METHOD_MESSAGE="You dont have any permission to change this values";
	
	public final static String PASSWORD_NOT_MATCHED="Your passwords are not matched";
	
	public final static String IMAGE_NOT_FOUND_MESSAGE="ImageFile with id %s not found";
	
	public final static String CAR_USED_BY_RESERVATION_MESSAGE="Car couldnt be deleted. Car is used by a reservation";
	
	public final static String RESERVATION_TIME_INCORRECT_MESSAGE="Reservation pick up time or drop off time not correct";
	
	public final static String CAR_NOT_AVAILABLE_MESSAGE="Car is not available for selected time";
}
