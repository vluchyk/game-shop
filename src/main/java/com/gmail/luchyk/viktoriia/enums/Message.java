package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Message {
    WHAT_TO_DO("What do you want to do?"),
    USERNAME_EXISTS("A user with this username already exists in the system."),
    USERNAME_PASSWORD_INCORRECT("The username or password is incorrect."),
    USER_REGISTERED_SUCCESSFULLY("The user is successfully registered."),
    WELCOME("Welcome!"),
    TRY_AGAIN("Please, try again."),
    USER_FULL_NAME("Full Name: "),
    USERNAME("Username: "),
    USER_BIRTH_DAY("Date of Birth (dd/mm/yyyy): "),
    USER_PASSWORD("Password: "),
    USER_ACCOUNT("User account:"),
    TOP_UP_ACCOUNT("Enter the amount to top up the account: "),
    AVAILABLE_GAMES("Available games are: "),
    GAME_TO_BUY("Enter the name of the game you want to buy: "),
    LOGOUT("Are you sure you want to log out? (Y/N)");

    private String message;
}
