package com.carrental.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.carrental.domain.Car;
import com.carrental.domain.Reservation;
import com.carrental.domain.User;
import com.carrental.domain.enums.ReservationStatus;
import com.carrental.dto.ReservationDTO;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	List<ReservationDTO> findAllBy();
	
	Optional<ReservationDTO> findDTOById(Long id);
	
	boolean existsByCarId(Car car);  // for CORS / pre-flight method errors
	boolean existsByUserId(User user); // for CORS / pre-flight method errors
	
	List<ReservationDTO> findAllByUserId(User userId);
	
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
	
	//we can add custom query if needed
	Optional<ReservationDTO> findByIdAndUserId(Long id,User user);
	
}
