package com.hs.lab1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.lab1.dto.UserDto;
import com.hs.lab1.entity.User;
import com.hs.lab1.mapper.UserMapper;
import com.hs.lab1.requests.CreateUserRequest;
import com.hs.lab1.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void addUser_success() throws Exception {
        CreateUserRequest request = new CreateUserRequest("testuser", "John", "Doe");

        User user = new User();
        UserDto dto = new UserDto(1L, "testuser", "John","B", new ArrayList<>());

        when(userService.addUser(request.username(), request.name(), request.surname())).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(userService, times(1)).addUser(request.username(), request.name(), request.surname());
    }

    @Test
    void getAllUsers_success() throws Exception {
        User user = new User();
        UserDto dto = new UserDto(1L, "testuser", "John","B", new ArrayList<>());

        when(userService.getAllUsers()).thenReturn(List.of(user));
        when(userMapper.toUserDtoList(List.of(user))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getUserById_success() throws Exception {
        User user = new User();
        UserDto dto = new UserDto(1L, "testuser", "John","B", new ArrayList<>());

        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserByUsername_success() throws Exception {
        User user = new User();
        UserDto dto = new UserDto(1L, "testuser", "John","B", new ArrayList<>());

        when(userService.getUserByUsername("testuser")).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/user/by-username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteUserById_success() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(1L);
    }
}
