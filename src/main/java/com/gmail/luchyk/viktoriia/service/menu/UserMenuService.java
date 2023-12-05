package com.gmail.luchyk.viktoriia.service.menu;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.enums.UserMenu;
import com.gmail.luchyk.viktoriia.model.User;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

@AllArgsConstructor
public class UserMenuService {
    private Scanner scanner;

    public void menu() {
        System.out.println(Arrays.toString(UserMenu.values()));
    }

    public User register() {
        User.UserBuilder builder = User.builder();
        System.out.print(Message.USER_FULL_NAME.getMessage());
        builder.fullName(scanner.next());
        System.out.print(Message.USERNAME.getMessage());
        builder.login(scanner.next());
        System.out.print(Message.USER_BIRTH_DAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        builder.birthDate(LocalDate.parse(scanner.next(), formatter));
        System.out.print(Message.USER_PASSWORD.getMessage());
        builder.password(scanner.next());
        return builder.build();
    }

    public User signIn() {
        User.UserBuilder builder = User.builder();
        System.out.print(Message.USERNAME.getMessage());
        builder.login(scanner.next());
        System.out.print(Message.USER_PASSWORD.getMessage());
        builder.password(scanner.next());
        return builder.build();
    }

    public String signOut() {
        System.out.println(Message.LOGOUT.getMessage());
        return scanner.next();
    }
}