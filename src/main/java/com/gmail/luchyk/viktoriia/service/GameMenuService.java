package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.GameMenu;
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
        System.out.println("Available games are: ");
    }

    public String buy() {
        System.out.println("Enter the name of the game you want to buy: ");
        return scanner.next();
    }
}
