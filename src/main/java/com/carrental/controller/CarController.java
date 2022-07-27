package com.carrental.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

import com.carrental.dto.CarDTO;
import com.carrental.dto.response.CRResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.CarService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
public class CarController {
	
	private CarService carService;
	
	//-------------------------------------- SAVE A CAR METHOD -----------------------------------------------	
	@PostMapping("/admin/{imageId}/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CRResponse> saveCar(@PathVariable String imageId,@Valid @RequestBody CarDTO carDTO){
		carService.saveCar(carDTO, imageId);
		
		CRResponse response=new CRResponse();
		response.setMessage("Car successfully saved");
		response.setSuccess(true);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	//-------------------------------------- GET ALL CARS METHOD -----------------------------------------------	
	
	@GetMapping("/visitors/all")
	public ResponseEntity<List<CarDTO>> getAllCars(){
		List<CarDTO> carDTOs = carService.getAllCars();
		return ResponseEntity.ok(carDTOs);
	}
	
	
	
	//-------------------------------------- GET A CAR BY ID METHOD -----------------------------------------------	
	
	@GetMapping("/visitors/{id}")
	public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
		CarDTO carDTO = carService.findById(id);
		
		return ResponseEntity.ok(carDTO);
		
	}
	
	//-------------------------------------- GET CARS WITH PAGES METHOD -----------------------------------------------
	
	@GetMapping("/visitors/pages") 	
	public ResponseEntity<Page<CarDTO>> getAllWithPage(@RequestParam("page") int page, @RequestParam("size") int size,
													   @RequestParam("sort") String prop, @RequestParam("direction") Direction direction ){
		
		Pageable pageable = PageRequest.of(page, size,Sort.by(direction,prop));
		Page<CarDTO> carDTOPages = carService.findAllWithPage(pageable);
		
		return ResponseEntity.ok(carDTOPages);
		
		
	}
	//-------------------------------------- UPDATE A CAR METHOD -----------------------------------------------
	
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CRResponse> updateCar(@RequestParam("id") Long id,
												@RequestParam("imageId") String imageId,
												@Valid @RequestBody CarDTO carDTO){
		
		carService.updateCar(id, imageId, carDTO);
		
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.CAR_UPDATE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		
	}
	//-------------------------------------- DELETE A CAR METHOD -----------------------------------------------
	
	@DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CRResponse> deleteCar(@PathVariable Long id){
		
		carService.removeById(id);
		
		CRResponse response = new CRResponse();
		response.setMessage(ResponseMessage.CAR_DELETE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
	}
	
	
	
	
	
	
	
}
