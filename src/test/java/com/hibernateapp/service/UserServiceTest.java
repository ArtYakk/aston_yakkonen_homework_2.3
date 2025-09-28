package com.hibernateapp.service;

import com.hibernateapp.dao.UserDAO;
import com.hibernateapp.exception.UserNotFoundException;
import com.hibernateapp.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    UserDAO userDAOMock;

    @InjectMocks
    UserService userService;

    @Test
    void findUser_ShouldReturnUser_WhenUserExists()
    {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Linus");
        Mockito.when(userDAOMock.findById(1L)).thenReturn(Optional.of(testUser));


        User retrievedUser = userService.findUser(1L);
        System.out.println(retrievedUser.getName());
        Assertions.assertEquals(testUser.getName(), retrievedUser.getName());
        Mockito.verify(userDAOMock, Mockito.times(1)).findById(1L);
    }

    @Test
    void findUser_ShouldThrowNotFound_WhenUserNotExists()
    {
        Mockito.when(userDAOMock.findById(1L)).thenReturn(Optional.empty());
        Supplier<User> retrieveUser = () -> userService.findUser(1L);
        Assertions.assertThrows(UserNotFoundException.class, retrieveUser::get);
        Mockito.verify(userDAOMock, Mockito.times(1)).findById(1L);
    }

    @Test
    void saveUser_ShouldReturnSavedUser_WhenUserSaved()
    {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Linus");

        Mockito.when(userDAOMock.save(testUser)).thenReturn(testUser);

        User savedUser = userService.saveUser(testUser);
        Assertions.assertEquals(testUser.getId(), savedUser.getId());
        Mockito.verify(userDAOMock).save(testUser);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserUpdated()
    {
        User updatedTestUser = new User();
        updatedTestUser.setId(1L);
        updatedTestUser.setName("LinusTorvalds");


        Mockito.when(userDAOMock.update(1L, updatedTestUser)).thenReturn(updatedTestUser);

        User retrievedUpdatedTestUser = userService.updateUser(1L, updatedTestUser);

        Assertions.assertEquals("LinusTorvalds", retrievedUpdatedTestUser.getName());
        Mockito.verify(userDAOMock, Mockito.times(1)).update(1L, updatedTestUser);
    }

    @Test
    void findAllUsers_ShouldReturnEmptyList_WhenGetAllUsersWithNoUsersSaved()
    {

        Mockito.when(userDAOMock.findAll()).thenReturn(List.of());

        List<User> retrievedUsers = userService.findAllUsers();

        Assertions.assertNotNull(retrievedUsers);
        Assertions.assertEquals(0, retrievedUsers.size());
        Mockito.verify(userDAOMock, Mockito.times(1)).findAll();
    }

    @Test
    void findAllUsers_ShouldReturnListOfUsers_WhenGetAllUsers()
    {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setId((long) i);
            user.setName("Linus" + i);
            users.add(user);
        }

        Mockito.when(userDAOMock.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.findAllUsers();

        Assertions.assertNotNull(retrievedUsers);
        Assertions.assertEquals(3, retrievedUsers.size());
        Assertions.assertEquals(users.getFirst().getId(), retrievedUsers.getFirst().getId());
        Mockito.verify(userDAOMock, Mockito.times(1)).findAll();
    }
}
