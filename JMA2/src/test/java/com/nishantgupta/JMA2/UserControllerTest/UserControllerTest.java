package com.nishantgupta.JMA2.UserControllerTest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nishantgupta.JMA2.Dto.UserDTO;
import com.nishantgupta.JMA2.Service.UserService;
import com.nishantgupta.JMA2.UserController.UserController;

@WebMvcTest()
class UserControllerTest {

	
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;
    
    
    

    @Test
    public void testGetUsersEndpoint() throws Exception {
        // Mocking the service layer
        List<UserDTO> mockUsers = Arrays.asList(
                new UserDTO("John Doe", "30", "Male", "1992-05-15", "US", "VERIFIED", Instant.now(), Instant.now()),
                new UserDTO("Jane Smith", "25", "Female", "1997-02-20", "CA", "TO_BE_VERIFIED", Instant.now(), Instant.now())
        );
        when(userService.getUsersWithLimitOffsetAndSorting(anyInt(), anyInt(), anyString(), anyString())).thenReturn(new ResponseEntity<>(mockUsers, HttpStatus.OK));
        when(userService.getTotalUsersCount()).thenReturn(100);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .param("limit", "5")
                .param("offset", "0")
                .param("sortType", "Name")
                .param("sortOrder", "EVEN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.users.body[0].name").value("John Doe"))
                .andExpect(jsonPath("$.users.body[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$.pageInfo.total").value(100));
    }

    @Test
    public void testGetRandomUserDataEndpoint() throws Exception {
        // Mocking the service layer
        when(userService.generateAndSaveRandomUsers(anyInt())).thenReturn(Arrays.asList("User 1", "User 2", "User 3","User 4", "User 5"));

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/randomUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"size\": \"5\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("User 1"))
                .andExpect(jsonPath("$[1]").value("User 2"))
                .andExpect(jsonPath("$[2]").value("User 3"))
                .andExpect(jsonPath("$[3]").value("User 4"))
                .andExpect(jsonPath("$[4]").value("User 5"))
               ;
    }
    
    @Test
    public void testGetUsersInvalidLimit() throws Exception {
    	
    	
        mockMvc.perform(get("/api/users")
                .param("limit", "abc")
                .param("offset", "0")
                .param("sortType", "Name")
                .param("sortOrder", "EVEN"))
                .andExpect(status().isBadRequest());
    }
    
    
    @Test
    public void testGetUsersLimitLessThanZero() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "-1")
                .param("offset", "0")
                .param("sortType", "Name")
                .param("sortOrder", "EVEN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUsersLimitGreaterThanFive() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "6")
                .param("offset", "0")
                .param("sortType", "Name")
                .param("sortOrder", "EVEN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUsersInvalidOffset() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "5")
                .param("offset", "xyz")
                .param("sortType", "Name")
                .param("sortOrder", "EVEN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUsersInvalidSortOrder() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "5")
                .param("offset", "0")
                .param("sortType", "Name")
                .param("sortOrder", "InvalidSortOrder"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testGetUsersInvalidSortType() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "5")
                .param("offset", "0")
                .param("sortType", "InvalidSortType")
                .param("sortOrder", "EVEN"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testGetRandomUserDataInvalidSize() throws Exception {
        mockMvc.perform(post("/api/randomUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"size\": \"abc\"}"))
                .andExpect(status().isBadRequest());
    }
}
