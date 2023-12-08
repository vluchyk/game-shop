package com.gmail.luchyk.viktoriia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameMenu {
    VIEW("View All Games (V)", "v"),
    VIEW_MY("View My Games (M)", "m"),
    BUY("Buy a Game (B)", "b"),
    CLOSE_MENU("Close Menu (C)", "c");

    private String action;
    private String command;

    @Override
    public String toString() {
        return action;
    }
}