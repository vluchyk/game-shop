package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameMenu {
    VIEW("View Games (V)", "v"),
    BUY("Buy a Game (B)", "b");

    private String action;
    private String command;

    @Override
    public String toString() {
        return action;
    }
}