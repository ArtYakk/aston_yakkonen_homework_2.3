package com.hibernateapp.dao;

import com.hibernateapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findById(Long id);
    User save(User user);
    User update(Long id, User user);
    void delete(Long id);
    List<User> findAll();
}
