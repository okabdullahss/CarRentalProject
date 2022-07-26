package com.carrental.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.CarDTO;
import com.carrental.dto.response.CRResponse;
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
	
}
