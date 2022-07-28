package com.carrental.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carrental.domain.Car;
import com.carrental.domain.Reservation;
import com.carrental.domain.User;
import com.carrental.domain.enums.ReservationStatus;
import com.carrental.dto.ReservationDTO;
import com.carrental.dto.mapper.ReservationMapper;
import com.carrental.dto.request.ReservationRequest;
import com.carrental.dto.request.ReservationUpdateRequest;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.exception.message.ErrorMessage;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationService {
	
	private ReservationRepository reservationRepository;
	private CarRepository carRepository;
	private UserRepository userRepository;
	private ReservationMapper reservationMapper;
	
	//--------------------------create a reservation ----------------------------------------------------------------	
	/**
	 * 
	 * @param reservationRequest - request(in form of dto), created by user
	 * @param userId             - which user wants to create this reservation(must be authenticated)
	 * @param carId				 - for which car, does the user wants to create reservation
	 */
	public void createReservation(ReservationRequest reservationRequest, Long userId, Long carId) {
		//step1) check the reservation dates if they are valid
		checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
		
		Car car = carRepository.findById(carId).orElseThrow(()-> 
		new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, carId)));
		
		//step2) check the requested times are available for requested car
		boolean carStatus = checkCarAvailability(carId, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
		
		User user =userRepository.findById(userId).orElseThrow(
	    		 ()-> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, userId)));
		
		Reservation reservation = reservationMapper.reservationRequestToReservation(reservationRequest);
		
		if(carStatus) {
			reservation.setStatus(ReservationStatus.CREATED);
		} else {
			throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
		}
		
		Double totalPrice =getTotalPrice(carId, reservation.getPickUpTime(), reservation.getDropOffTime());
		
	    reservation.setTotalPrice(totalPrice);
		
		reservation.setCarId(car);
		reservation.setUserId(user);
		
		reservationRepository.save(reservation);
	}
	
	
	
	//---------------------get all reservations ---------------------------------------------------------
	@Transactional(readOnly = true)
	public List<ReservationDTO> getAllReservation(){
		return reservationRepository.findAllBy();
	}
	//---------------------get a reservation by id ---------------------------------------------------------
	@Transactional(readOnly = true)
	public ReservationDTO findById(Long id) {
		return reservationRepository.findDTOById(id).orElseThrow(()->new
				ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
	}
	
	//--------------------- update a reservation  ---------------------------------------------------------
	
	public void updateReservation(Long carId, Long reservationId, ReservationUpdateRequest reservationUpdateRequest) {
		
		Reservation reservation=reservationRepository.findById(reservationId).orElseThrow(()->new 
            ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, reservationId)));
	
	//check res time is correct
	checkReservationTimeIsCorrect(reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
	
	Car car = carRepository.findById(carId).orElseThrow(()->
    new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, carId)));
	
	//check the car if available for times to be updated
	boolean carStatus =checkCarAvailability(carId, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
		
	//check to see if pickup and drop off times are equal for the updated reservation. and check the car id as well. if all same, 
	//then set the new status of the reservation
	if(reservationUpdateRequest.getPickUpTime().compareTo(reservation.getPickUpTime())==0 &&
	   reservationUpdateRequest.getDropOffTime().compareTo(reservation.getDropOffTime()) == 0 &&
	   car.getId().equals(reservation.getCarId().getId())) {
		reservation.setStatus(reservationUpdateRequest.getStatus());
	}else if (!carStatus) {
		throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
	}
	Double totalPrice = getTotalPrice(carId, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
    
    reservation.setTotalPrice(totalPrice);
    
    reservation.setCarId(car);
    reservation.setPickUpTime(reservationUpdateRequest.getPickUpTime());
    reservation.setDropOffTime(reservationUpdateRequest.getDropOffTime());
    reservation.setPickUpLocation(reservationUpdateRequest.getPickUpLocation());
    reservation.setDropOffLocation(reservationUpdateRequest.getDropOffLocation());
    reservationRepository.save(reservation);
	
	}
	
	//--------------------- get all reservations of a user by admin----------------------------------------------
	@Transactional(readOnly = true)
	public List<ReservationDTO> findAllByUserId(Long userId){
		User user=userRepository.findById(userId).orElseThrow(()->
        new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, userId)));
		
		return reservationRepository.findAllByUserId(user);
		
	}

	
	//------------method for checking res times --------------------------------------------
		private void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
			 LocalDateTime now = LocalDateTime.now();
			 
			 if(pickUpTime.isBefore(now)) {
				 throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
			 }
			 boolean isEqual = pickUpTime.isEqual(dropOffTime)?true:false;
			 boolean isBefore= pickUpTime.isBefore(dropOffTime)?true:false;
			 
			 if(isEqual || !isBefore) {
				 throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
			 }
		}
		//------------method for checking time availability for requested car ---------------------------------
		public boolean checkCarAvailability(Long carId,  LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
			
			//we must check cars that have the reservation status CREATED only. res status cancelled and done are already available
			ReservationStatus[] status = {ReservationStatus.CANCELLED,ReservationStatus.DONE};
			
			List<Reservation> existsReservations = reservationRepository.checkCarStatus(carId, pickUpTime, dropOffTime, status);
			return existsReservations.isEmpty();
		}
		
		//------------method for calculating the total price for car rental ---------------------------------
		public Double getTotalPrice(Long carId, LocalDateTime pickUpTime, LocalDateTime dropffTime) {
			
			Car car= carRepository.findById(carId).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, carId)));
			
			Long hours= (new Reservation()).getTotalHours(pickUpTime, dropffTime);
			
			return car.getPricePerHour()*hours;
		}
	
}
