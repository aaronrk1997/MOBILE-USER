package com.tm.user.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.user.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByName(String name);
    List<User> findByNameContains(String name);
}
