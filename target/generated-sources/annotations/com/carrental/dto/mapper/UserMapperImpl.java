package com.carrental.dto.mapper;

import com.carrental.domain.Role;
import com.carrental.domain.User;
import com.carrental.dto.UserDTO;
import com.carrental.dto.request.AdminUserUpdateRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-27T11:27:57+0300",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 1.4.50.v20210914-1429, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setAddress( user.getAddress() );
        userDTO.setBuiltIn( user.getBuiltIn() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setId( user.getId() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setPhoneNumber( user.getPhoneNumber() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            userDTO.setRoles( new HashSet<Role>( set ) );
        }
        userDTO.setZipCode( user.getZipCode() );

        return userDTO;
    }

    @Override
    public List<UserDTO> map(List<User> user) {
        if ( user == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( user.size() );
        for ( User user1 : user ) {
            list.add( userToUserDTO( user1 ) );
        }

        return list;
    }

    @Override
    public User adminUserUpdateRequestToUser(AdminUserUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setAddress( request.getAddress() );
        user.setBuiltIn( request.getBuiltIn() );
        user.setEmail( request.getEmail() );
        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setPassword( request.getPassword() );
        user.setPhoneNumber( request.getPhoneNumber() );
        user.setZipCode( request.getZipCode() );

        return user;
    }
}
