package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountMenu {
    CREATE("Create (C)", "c"),
    VIEW("View (V)", "v"),
    REPLENISHMENT("Top Up (U)", "u"),
    NEXT("Next (N)", "n");

    private String action;
    private String command;

    @Override
    public String toString() {
        return action;
    }
}