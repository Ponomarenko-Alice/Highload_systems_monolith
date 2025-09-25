package com.hs.lab1.service;

import com.hs.lab1.entity.Event;
import com.hs.lab1.entity.User;
import com.hs.lab1.exceptions.EventNotFoundException;
import com.hs.lab1.exceptions.UserNotFoundException;
import com.hs.lab1.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(String username, String name, String surname) {
        boolean exists = userRepository.existsByUsername(username);
        if (exists) throw new IllegalArgumentException("user with username = " + username + " already exists"); //TODO мб пользовательский эксепшен сделать

        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new UserNotFoundException("User with id = " + id + " not found");
        return user.get();
    }

    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) throw new UserNotFoundException("user with username = " + username + " does not exist");
        return user.get();
    }

    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new EventNotFoundException("User with id = " + id + " not found");
        userRepository.deleteById(id);
    }
}
