package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountMenu {
    VIEW("View User Accounts (V)", "v"),
    REPLENISHMENT("Replenishment (R)", "r");

    private String action;
    private String command;

    @Override
    public String toString() {
        return action;
    }
}