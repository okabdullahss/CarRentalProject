package com.carrental.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.carrental.domain.User;
import com.carrental.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO userToUserDTO(User user);
	List<UserDTO> map(List<User> user);
}
