package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserMenu {
    REGISTER("Register (R)", "r"),
    SIGNIN("Sign In (I)", "i"),
    SIGNOUT("Sign Out (O)", "o"),
    EXIT("Exit (E)", "e");

    private String action;
    private String command;

    @Override
    public String toString() {
        return action;
    }
}