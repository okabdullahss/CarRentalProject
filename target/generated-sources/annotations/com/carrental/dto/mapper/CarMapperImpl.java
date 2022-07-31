package com.carrental.dto.mapper;

import com.carrental.domain.Car;
import com.carrental.dto.CarDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-31T12:55:32+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class CarMapperImpl implements CarMapper {

    @Override
    public Car carDTOToCar(CarDTO carDTO) {
        if ( carDTO == null ) {
            return null;
        }

        Car car = new Car();

        car.setId( carDTO.getId() );
        car.setModel( carDTO.getModel() );
        car.setDoors( carDTO.getDoors() );
        car.setSeats( carDTO.getSeats() );
        car.setLuggage( carDTO.getLuggage() );
        car.setTransmission( carDTO.getTransmission() );
        car.setAirConditioning( carDTO.getAirConditioning() );
        car.setAge( carDTO.getAge() );
        car.setPricePerHour( carDTO.getPricePerHour() );
        car.setFuelType( carDTO.getFuelType() );
        car.setBuiltIn( carDTO.getBuiltIn() );

        return car;
    }

    @Override
    public CarDTO carToCarDTO(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();

        carDTO.setImage( CarMapper.getImageId( car.getImage() ) );
        carDTO.setId( car.getId() );
        carDTO.setModel( car.getModel() );
        carDTO.setDoors( car.getDoors() );
        carDTO.setSeats( car.getSeats() );
        carDTO.setLuggage( car.getLuggage() );
        carDTO.setTransmission( car.getTransmission() );
        carDTO.setAirConditioning( car.getAirConditioning() );
        carDTO.setAge( car.getAge() );
        carDTO.setPricePerHour( car.getPricePerHour() );
        carDTO.setFuelType( car.getFuelType() );
        carDTO.setBuiltIn( car.getBuiltIn() );

        return carDTO;
    }

    @Override
    public List<CarDTO> map(List<Car> cars) {
        if ( cars == null ) {
            return null;
        }

        List<CarDTO> list = new ArrayList<CarDTO>( cars.size() );
        for ( Car car : cars ) {
            list.add( carToCarDTO( car ) );
        }

        return list;
    }
}
