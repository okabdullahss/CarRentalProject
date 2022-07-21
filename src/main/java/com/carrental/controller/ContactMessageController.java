package com.carrental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.domain.ContactMessage;
import com.carrental.service.ContactMessageService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/contactmessage")
@AllArgsConstructor
public class ContactMessageController {
	
	private ContactMessageService contactMessageService;

//------------------------CREATE NEW CONTACT MESSAGE---------------------------------------------------
	@PostMapping("/visitors")
	public ResponseEntity<Map<String,String>> createMessage(@Valid @RequestBody ContactMessage contactMessage){
		
		Map<String,String> map = new HashMap<>();
		map.put("message","Contact Message Created Sucesfully");
		map.put("status","true");
		
		contactMessageService.createContactMessage(contactMessage);
		
		return new ResponseEntity(map,HttpStatus.CREATED);	
	}
	
//------------------------GET ALL CONTACT MESSAGES------------------------------------------------------
	@GetMapping
	public ResponseEntity<List<ContactMessage>> getAllContactMessages(){
		
		List<ContactMessage> allMessages = contactMessageService.getAllContactMessage();
		
		return ResponseEntity.ok(allMessages);
	}
	
//------------------------GET A CONTACT MESSAGE WITH AN ID / PATHVARIABLE ----------------------------------------------
	
	@GetMapping("/{id}")
	public ResponseEntity<ContactMessage> getContactMessageWithIdPathVar(@PathVariable("id") Long id){
		
		ContactMessage contactMessageWithId = contactMessageService.getContactMessageWithId(id);
		
		return ResponseEntity.ok(contactMessageWithId);
		
	}
	
//------------------------GET A CONTACT MESSAGE WITH AN ID /REQUESTPARAM ----------------------------------------------	
	
	@GetMapping("/request")
	public ResponseEntity<ContactMessage> getContactMessageWithIdReqParam(@RequestParam("id") Long id){
		
		ContactMessage contactMessageWithId = contactMessageService.getContactMessageWithId(id);
		
		return ResponseEntity.ok(contactMessageWithId);
		
	}
	
//------------------------UPDATE A CONTACT MESSAGE WITH AN ID ----------------------------------------------
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String,String>> updateContactMessage(@PathVariable("id") Long id, @Valid @RequestBody ContactMessage message){
		
		contactMessageService.updateContactMessage(message, id);
		
		Map<String,String> map = new HashMap<>();
		map.put("message","Contact Message Updated Sucesfully");
		map.put("status","true");
				
		return new ResponseEntity(map,HttpStatus.OK);
		
	}
	
//------------------------DELETE A CONTACT MESSAGE WITH AN ID ----------------------------------------------	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String,String>> deleteContactMessage(@PathVariable("id") Long id){
		
		contactMessageService.deleteContactMessageWithId(id);
		
		Map<String,String> map = new HashMap<>();
		map.put("message","Contact Message Deleted Sucesfully");
		map.put("status","true");
				
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
//------------------------GET ALL CONTACT MESSAGES WITH PAGES ----------------------------------------------	
	
	@GetMapping("/pages")
	public ResponseEntity<Page<ContactMessage>> getAllMessagesWithPage(@RequestParam("page") int page, @RequestParam("size") int size,
																	   @RequestParam("sort") String prop, @RequestParam("direction") Direction direction){
		
		Pageable pageable = PageRequest.of(page, size,Sort.by(direction,prop));
		Page<ContactMessage> allWithPage = contactMessageService.getAllWithPage(pageable);
		
		return ResponseEntity.ok(allWithPage);
		
	}
	
	
	
	
	
	
	
	
}
