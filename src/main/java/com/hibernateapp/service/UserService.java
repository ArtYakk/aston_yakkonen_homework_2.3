package com.hibernateapp.service;

import com.hibernateapp.dao.UserDAO;
import com.hibernateapp.dao.UserDAOImpl;
import com.hibernateapp.exception.UserNotFoundException;
import com.hibernateapp.model.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAOImpl();

    public UserService() {
    }

    public User findUser(Long id){
        return userDAO.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));
    }

    public User saveUser(User user){
        return userDAO.save(user);
    }

    public void deleteUser(Long id){
        userDAO.delete(id);
    }

    public User updateUser(Long id, User data){
        return userDAO.update(id, data);
    }

    public List<User> findAllUsers(){
        return userDAO.findAll();
    }
}
