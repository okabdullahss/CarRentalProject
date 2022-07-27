package com.carrental.dto.mapper;

import org.mapstruct.Mapper;

import com.carrental.domain.Reservation;
import com.carrental.dto.request.ReservationRequest;

@Mapper(componentModel="spring")
public interface ReservationMapper {
	
	Reservation reservationRequestToReservation(ReservationRequest reservationRequest);

}
