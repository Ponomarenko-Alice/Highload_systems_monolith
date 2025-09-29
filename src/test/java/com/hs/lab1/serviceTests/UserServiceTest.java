package com.hs.lab1.serviceTests;

import com.hs.lab1.entity.User;
import com.hs.lab1.exceptions.UserNotFoundException;
import com.hs.lab1.repository.UserRepository;
import com.hs.lab1.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void addUser_success() {
        User user = new User();
        user.setUsername("test");
        when(userRepository.existsByUsername("test")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.addUser("test", "Name", "Surname");

        assertEquals("test", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addUser_usernameExists_throwsException() {
        when(userRepository.existsByUsername("test")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.addUser("test", "Name", "Surname"));
    }

    @Test
    void getUserById_found() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(user, result);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void getUserByUsername_found() {
        User user = new User();
        user.setUsername("test");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("test");

        assertEquals(user, result);
    }

    @Test
    void getUserByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserByUsername("test"));
    }

    @Test
    void getAllUsers_success() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void deleteUserById_success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> userService.deleteUserById(1L));
    }
}
