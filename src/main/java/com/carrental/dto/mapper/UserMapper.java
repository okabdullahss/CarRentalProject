package com.carrental.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.carrental.domain.User;
import com.carrental.dto.UserDTO;
import com.carrental.dto.request.AdminUserUpdateRequest;


@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO userToUserDTO(User user);
	List<UserDTO> map(List<User> user);
	
	@Mapping(target = "id", ignore = true) // we have id, and roles(as Role type) in User entity class
	@Mapping(target = "roles", ignore = true)// but we dont have these two in our dto class, so we ignore them during mapping
	User adminUserUpdateRequestToUser(AdminUserUpdateRequest request);
	 
}
