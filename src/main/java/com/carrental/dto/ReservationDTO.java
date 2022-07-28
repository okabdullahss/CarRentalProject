package com.carrental.dto;

import java.time.LocalDateTime;

import com.carrental.domain.Reservation;
import com.carrental.domain.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
	 private Long id;
	    
	   private CarDTO carId;
	   
	   private Long userId;
	   
	   private LocalDateTime pickUpTime;
	   private LocalDateTime dropOffTime;
	   
	   private String pickUpLocation;
	   
	   private String dropOffLocation;
	   private ReservationStatus status;
	   
	   private Double totalPrice;
	   
	   public ReservationDTO(Reservation reservation) {
		    this.id=reservation.getId();
		    this.carId= new CarDTO(reservation.getCarId());
		    this.userId=reservation.getUserId().getId();
		    this.pickUpLocation=reservation.getPickUpLocation();
		    this.pickUpTime=reservation.getPickUpTime();
		    this.dropOffLocation=reservation.getDropOffLocation();
		    this.dropOffTime=reservation.getDropOffTime();
		    this.status=reservation.getStatus();
		    this.totalPrice=reservation.getTotalPrice();
	   }
}
