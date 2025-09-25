package com.hs.lab1.controller;

import com.hs.lab1.dto.UserDto;
import com.hs.lab1.entity.User;
import com.hs.lab1.mapper.UserMapper;
import com.hs.lab1.requests.CreateUserRequest;
import com.hs.lab1.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.addUser(
                request.username(),
                request.name(),
                request.surname());
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.toUserDtoList(users));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @Min(1) Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @GetMapping(path = "/by-username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable @NotBlank String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Min(1) Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
