package com.gmail.luchyk.viktoriia;

import com.gmail.luchyk.viktoriia.connection.postgresqlSingleton;
import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.enums.UserMenu;
import com.gmail.luchyk.viktoriia.repository.UserRepositoryImpl;
import com.gmail.luchyk.viktoriia.service.UserService;
import com.gmail.luchyk.viktoriia.service.menu.UserMenuService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        try {
            Connection connection = postgresqlSingleton.getConnection();
            UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl(connection);
            UserMenuService userMenuService = new UserMenuService(scanner);
            UserService userService = new UserService(userRepositoryImpl, userMenuService);

            execute(userMenuService, userService, scanner);
        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
        scanner.close();
    }

    private static void execute(UserMenuService userMenuService, UserService userService, Scanner scanner){
        System.out.println(Message.WHAT_TO_DO.getMessage());
        userMenuService.menu();
        String command;
        command = scanner.next().toLowerCase();
        switch (command) {
            case "r" -> userService.register();
            case "i" -> userService.login();
            case "o" -> userService.logout();
            case "e" -> userService.exit();
        }
    }
}
