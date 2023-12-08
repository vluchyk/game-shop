package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Message {
    MAIN_MENU("Main Menu: "),
    ACCOUNT_MENU("Account Menu: "),
    GAME_MENU("Game Menu: "),

    WHAT_TO_DO("What do you want to do?"),
    WELCOME("Welcome!"),
    TRY_AGAIN("Please, try again."),

    USERNAME_EXISTS("A user with this username already exists in the system."),
    USERNAME_PASSWORD_INCORRECT("The username or password is incorrect."),
    USER_REGISTERED_SUCCESSFULLY("The user is successfully registered."),
    USER_FULL_NAME("Full Name: "),
    USERNAME("Username: "),
    USER_BIRTH_DAY("Date of Birth (dd/mm/yyyy): "),
    USER_PASSWORD("Password: "),
    USER_ACCOUNT("User account:"),

    ACCOUNT_TOP_UP("Enter the amount to top up the account: "),
    ACCOUNT_TYPE("Enter the type of the amount: "),
    ACCOUNT_EXISTS("An account already exists in the system."),
    ACCOUNT_CREATED_SUCCESSFULLY("The account is successfully created."),
    ACCOUNT_TOPPED_UP_SUCCESSFULLY("The account is successfully topped up."),
    ACCOUNT_NEED_CREATE("Please, create an account for the current user."),
    ACCOUNT_NOT_ENOUGH_MONEY("There is not enough money in the account."),

    AVAILABLE_GAMES("Available games are: "),
    MY_GAMES("My games are: "),
    NO_GAMES("None"),
    GAME_TO_BUY("Enter the name of the game you want to buy: "),

    LOGOUT("Are you sure you want to log out? (Y/N)");

    private String message;
}
