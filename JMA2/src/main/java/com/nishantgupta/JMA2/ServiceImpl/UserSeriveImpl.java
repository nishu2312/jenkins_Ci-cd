package com.nishantgupta.JMA2.ServiceImpl;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nishantgupta.JMA2.Dto.UserDTO;
import com.nishantgupta.JMA2.Entity.User;
import com.nishantgupta.JMA2.Repo.UserRepository;
import com.nishantgupta.JMA2.Response.ErrorResponse;
import com.nishantgupta.JMA2.Service.UserService;
import com.nishantgupta.JMA2.util.ConvertDto;
import com.nishantgupta.JMA2.util.Comparator.UserComparator;


@Service
public class UserSeriveImpl implements UserService  {
	
	
	 @Autowired
	    private UserRepository userRepository;
	 
	  @Autowired
	    private WebClient api1WebClient;

	    @Autowired
	    private WebClient api2WebClient;

	    @Autowired
	    private WebClient api3WebClient;

         
	    @Override
	    public  List<String> generateAndSaveRandomUsers(int size) {
	        List<String> responses = IntStream.range(0, size)
	                .mapToObj(i -> getRandomUserData().getBody()+ "\n")
	                .collect(Collectors.toList());

	        return responses;
	    }
	    
	  
	    @Override
	    public ResponseEntity<List<?>> getUsersWithLimitOffsetAndSorting(int limit, int offset, String sortType, String sortOrder) {

	        if ((limit<=5 && limit>0) && ((sortType.equalsIgnoreCase("age") || sortType.equalsIgnoreCase("name")) &&
	                (sortOrder.equalsIgnoreCase("even") || sortOrder.equalsIgnoreCase("odd")))) {
	            // Fetch all users from the repository and convert them to UserDTO
	            List<UserDTO> allUserDTOs = userRepository.findAll()
	                    .stream()
	                    .map(user -> new UserDTO(
	                            user.getName(),
	                            String.valueOf(user.getAge()),
	                            user.getGender(),
	                            user.getDob(),
	                            user.getNationality(),
	                            user.getVerificationStatus(),
	                            user.getDateCreated(),
	                            user.getDateModified()))
	                    .collect(Collectors.toList());

	            // Validate and adjust limit and offset
	            int startIndex = Math.min(offset, allUserDTOs.size());
	            int endIndex = Math.min(startIndex + limit, allUserDTOs.size());
	            List<UserDTO> sublist = allUserDTOs.subList(startIndex, endIndex);

	            // Apply sorting based on sortType and sortOrder to the sublist
	            Comparator<UserDTO> comparator;

	            if ("Age".equalsIgnoreCase(sortType)) {
	                comparator = Comparator.comparing(userDTO -> Integer.parseInt(userDTO.getAge()));
	            } else if ("Name".equalsIgnoreCase(sortType)) {
	                comparator = Comparator.comparingInt(userDTO -> UserComparator.countCharactersWithoutSpaces(userDTO.getName()));
	            } else { // Default to "Name" if sortType is not recognized
	                comparator = Comparator.comparing(UserDTO::getName);
	            }

	            // Sort the sublist
	            sublist.sort(comparator);

	            if (sortType.equalsIgnoreCase("age")) {
	                // Apply additional sorting based on sortOrder
	                if ("ODD".equalsIgnoreCase(sortOrder)) {
	                    sublist = sublist.stream()
	                            .filter(userDTO -> Integer.parseInt(userDTO.getAge()) % 2 != 0) // Odd ages
	                            .collect(Collectors.toList());
	                } else if ("EVEN".equalsIgnoreCase(sortOrder)) {
	                    sublist = sublist.stream()
	                            .filter(userDTO -> Integer.parseInt(userDTO.getAge()) % 2 == 0) // Even ages
	                            .collect(Collectors.toList());
	                }
	                // Default to no additional sorting if sortOrder is not recognized
	            } else if (sortType.equalsIgnoreCase("name")) {
	                if ("ODD".equalsIgnoreCase(sortOrder)) {
	                    sublist = sublist.stream()
	                            .filter(userDTO -> UserComparator.countCharactersWithoutSpaces(userDTO.getName()) % 2 != 0) // Odd character count for Name
	                            .collect(Collectors.toList());
	                } else if ("EVEN".equalsIgnoreCase(sortOrder)) {
	                    sublist = sublist.stream()
	                            .filter(userDTO -> UserComparator.countCharactersWithoutSpaces(userDTO.getName()) % 2 == 0) // Even character count for Name
	                            .collect(Collectors.toList());
	                }
	            }
	            return new ResponseEntity<>(sublist, HttpStatus.OK);
	        } else {
	        	  List<ErrorResponse> notFoundList = new ArrayList<>();
	        	  Instant timestamp = Instant.now(); 
	              notFoundList.add(new ErrorResponse("Page not found", "404", timestamp));

	              return new ResponseEntity<>(notFoundList, HttpStatus.NOT_FOUND);
	        }
	    }


	    
	    @Override
	    public ResponseEntity<String> getRandomUserData() {
	       
	        // Make a GET request to the random user API
	        String result = api1WebClient.get()
	                .uri("/")
	                .retrieve()
	                .bodyToMono(String.class)
	                .block();

	        // Extract the first name from the random user data
	        String firstName = extractFirstName(result);
	        String lastName = extractLastName(result);
	        String DOB = extractUserDob(result);
	        String nationality = extractUserNationality(result);
	        String gender = extractUserGender(result);
	        String dob = extractUserDob(result);
            LocalDate birthDate = parseDate(dob);
            String age = extractAge(result);
          
	        

	        // Execute API calls in parallel using ExecutorService
	        ExecutorService executorService = Executors.newFixedThreadPool(2);

	      

	        CompletableFuture<String> nationalityFuture = CompletableFuture.supplyAsync(() ->
	                api2WebClient.get()
	                        .uri("/?name=" + firstName)
	                        .retrieve()
	                        .bodyToMono(String.class)
	                        .block(), executorService);

	        CompletableFuture<String> genderFuture = CompletableFuture.supplyAsync(() ->
	                api3WebClient.get()
	                        .uri("/?name=" + firstName)
	                        .retrieve()
	                        .bodyToMono(String.class)
	                        .block(), executorService);

	        // Combine the results of parallel API calls
	        try {
	            String nationalityResult = nationalityFuture.get();
	            String genderResult = genderFuture.get();

	            // Determine the verification status
	            String verificationStatus = verificationStatus(result, nationalityResult, genderResult);


	            if (birthDate != null) {
	            	 UserDTO userDTO = new UserDTO();
                     userDTO.setName(firstName + " " + lastName);
                     userDTO.setAge(extractAge(result));
                     userDTO.setGender(extractUserGender(result));
                     userDTO.setDob(extractUserDob(result));
                     userDTO.setNationality(extractUserNationality(result));
                     userDTO.setVerificationStatus(verificationStatus);
                     userDTO.setDateCreated(Instant.now());
                     userDTO.setDateModified(Instant.now());

                     User user = ConvertDto.convertDTOToEntity(userDTO);
                     userRepository.save(user);

                 
	            }

	            // Return the response with verification status
	            String response = 
	            		
	            		"API 1" + 
	                      "\nName: " + firstName + " " + lastName  +
	            		  "\nDOB: " + DOB +
	            		  "\nNat: " + nationality +
	            		  "\nAge: " + age +
	            		  "\nUserGender: " + gender +
	            		  "\nAPI 2" +
	                      "\nNationality: " + nationalityResult +
	                      "\nAPI 3" +
	                      "\nGender: " + genderResult +
	                      "\nVerification Status: " + verificationStatus;

	            // Set VERIFIED status in response header if applicable
	            if ("VERIFIED".equals(verificationStatus)) {
	                return ResponseEntity.ok().header("Verification-Status", "VERIFIED").body(response);
	            } else {
	                return ResponseEntity.ok().body(response);
	            }
	        } catch (InterruptedException | ExecutionException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error occurred while fetching data.");
	        } finally {
	            // Shutdown the executor service
	            executorService.shutdown();
	        }
	    }
	

	    @Override
	    public String extractFirstName(String result) {
	    	  try {
	                ObjectMapper objectMapper = new ObjectMapper();
	                JsonNode rootNode = objectMapper.readTree(result);

	                Optional<String> firstName = Optional.ofNullable(rootNode.path("results")
	                        .elements()
	                        .next())  // Assuming there's at least one result
	                        .map(resultNode -> resultNode.path("name").path("first").asText());

	                return firstName.orElse("");
	            } catch (IOException e) {
	                e.printStackTrace();
	                
	                return "";
	            }
	        }
	    

	    @Override
	    public String extractAge(String result) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(result);

	            Optional<String> age = Optional.ofNullable(rootNode.path("results")
	                    .elements()
	                    .next())  // Assuming there's at least one result
	                    .map(resultNode -> resultNode.path("dob").path("age").asText());

	            return age.orElse("");
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "";
	        }
	    }


	    public String extractLastName(String result) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(result);

	            return Optional.ofNullable(rootNode.path("results").elements().next())
	                    .map(resultNode -> resultNode.path("name").path("last").asText())
	                    .orElse("");
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "";
	        }
	    }

	    public String extractUserGender(String result) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(result);

	            return Optional.ofNullable(rootNode.path("results").elements().next())
	                    .map(resultNode -> resultNode.path("gender").asText())
	                    .orElse("");
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "";
	        }
	    }

	    public String extractUserNationality(String result) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(result);

	            return Optional.ofNullable(rootNode.path("results").elements().next())
	                    .map(resultNode -> resultNode.path("nat").asText())
	                    .orElse("");
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "";
	        }
	    }

	    public String extractUserDob(String result) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(result);

	            return Optional.ofNullable(rootNode.path("results").elements().next())
	                    .map(resultNode -> resultNode.path("dob").path("date").asText())
	                    .orElse("");
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "";
	        }
	    }

	    public LocalDate parseDate(String dob) {
	        try {
	            Instant instant = Instant.parse(dob);
	            return instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    @Override
	    public int getTotalUsersCount() {
	    	 return (int) userRepository.count();
	    }

	    @Override
	    public String verificationStatus(String userData, String nationalityData, String genderData) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode userNode = objectMapper.readTree(userData);
	            JsonNode nationalityNode = objectMapper.readTree(nationalityData);
	            JsonNode genderNode = objectMapper.readTree(genderData);

	            String userNationality = userNode.path("results").get(0).path("nat").asText();
	            String userGender = userNode.path("results").get(0).path("gender").asText();

	            boolean nationalityMatches = false;
	            JsonNode countryArray = nationalityNode.path("country");
	            for (int i = 0; i < countryArray.size(); i++) {
	                JsonNode countryNode = countryArray.get(i);
	                if (countryNode.path("country_id").asText().equals(userNationality)) {
	                    nationalityMatches = true;
	                    break;
	                }
	            }

	            boolean genderMatches = genderNode.path("gender").asText().equalsIgnoreCase(userGender);

	            if (nationalityMatches && genderMatches) {
	                return "VERIFIED";
	            } else {
	                return "TO_BE_VERIFIED";
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "ERROR";
	        }
	    }


		
	}
