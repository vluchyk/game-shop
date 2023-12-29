package com.gmail.luchyk.viktoriia.service.menu;

import com.gmail.luchyk.viktoriia.enums.AccountMenu;
import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Scanner;

@AllArgsConstructor
@Data
public class AccountMenuService {
    private Scanner scanner;
    private User user;

    public void menu() {
        System.out.println(Arrays.toString(AccountMenu.values()));
    }

    public Account create() {
        Account.AccountBuilder builder = Account.builder();
        System.out.println(Message.ACCOUNT_TYPE.getMessage());
        builder.type(scanner.next());
        builder.user(user);
        return builder.build();
    }

    public void view() {
        System.out.println(Message.USER_ACCOUNT.getMessage());
    }

    public double topUp() {
        System.out.println(Message.ACCOUNT_TOP_UP.getMessage());
        return scanner.nextDouble();
    }
}
