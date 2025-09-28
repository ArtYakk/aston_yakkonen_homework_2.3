package com.hibernateapp;

import com.hibernateapp.dao.UserDAO;
import com.hibernateapp.dao.UserDAOImpl;
import com.hibernateapp.exception.*;
import com.hibernateapp.model.User;
import com.hibernateapp.service.UserService;

import java.util.List;
import java.util.Scanner;

public class HibernateApplication {

    public static void main(String[] args) {
        HibernateApplication hibernateApplication = new HibernateApplication();
        UserDAO userDAO = new UserDAOImpl();
        UserService userService = new UserService(userDAO);
        while (true){
            try {
                hibernateApplication.run(userService);
            }
            catch (UserNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void run(UserService userService){
        Scanner sc = new Scanner(System.in);

            System.out.println("""
                    Введите в консоль слово:
                    get - Получить пользователя по id
                    getall - Получить список всех пользователей
                    post - Сохранить нового пользователя
                    delete - Удалить пользователя по id
                    put - Обновить информацию пользователя
                    """);
            String input = sc.nextLine();
            switch (input){
                case "get" -> {
                    System.out.println("Введите id пользователя");
                    long id = sc.nextLong();
                    System.out.println(userService.findUser(id));
                }
                case "getall" -> {
                    List<User> users = userService.findAllUsers();
                    for(User user : users){
                        System.out.println(user);
                    }
                }
                case "post" -> {
                    System.out.println("""
                            Введите данные пользователя через пробел:
                            [Имя] [Почта] [Возраст - целое число]
                            """);
                    String[] userData = sc.nextLine().split(" ");
                    User user = new User();
                    user.setName(userData[0]);
                    user.setEmail(userData[1]);
                    user.setAge(Integer.parseInt(userData[2]));
                    User savedUser = userService.saveUser(user);
                    System.out.printf("Сохранен пользователь:\n%s\ng", savedUser);
                }
                case "delete" -> {
                    System.out.println("Введите id пользователя");
                    long id = sc.nextLong();
                    userService.deleteUser(id);
                    System.out.printf("Пользователь с id %d удален", id);
                }
                case "put" -> {
                    System.out.println("""
                            Введите данные пользователя через пробел:
                            [ID] [Имя] [Почта] [Возраст - целое число]
                            * ID - id того, чьи данные хотите обновить
                            * Имя, возраст, почта - новые данные для пользователя
                            """);
                    String[] userData = sc.nextLine().split(" ");
                    User user = new User();
                    long id = Long.parseLong(userData[0]);
                    user.setName(userData[1]);
                    user.setEmail(userData[2]);
                    user.setAge(Integer.parseInt(userData[3]));
                    User updatedUser = userService.updateUser(id, user);
                    System.out.printf("Обновленный пользователь:\n%s", updatedUser);
                }
            }
    }
}
