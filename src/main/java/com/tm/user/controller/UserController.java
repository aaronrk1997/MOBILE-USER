package com.tm.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tm.user.model.User;
import com.tm.user.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{name}")
    public List<User> getUserByName(@PathVariable(value = "name") String name) {
        return userRepository.findByName(name);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{name}")
    public User updateUser(@PathVariable(value = "name") String name, @RequestBody User userDetails) {
        User user = userRepository.findByName(name);
        user.setName(userDetails.getName());
        user.setFirstName(userDetails.getFirstName());
        user.setDateOfBirth(userDetails.getDateOfBirth());

        User updatedUser = userRepository.save(user);
        return updatedUser;
    }

    @DeleteMapping("/users/{name}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "name") String name) {
        User user = userRepository.findByName(name);
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }
    
}
