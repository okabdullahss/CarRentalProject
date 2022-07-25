package com.carrental.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.carrental.domain.ImageFile;
import com.carrental.dto.response.ImageSavedResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.ImageFileService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class ImageFileController {

	private ImageFileService imageFileService;
	
	//-----------------------UPLOAD A CAR IMAGE METHOD------------------------------------------------------------------	  
	
	@PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file") MultipartFile file){
		String imageId = imageFileService.saveImage(file);
		
		ImageSavedResponse response = new ImageSavedResponse();
		response.setImageId(imageId);
		response.setMessage(ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		//added some configs in application.yml in order NOT to get images, that exceeds the specified size
		
		
	}
	
	//-----------------------DOWNLOAD CAR IMG(NO AUTH REQUIRED) BY ANONYMOUS USER------------------------------------------
	
	//this method is being allowed on WebSecurityConfig section, since this method doesnt require any authorisation
	  @GetMapping("/download/{id}")
	  public ResponseEntity<byte []> getImageFile(@PathVariable String id){
		  ImageFile imageFile = imageFileService.getImageById(id);
		  
		  //when downloaded, image data will be saved with its own name(this is what content_dip attachment" is all about)
		  return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+
				  imageFile.getName()).body(imageFile.getData());
		  
	  }
	
		//-----------------------DISPLAY A CAR IMG(NO AUTH REQUIRED) BY ANONYMOUS USER------------------------------------------
	
	//this method is being allowed on WebSecurityConfig section, since this method doesnt require any authorisation
	
	  @GetMapping("/display/{id}")
	  public ResponseEntity<byte []> displayImageFile(@PathVariable String id){
		  
		  //this method uses the same service method as download image, which is getImageById()
	    	ImageFile imageFile = imageFileService.getImageById(id);
	    	
	    	HttpHeaders header = new HttpHeaders();
	    	header.setContentType(MediaType.IMAGE_JPEG);//which format will be using when displaying the image
	    	
	    	return new ResponseEntity<>(imageFile.getData(),header,HttpStatus.OK);
	    	
	    }
}
