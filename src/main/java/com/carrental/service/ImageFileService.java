package com.carrental.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.carrental.domain.ImageFile;
import com.carrental.dto.ImageFileDTO;
import com.carrental.exception.ImageFileException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.exception.message.ErrorMessage;
import com.carrental.repository.ImageFileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageFileService {

	private ImageFileRepository imageFileRepository;
	
	 //------------------------UPLOAD A CAR IMAGE METHOD------------------------------------------------------------------	
	
	public String saveImage(MultipartFile file) {
		
		//get the original path of the image file
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		ImageFile imageFile = null;
		try {
			imageFile = new ImageFile(fileName, file.getContentType(), file.getBytes());
		} catch (IOException e) {
			throw new ImageFileException(e.getMessage());
		}
		
		imageFileRepository.save(imageFile);
		
		return imageFile.getId(); // i want to return only id of the image to the controller side, not the whole image
		
	}
	
	//-----------------------DOWNLOAD CAR IMG(NO AUTH REQUIRED) BY ANONYMOUS USER------------------------------------------
	
	public ImageFile getImageById(String id) {
		ImageFile imageFile=  imageFileRepository.findById(id).
				orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));
		
		return imageFile;
	}
	
	//-----------------------DOWNLOAD ALL CAR IMAGES --------------------------------------------------------------
	
	public List<ImageFileDTO> getAllImages(){
		
		List<ImageFile> imageList = imageFileRepository.findAll();
		
		
		List<ImageFileDTO> files = imageList.stream().map(imFile -> {
			
		 String imageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				 .path("files/download/")
				 .path(imFile.getId())
				 .toUriString();
		 
		 return new ImageFileDTO(imFile.getName(),imageUri,imFile.getType(),imFile.getData().length);
			
		}).collect(Collectors.toList());
		
		return files;
	}
	
	
}
