package com.gmail.luchyk.viktoriia;

import com.gmail.luchyk.viktoriia.connection.postgresqlSingleton;
import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.repository.AccountRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.UserRepositoryImpl;
import com.gmail.luchyk.viktoriia.service.AccountService;
import com.gmail.luchyk.viktoriia.service.UserService;
import com.gmail.luchyk.viktoriia.service.menu.AccountMenuService;
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

            execute(userService);

            AccountRepositoryImpl accountRepositoryImpl = new AccountRepositoryImpl(connection);
            AccountMenuService accountMenuService = new AccountMenuService(scanner, userService.getUser());
            AccountService accountService = new AccountService(accountRepositoryImpl, accountMenuService);

            execute(accountService);

        } catch (SQLException e) {
            throw new RuntimeException(e); // todo
        }
        scanner.close();
    }

    private static void execute(UserService userService) {
        String command;
        do {
            System.out.print(Message.MAIN_MENU.getMessage().toUpperCase());
            userService.getUserMenuService().menu();
            System.out.println(Message.WHAT_TO_DO.getMessage());
            command = userService.getUserMenuService().getScanner().next().toLowerCase();
            switch (command) {
                case "r" -> userService.register();
                case "i" -> userService.login();
//                case "o" -> userService.logout();
                case "e" -> userService.exit();
            }
        } while ("r".equals(command));
    }

    private static void execute(AccountService accountService) {
        String command;
        do {
            System.out.print(Message.ACCOUNT_MENU.getMessage().toUpperCase());
            accountService.getAccountMenuService().menu();
            System.out.println(Message.WHAT_TO_DO.getMessage());
            command = accountService.getAccountMenuService().getScanner().next().toLowerCase();
            switch (command) {
                case "c" -> accountService.create();
                case "v" -> accountService.view();
                case "u" -> accountService.topUp();
                case "n" -> accountService.next();
            }
        } while (!"n".equals(command));
    }
}
