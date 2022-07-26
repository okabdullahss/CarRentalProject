package com.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrental.domain.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}
