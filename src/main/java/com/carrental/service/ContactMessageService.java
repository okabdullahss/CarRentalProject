package com.carrental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carrental.domain.ContactMessage;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.exception.message.ErrorMessage;
import com.carrental.repository.ContactMessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContactMessageService {

	@Autowired
	private ContactMessageRepository contactMessageRepository;
	
//------------------------CREATE NEW CONTACT MESSAGE---------------------------------------------------
	public void createContactMessage(ContactMessage contactMessage) {
		
		contactMessageRepository.save(contactMessage);
	}
	
//------------------------GET ALL CONTACT MESSAGES------------------------------------------------------	
	
	public List<ContactMessage> getAllContactMessage(){
		List<ContactMessage> messages = contactMessageRepository.findAll();
		
		return messages;
	}
	
//------------------------GET A CONTACT MESSAGE WITH AN ID ----------------------------------------------	
	
	public ContactMessage getContactMessageWithId(Long id) {
	  return  contactMessageRepository.findById(id).orElseThrow(
	    		()-> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
	}
	
//------------------------DELETE A CONTACT MESSAGE WITH AN ID -------------------------------------------		
	
	public void deleteContactMessageWithId(Long id) {
		ContactMessage message = getContactMessageWithId(id);
		contactMessageRepository.delete(message);
 
	}
	
//------------------------UPDATE A CONTACT MESSAGE WITH AN ID --------------------------------------------
	
	public void updateContactMessage(ContactMessage newContactMessage, Long id) {
		
		ContactMessage foundMessage = getContactMessageWithId(id);
		
		foundMessage.setName(newContactMessage.getName());
		foundMessage.setBody(newContactMessage.getBody());
		foundMessage.setEmail(newContactMessage.getEmail());
		foundMessage.setSubject(newContactMessage.getSubject());
		
		contactMessageRepository.save(foundMessage);
		
	}
//------------------------GET ALL CONTACT MESSAGES WITH PAGE------------------------------------------------------	
	public Page<ContactMessage> getAllWithPage(Pageable page){
		return contactMessageRepository.findAll(page);
	}
	
}
