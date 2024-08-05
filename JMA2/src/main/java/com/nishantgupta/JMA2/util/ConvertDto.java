package com.nishantgupta.JMA2.util;

import com.nishantgupta.JMA2.Dto.UserDTO;
import com.nishantgupta.JMA2.Entity.User;

public class ConvertDto {

    public static User convertDTOToEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setDob(userDTO.getDob());
        user.setNationality(userDTO.getNationality());
        user.setVerificationStatus(userDTO.getVerificationStatus());
        user.setDateCreated(userDTO.getDateCreated());
        user.setDateModified(userDTO.getDateModified());
        // You may set other fields as needed
        return user;
    }

    public static UserDTO convertEntityToDTO(User user) {
        return new UserDTO(
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getDob(),
                user.getNationality(),
                user.getVerificationStatus(),
                user.getDateCreated(),
                user.getDateModified()
        );
    }
}
