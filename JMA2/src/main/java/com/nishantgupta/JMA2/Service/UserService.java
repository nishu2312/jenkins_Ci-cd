package com.nishantgupta.JMA2.Service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nishantgupta.JMA2.Dto.UserDTO;
import com.nishantgupta.JMA2.Entity.User;


public interface UserService {
	
	  
	ResponseEntity<String> getRandomUserData();
	  
    String extractFirstName(String result);

    String extractLastName(String result);

    String extractUserGender(String result);

    String extractUserNationality(String result);

    String extractUserDob(String result);

    LocalDate parseDate(String dob);

    List<String> generateAndSaveRandomUsers(int size);

    String verificationStatus(String userData, String nationalityData, String genderData);

	String extractAge(String result);
	
	 public int getTotalUsersCount();
	
	 public  ResponseEntity<List<?>> getUsersWithLimitOffsetAndSorting(int limit, int offset, String sortType, String sortOrder);

//  
	
	
}

