package com.hibernateapp.dao;

import com.hibernateapp.model.User;
import com.hibernateapp.util.HibernateSessionFactoryUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Testcontainers
public class UserDAOTest {
    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private UserDAO userDAO;

    @BeforeEach
    void setUp(){
        HibernateSessionFactoryUtil hibernateSessionFactoryUtil = getHibernateSessionFactoryUtil();

        userDAO = new UserDAOImpl(hibernateSessionFactoryUtil.getSessionFactory());

        for (int i = 1; i < 6; i++) {
            User testUser = new User();
            testUser.setName("Linus" + i);
            testUser.setEmail(String.format("linus%d@gmail.com", i));
            testUser.setAge(i + 30);
            
            userDAO.save(testUser);
        }
    }

    private static @NotNull HibernateSessionFactoryUtil getHibernateSessionFactoryUtil() {
        Properties testProperties = new Properties();
        testProperties.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        testProperties.setProperty("hibernate.connection.username",postgres.getUsername());
        testProperties.setProperty("hibernate.connection.password",postgres.getPassword());
        testProperties.setProperty("hibernate.hbm2ddl.auto","create-drop");

        return new HibernateSessionFactoryUtil(testProperties);
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists(){
        User retrievedUser = userDAO.findById(1L).get();
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals("Linus1", retrievedUser.getName());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenUserNotExist(){
        Optional<User> retrievedUser = userDAO.findById(999L);
        Assertions.assertEquals(Optional.empty(), retrievedUser);
    }

    @Test
    void save_ShouldReturnSavedUser_WhenUserSaved(){
        User testUser = new User();
        testUser.setName("Artem");
        testUser.setAge(26);
        testUser.setEmail("iakonien@gmail.com");

        User savedUser = userDAO.save(testUser);

        Assertions.assertNotNull(savedUser);
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("Artem", savedUser.getName());

        Optional<User> foundUser = userDAO.findById(savedUser.getId());
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals("Artem", foundUser.get().getName());
    }

    @Test
    void update_ShouldReturnUpdatedUser_WhenUserUpdated(){
        User userFromDB = userDAO.findById(1L).get();

        User dataForUpdate = new User();
        dataForUpdate.setName("NotALinus");

        User updatedUser = userDAO.update(userFromDB.getId(), dataForUpdate);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(dataForUpdate.getName(), updatedUser.getName());
        Assertions.assertEquals(userFromDB.getId(), updatedUser.getId());

        Optional<User> foundUpdatedUser = userDAO.findById(updatedUser.getId());
        Assertions.assertTrue(foundUpdatedUser.isPresent());
        Assertions.assertEquals(dataForUpdate.getName(), foundUpdatedUser.get().getName());
    }

    @Test
    void delete_ShouldDeleteUser(){
        User userFromDB = userDAO.findById(1L).get();

        userDAO.delete(userFromDB.getId());

        Optional<User> deletedUser = userDAO.findById(userFromDB.getId());
        Assertions.assertTrue(deletedUser.isEmpty());
    }

    @Test
    void findAll_ShouldReturnListOfUser(){
        List<User> retrievedUsers = userDAO.findAll();

        Assertions.assertFalse(retrievedUsers.isEmpty());
        Assertions.assertEquals(5, retrievedUsers.size());
    }

}
