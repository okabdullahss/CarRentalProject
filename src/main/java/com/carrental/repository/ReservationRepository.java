package com.carrental.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.carrental.domain.Car;
import com.carrental.domain.Reservation;
import com.carrental.domain.User;
import com.carrental.domain.enums.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	boolean existsByCarId(Car car);
	boolean existsByUserId(User user);
	
	//custom query for checkCarAvailability method in ReservationService.class
	@Query("SELECT r FROM Reservation r "
            + "JOIN FETCH Car cd on r.carId=cd.id WHERE "
            + "cd.id=:carId and (r.status not in :status) and :pickUpTime BETWEEN r.pickUpTime and r. dropOffTime "
            + "or "
            + "cd.id=:carId and (r.status not in :status) and :dropOffTime BETWEEN r.pickUpTime and r. dropOffTime "
            + "or "
            + "cd.id=:carId and (r.status not in :status) and (r.pickUpTime BETWEEN :pickUpTime and :dropOffTime)")
	List<Reservation> checkCarStatus(@Param("carId") Long carId, @Param("pickUpTime") LocalDateTime pickUptime,
									 @Param("dropOffTime") LocalDateTime dropOffTime, @Param("status") ReservationStatus[] status);
	
}
