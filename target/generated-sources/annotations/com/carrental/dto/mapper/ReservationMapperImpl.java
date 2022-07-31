package com.carrental.dto.mapper;

import com.carrental.domain.Reservation;
import com.carrental.dto.request.ReservationRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-31T12:55:32+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public Reservation reservationRequestToReservation(ReservationRequest reservationRequest) {
        if ( reservationRequest == null ) {
            return null;
        }

        Reservation reservation = new Reservation();

        reservation.setPickUpTime( reservationRequest.getPickUpTime() );
        reservation.setDropOffTime( reservationRequest.getDropOffTime() );
        reservation.setPickUpLocation( reservationRequest.getPickUpLocation() );
        reservation.setDropOffLocation( reservationRequest.getDropOffLocation() );

        return reservation;
    }
}
