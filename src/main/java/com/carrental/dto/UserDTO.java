package com.carrental.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.carrental.domain.Role;
import com.carrental.domain.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
 
public class UserDTO {
	
	 
	private Long id;
	 
	private String firstName;
		 
	private String lastName;	
	 
	private String email;
		 
	// private String password; password dönmicem onu siliyorum
		 
	private String phoneNumber;
		 
	private String address;
		 
	private String zipCode;
		 
	private Boolean builtIn=false;   
		 
	private Set<String> roles ; //burası neden Set<Role> değil de Set<String> ? bunu RoleType şeklinde dönmesine gerek yok
								//kendim istediğim gibi String şeklinde dönebilirim
	
	//dönüşümü sağlarken, veritabanındaki Role tipindeki rollerimi, String'e çevirmek için bu methodu yazdım
	
	public void setRoles(Set<Role> roles) {
		Set<String> rolesStr = new HashSet<>();
		
		roles.forEach(r->{
			if(r.getName().equals(RoleType.ROLE_ADMIN))
				rolesStr.add("Administrator");
			else
				rolesStr.add("Customer");
		});
		
		this.roles=rolesStr;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
