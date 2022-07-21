package com.carrental.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* This entity allows ANONYMOUS USERS to send messages which will be kept in database  */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_cmessage")
public class ContactMessage implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Please provide your name")
	@Size(min=1, max=50, message = "Your name '${validatedValue}' must be {min} and {max} chars long")
	@Column(length = 50, nullable = false)
	private String name;
	
	@NotNull(message = "Please provide the subject of your message")
	@Size(min=5, max=50, message = "Your subject '${validatedValue}' must be {min} and {max} chars long")
	@Column(length = 50, nullable = false)
	private String subject;
	
	
	@NotNull(message = "Please provide your message body")
	@Size(min=20, max=200, message = "Your message '${validatedValue}' must be {min} and {max} chars long")
	@Column(length = 200,nullable = false)
	private String body;
	
	@Email(message="Provide correct email adress")
	@Column(length = 50,nullable=false)
	private String email;
	
	
	

}
