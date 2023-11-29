package com.gmail.luchyk.viktoriia.service;

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
        System.out.println("Full Name: ");
        builder.fullName(scanner.next());
        System.out.println("Username: ");
        builder.login(scanner.next());
        System.out.println("Date of Birth (dd/mm/yyyy): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        builder.birthDate(LocalDate.parse(scanner.next(), formatter));
        return builder.build();
    }

    public String signIn() {
        System.out.println("Username: ");
        return scanner.next();
    }

    public String signOut() {
        System.out.println("Are you sure you want to log out? (Y/N)");
        return scanner.next();
    }
}