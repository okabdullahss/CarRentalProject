package com.carrental.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

 

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carrental.domain.Car;
import com.carrental.domain.ImageFile;
import com.carrental.dto.CarDTO;
import com.carrental.dto.mapper.CarMapper;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.exception.message.ErrorMessage;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ImageFileRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CarService {

	private CarRepository carRepository;
	private ImageFileRepository imageFileRepository;
	private CarMapper carMapper;

	
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
}
