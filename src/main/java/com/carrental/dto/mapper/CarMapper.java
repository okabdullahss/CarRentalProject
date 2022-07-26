package com.carrental.dto.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.carrental.domain.Car;
import com.carrental.domain.ImageFile;
import com.carrental.dto.CarDTO;

@Mapper(componentModel = "spring")
public interface CarMapper {

	@Mapping(target ="image",ignore = true)
	Car carDTOToCar(CarDTO carDTO);
	
	@Mapping(source="image",target="image",qualifiedByName = "getImageAsString")
	CarDTO carToCarDTO(Car car);//at this point, images in CarDTO are String type and images in Car are ImageFile type
								//we must covert ImageFile types into String, because return type is CarDTO(which is using String
								//type images)
	
	@Named("getImageAsString")
	public static Set<String> getImageId(Set<ImageFile> images){
		
		Set<String> imgs = new HashSet<>();
		
		imgs = images.stream().map(image-> image.getId().toString()).collect(Collectors.toSet());
		
		return imgs;
		
	}
	
	List<CarDTO> map(List<Car> cars);
	
}
