package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserMenu {
    REGISTER("Register (R)", "r"),
    SIGNIN("Sign In (I)", "i"),
    EXIT("Exit (E)", "e");

    private final String action;
    private final String command;

    @Override
    public String toString() {
        return action;
    }
}