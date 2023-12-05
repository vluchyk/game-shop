package com.gmail.luchyk.viktoriia.service.menu;

import com.gmail.luchyk.viktoriia.enums.GameMenu;
import com.gmail.luchyk.viktoriia.enums.Message;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Scanner;

@AllArgsConstructor
public class GameMenuService {
    private Scanner scanner;

    public void menu() {
        System.out.println(Arrays.toString(GameMenu.values()));
    }

    public void view() {
        System.out.println(Message.AVAILABLE_GAMES.getMessage());
    }

    public String buy() {
        System.out.println(Message.GAME_TO_BUY.getMessage());
        return scanner.next();
    }
}
