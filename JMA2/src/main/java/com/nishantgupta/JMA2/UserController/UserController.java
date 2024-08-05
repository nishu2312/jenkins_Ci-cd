package com.nishantgupta.JMA2.UserController;


import java.time.Instant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.nishantgupta.JMA2.Entity.PageInfo;

import com.nishantgupta.JMA2.Response.ErrorResponse;
import com.nishantgupta.JMA2.Service.UserService;
import com.nishantgupta.JMA2.Validators.Validator;
import com.nishantgupta.JMA2.Validators.ValidatorFactory;

@RestController
@ComponentScan(basePackages = "com.nishantgupta.JMA2")
@RequestMapping("/api")
public class UserController {


	 @Autowired
	    private UserService userService;

	    @PostMapping("randomUser")
	    public ResponseEntity<?> getRandomUserData(@RequestBody RandomUserRequest request) {
	        // Validate input parameters
	        Validator numericValidator = ValidatorFactory.createValidator("Numeric");
	   

	        System.out.println(request.getSize());
	        // Validate size is a numeric value and not greater than 5
	        if (!numericValidator.validate(String.valueOf(request.getSize())) || Integer.parseInt(request.getSize()) > 5 || Integer.parseInt(request.getSize()) < 1) {
	            return ResponseEntity.badRequest().body("Invalid size. Size should be a numeric value and not greater than 5.");
	        }
	  
	        return ResponseEntity.ok(userService.generateAndSaveRandomUsers(Integer.parseInt(request.getSize())));
	    }


	    @GetMapping("/users")
	    public ResponseEntity<?> getUsers(
	            @RequestParam(defaultValue = "5") String limit,
	            @RequestParam(defaultValue = "0") String offset,
	            @RequestParam(defaultValue = "Name") String sortType,
	            @RequestParam(defaultValue = "EVEN") String sortOrder) {

	        // Validate input parameters
	        Validator numericValidator = ValidatorFactory.createValidator("Numeric");
	        Validator characterValidator = ValidatorFactory.createValidator("Character");

	        if (!numericValidator.validate(limit) || !numericValidator.validate(offset)) {
	            return ResponseEntity.badRequest().body("Invalid limit or offset. They should be numeric values.");
	        }
 
	        if (!characterValidator.validate(sortType)) {
	            return ResponseEntity.badRequest().body("Invalid sortType. It should contain only English alphabets.");
	        }
	        
	        if (!characterValidator.validate(sortOrder)) {
	            return ResponseEntity.badRequest().body("Invalid sortOrder. It should contain only English alphabets.");
	        }
	        
	        // Fetch users with limit and offset and apply sorting
	        ResponseEntity<List<?>> users = userService.getUsersWithLimitOffsetAndSorting(Integer.parseInt(limit),
	                Integer.parseInt(offset), sortType, sortOrder);

	        
	    //  validation for limit
	        if (Integer.parseInt(limit) <= 0 || Integer.parseInt(limit) > 5) {
	        	 List<ErrorResponse> notFoundList = new ArrayList<>();
	        	 Instant timestamp = Instant.now(); 
               notFoundList.add(new ErrorResponse("Page not found", "404", timestamp));
	            return new ResponseEntity<>(notFoundList,HttpStatus.NOT_FOUND);
	        }

	        //  validation for sortType
	        if (!"name".equalsIgnoreCase(sortType) && !"age".equalsIgnoreCase(sortType)) {
	            return ResponseEntity.badRequest().body("Invalid sortType. It should be either 'name' or 'age'.");
	        }

	        //  validation for sortOrder
	        if (!"even".equalsIgnoreCase(sortOrder) && !"odd".equalsIgnoreCase(sortOrder)) {
	            return ResponseEntity.badRequest().body("Invalid sortOrder. It should be either 'EVEN' or 'ODD'.");
	        }

	        // Get total count of users in the database
	        int totalUsers = userService.getTotalUsersCount();

	        // Create PageInfo object
	        PageInfo pageInfo = new PageInfo();
	        pageInfo.setTotal(totalUsers);
	        pageInfo.setOffset(Integer.parseInt(offset));
	        pageInfo.setLimit(Integer.parseInt(limit));

	        // Create a custom response object
	        Map<String, Object> response = new HashMap<>();
	        response.put("users", users);
	        response.put("pageInfo", pageInfo.toMap()); // Use toMap() to format PageInfo

	        return ResponseEntity.ok(response);
	    }
	    
}


	




