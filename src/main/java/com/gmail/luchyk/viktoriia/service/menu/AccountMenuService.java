package com.gmail.luchyk.viktoriia.service.menu;

import com.gmail.luchyk.viktoriia.enums.AccountMenu;
import com.gmail.luchyk.viktoriia.enums.Message;
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
        System.out.println(Message.USER_ACCOUNT.getMessage());
    }

    public double replenish() {
        System.out.println(Message.TOP_UP_ACCOUNT.getMessage());
        return scanner.nextDouble();
    }
}
