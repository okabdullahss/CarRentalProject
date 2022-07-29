package com.carrental.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.carrental.domain.Car;
import com.carrental.domain.Reservation;
import com.carrental.domain.User;
import com.carrental.dto.request.ReservationRequest;
import com.carrental.helper.ExcelReportHelper;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReportService {
	
	UserRepository userRepository;
	CarRepository carRepository;
	ReservationRepository reservationRepository;
	//-------------CREATE USER INFOS AS EXCEL REPORT -------------------------
  	
	// org.apache.poi dependency is added into pom.xml to use reporting mechanism
    public ByteArrayInputStream getUserReport() throws IOException {
        List<User> users= userRepository.findAll();
        return ExcelReportHelper.getUsersExcelReport(users);
    }
  //-------------CREATE CAR INFOS AS EXCEL REPORT------------------------- 
    public ByteArrayInputStream getCarReport() throws IOException {
        List<Car> cars= carRepository.findAll();
        return ExcelReportHelper.getCarsExcelReport(cars);
    }
    
  //-------------CREATE RESERVATION INFOS AS EXCEL REPORT------------------------- 
    public ByteArrayInputStream getReservationReport() throws IOException {
        List<Reservation> reservations= reservationRepository.findAll();
        return ExcelReportHelper.getReservationExcelReport(reservations);
    } 
    
    

}
