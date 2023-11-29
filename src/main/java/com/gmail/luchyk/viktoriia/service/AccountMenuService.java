package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.AccountMenu;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Scanner;

@AllArgsConstructor
public class AccountMenuService {
    private Scanner scanner;

    public void menu() {
        System.out.println(Arrays.toString(AccountMenu.values()));
    }

    public void view() {
        System.out.println("User account: ");
    }

    public double replenish() {
        System.out.println("Enter the amount to top up the account: ");
        return scanner.nextDouble();
    }
}
