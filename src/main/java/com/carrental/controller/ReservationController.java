package com.carrental.controller;

 

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.ReservationDTO;
import com.carrental.dto.request.ReservationRequest;
import com.carrental.dto.request.ReservationUpdateRequest;
import com.carrental.dto.response.CRResponse;
import com.carrental.dto.response.CarAvailabilityResponse;
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
	
	//----------------------------------------GET ALL RESERVATIONS ----------------------------------------------------------
	
	@GetMapping("/admin/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getAllReservations(){
		List<ReservationDTO> allReservation = reservationService.getAllReservation();
		return ResponseEntity.ok(allReservation);
	}
	//----------------------------------------GET A RESERVATION BY ID ----------------------------------------------------------
	
	@GetMapping("/{id}/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id){
		
		ReservationDTO reservationDTO = reservationService.findById(id);
		return ResponseEntity.ok(reservationDTO);
		
	}
	
	//---------------------------MAKE A RESERVATION BY ADMIN ONLY WITH A USER ID ------------------------------------------------
	
	@PostMapping("/add/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CRResponse> addReservation(@RequestParam(value = "userId") Long userId,
													 @RequestParam(value="carId") Long carId,
													 @Valid @RequestBody ReservationRequest request){
		
		reservationService.createReservation(request, userId, carId);
		
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
        response.setSuccess(true);
		
        return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	//----------------------------------------UPDATE A RESERVATION ----------------------------------------------------------
	/* POSTMAN =  PUT + http://localhost:8080/reservations/admin/auth?carId=3&reservationId=1
	  JSON BODY = {
	    "pickUpTime":"07/16/2022 20:00:00",
	    "dropOffTime":"07/16/2022 23:00:00",
	    "pickUpLocation":"Ankara",
	    "dropOffLocation":"Ankara",
	    "status":"DONE"
	}
	 */
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CRResponse> updateReservation(@RequestParam(value="carId") Long carId,
														@RequestParam(value="reservationId") Long reservationId,
														@Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest
														){
		reservationService.updateReservation(carId, reservationId, reservationUpdateRequest);
		
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.RESERVATION_UPDATE_RESPONSE_MESSAGE); 
        response.setSuccess(true);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//--------------------------GET ALL RESERVATIONS OF A USER BY ADMIN----------------------------------
	@GetMapping("/admin/auth/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getAllUserReservations(@RequestParam(value="userId") Long userId){
		
		List<ReservationDTO> reservations = reservationService.findAllByUserId(userId);
		return ResponseEntity.ok(reservations);
		
	}
	
	//-----------------------------CHECK CAR AVAILABILITY METHOD --------------------------------------------------
	@GetMapping("/auth")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<CRResponse> checkCarIsAvailable(@RequestParam(value="carId") Long carId,
            											  @RequestParam(value="pickUpDateTime") @DateTimeFormat(pattern="MM/dd/yyyy HH:mm:ss") LocalDateTime pickUpTime,
            											  @RequestParam(value="dropOffDateTime") @DateTimeFormat(pattern="MM/dd/yyyy HH:mm:ss") LocalDateTime dropOffTime){
		
		boolean isAvailable = reservationService.checkCarAvailability(carId, pickUpTime, dropOffTime);
		Double totalPrice = reservationService.getTotalPrice(carId, pickUpTime, dropOffTime);
		
		 CarAvailabilityResponse response=new CarAvailabilityResponse(isAvailable,totalPrice,ResponseMessage.CAR_AVAILABLE_MESSAGE,true);
		
		 return new ResponseEntity<>(response,HttpStatus.OK);
		 
	}
	
	//--------------------------------------DELETE A RESERVATION ------------------------------------------------------
	@DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CRResponse> deleteReservation(@PathVariable Long id){
		
		reservationService.removeById(id);
		
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.RESERVATION_DELETED_RESPONSE_MESSAGE); 
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
		
	}
	
	//--------------------------------------GET A RESERVATION OF ITS OWN by ID(user or admin) ------------------
	 @GetMapping("/{id}/auth")
	 @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	 public ResponseEntity<ReservationDTO>getUserReservationById(HttpServletRequest request, @PathVariable Long id){
		 
		 Long userId = (Long)request.getAttribute("id"); //to keep track on authenticated user
		 
		 ReservationDTO reservationDTO = reservationService.findByIdAndUserId(id, userId);
		 
		 return ResponseEntity.ok(reservationDTO);
		 
	 }
	 
	//--------------------------------------GET ALL RESERVATIONS OF AUTHENTICATED USER BY ITS OWN(user or admin) ------------------
	 @GetMapping("/auth/all")
	 @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	 public ResponseEntity<List<ReservationDTO>> getUserReservationsByUserId(HttpServletRequest request){
		 
		 Long userId = (Long)request.getAttribute("id");
		 
		 List<ReservationDTO> reservations = reservationService.findAllByUserId(userId);
		 
		 return ResponseEntity.ok(reservations);
		 
	 }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
