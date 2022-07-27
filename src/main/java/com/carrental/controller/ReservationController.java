package com.carrental.controller;

 

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.request.ReservationRequest;
import com.carrental.dto.response.CRResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.ReservationService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {

	private ReservationService reservationService;
	
	//----------------------------------------CREATE A RESERVATION ----------------------------------------------------------
	@PostMapping("/add")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<CRResponse> makeReservation(HttpServletRequest request,@RequestParam(value="carId") Long carId,
													  @Valid @RequestBody ReservationRequest reservationRequest){
		Long userId = (Long)request.getAttribute("id");
		reservationService.createReservation(reservationRequest, userId, carId);
		
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
}
