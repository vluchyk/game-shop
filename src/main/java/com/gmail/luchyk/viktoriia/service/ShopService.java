package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.connection.postgresqlSingleton;
import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.repository.AccountRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.GameRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.PurchaseRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.UserRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;
import com.gmail.luchyk.viktoriia.repository.dao.GameRepository;
import com.gmail.luchyk.viktoriia.repository.dao.PurchaseRepository;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
import com.gmail.luchyk.viktoriia.service.menu.AccountMenuService;
import com.gmail.luchyk.viktoriia.service.menu.GameMenuService;
import com.gmail.luchyk.viktoriia.service.menu.UserMenuService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ShopService {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        try {
            Connection connection = postgresqlSingleton.getConnection();

            UserRepository userRepository = new UserRepositoryImpl(connection);
            UserMenuService userMenuService = new UserMenuService(scanner);
            UserService userService = new UserService(userRepository, userMenuService);

            userMenuRun(userService);

            AccountRepository accountRepository = new AccountRepositoryImpl(connection);
            AccountMenuService accountMenuService = new AccountMenuService(scanner, userService.getUser());
            AccountService accountService = new AccountService(accountRepository, accountMenuService);

            GameRepository gameRepository = new GameRepositoryImpl(connection);
            GameMenuService gameMenuService = new GameMenuService(scanner);

            PurchaseRepository purchaseRepository = new PurchaseRepositoryImpl(accountRepository, gameRepository, connection);
            GameService gameService = new GameService(purchaseRepository, gameMenuService, userService.getUser());

            accountMenuRun(accountService, gameService);

            connection.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

    public void userMenuRun(UserService userService) {
        String command;
        do {
            System.out.println();
            System.out.println(Message.MAIN_MENU.getMessage().toUpperCase());
            userService.getUserMenuService().menu();
            System.out.println(Message.WHAT_TO_DO.getMessage());
            command = userService.getUserMenuService().getScanner().next().toLowerCase();
            switch (command) {
                case "r" -> userService.register();
                case "i" -> userService.login();
                case "e" -> userService.exit();
                default -> command = "r";
            }
        } while ("r".equals(command));
    }

    public void accountMenuRun(AccountService accountService, GameService gameService) {
        String command;
        do {
            System.out.println();
            System.out.println(Message.ACCOUNT_MENU.getMessage().toUpperCase());
            accountService.getAccountMenuService().menu();
            System.out.println(Message.WHAT_TO_DO.getMessage());
            command = accountService.getAccountMenuService().getScanner().next().toLowerCase();
            switch (command) {
                case "c" -> accountService.create();
                case "v" -> accountService.view();
                case "u" -> accountService.topUp();
                case "n" -> gameMenuRun(accountService, gameService);
            }
        } while (!"n".equals(command));
    }

    public void gameMenuRun(AccountService accountService, GameService gameService) {
        String command;
        do {
            System.out.println();
            System.out.println(Message.GAME_MENU.getMessage().toUpperCase());
            gameService.getGameMenuService().menu();
            System.out.println(Message.WHAT_TO_DO.getMessage());
            command = gameService.getGameMenuService().getScanner().next().toLowerCase();
            switch (command) {
                case "v" -> gameService.view();
                case "m" -> gameService.viewMy();
                case "b" -> gameService.buy();
                case "a" -> accountMenuRun(accountService, gameService);
                case "e" -> gameService.exit();
            }
        } while (!"a".equals(command));
    }
}
