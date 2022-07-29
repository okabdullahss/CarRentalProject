package com.carrental.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carrental.domain.Car;
import com.carrental.domain.ImageFile;
import com.carrental.dto.CarDTO;
import com.carrental.dto.mapper.CarMapper;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.exception.message.ErrorMessage;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ImageFileRepository;
import com.carrental.repository.ReservationRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CarService {

	private CarRepository carRepository;
	private ImageFileRepository imageFileRepository;
	private CarMapper carMapper;
	private ReservationRepository reservationRepository;
	
	//---------------- save car -----------------------------------------	
	
	public void saveCar(CarDTO carDTO, String imageId) {
		
		ImageFile imFile = imageFileRepository.findById(imageId).
				orElseThrow(()->new ResourceNotFoundException
						(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, imageId)));
		
		Car car = carMapper.carDTOToCar(carDTO);
		
		Set<ImageFile> imFiles = new HashSet<>();
		imFiles.add(imFile);
		car.setImage(imFiles);
		
		carRepository.save(car);
		
	}
	//--------------- get all cars -----------------------------------
	
	@Transactional(readOnly = true)//just in case for the exception " unable to access lob stream". When fetch type is lazy between
									// 2 entities, @Transactional annot will open a transaction until service method is finished
	public List<CarDTO> getAllCars(){
		List<Car> carList = carRepository.findAll();
		return carMapper.map(carList);
	}
	
	
	
	
	//---------------- get car by id -----------------------------------------	
	
	@Transactional(readOnly = true)//just in case for the exception " unable to access lob stream"
	public CarDTO findById(Long id) {
		
		Car car = carRepository.findById(id).orElseThrow(()-> 
		new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
		
		return carMapper.carToCarDTO(car);
		
	}
	//---------------- get all cars with page -----------------------------------------
	
	@Transactional(readOnly = true)
	public Page<CarDTO> findAllWithPage(Pageable pageable){
		return carRepository.findAllCarWithPage(pageable);
		
		 
	}
	
	//---------------- update a car -------------------------------------------------
	 @Transactional
	public void updateCar(Long id,String imageId, CarDTO carDTO) {
		
		Car foundCar = carRepository.findById(id).orElseThrow(()-> 
        new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
		
		ImageFile imFile =imageFileRepository.findById(imageId).
        orElseThrow(()-> new ResourceNotFoundException (String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, imageId)));
		
		if(foundCar.getBuiltIn()) {
			 throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		
		Set<ImageFile> imgs = foundCar.getImage();
		imgs.add(imFile);
		
		Car car = carMapper.carDTOToCar(carDTO);
		car.setId(foundCar.getId());
		car.setImage(imgs);
		
		carRepository.save(car);
		
	}
	
	//---------------- delete a car -------------------------------------------------
	
	public void removeById(Long id) {
		Car car = carRepository.findById(id).orElseThrow(()->
        new ResourceNotFoundException
        (String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
		
		
		boolean exists = reservationRepository.existsByCarId(car);
		
		//this part ensures to return more readable error message to the user, in stead of INTERNAL SERVER ERROR
		//wheh we try to use pre-flight methods and have CORST interception in browser
		if(exists) {
			throw new BadRequestException(ErrorMessage.CAR_USED_BY_RESERVATION_MESSAGE);
		}
		
		if(car.getBuiltIn()) {
			 throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		
		carRepository.deleteById(id);
	}
}
