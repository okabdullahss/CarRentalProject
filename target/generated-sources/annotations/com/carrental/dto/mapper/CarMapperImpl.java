package com.carrental.dto.mapper;

import com.carrental.domain.Car;
import com.carrental.dto.CarDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-26T14:29:19+0300",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 1.4.50.v20210914-1429, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class CarMapperImpl implements CarMapper {

    @Override
    public Car carDTOToCar(CarDTO carDTO) {
        if ( carDTO == null ) {
            return null;
        }

        Car car = new Car();

        car.setAge( carDTO.getAge() );
        car.setAirConditioning( carDTO.getAirConditioning() );
        car.setBuiltIn( carDTO.getBuiltIn() );
        car.setDoors( carDTO.getDoors() );
        car.setFuelType( carDTO.getFuelType() );
        car.setId( carDTO.getId() );
        car.setLuggage( carDTO.getLuggage() );
        car.setModel( carDTO.getModel() );
        car.setPricePerHour( carDTO.getPricePerHour() );
        car.setSeats( carDTO.getSeats() );
        car.setTransmission( carDTO.getTransmission() );

        return car;
    }

    @Override
    public CarDTO carToCarDTO(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();

        carDTO.setImage( CarMapper.getImageId( car.getImage() ) );
        carDTO.setAge( car.getAge() );
        carDTO.setAirConditioning( car.getAirConditioning() );
        carDTO.setBuiltIn( car.getBuiltIn() );
        carDTO.setDoors( car.getDoors() );
        carDTO.setFuelType( car.getFuelType() );
        carDTO.setId( car.getId() );
        carDTO.setLuggage( car.getLuggage() );
        carDTO.setModel( car.getModel() );
        carDTO.setPricePerHour( car.getPricePerHour() );
        carDTO.setSeats( car.getSeats() );
        carDTO.setTransmission( car.getTransmission() );

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
