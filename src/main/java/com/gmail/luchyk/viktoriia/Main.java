package com.gmail.luchyk.viktoriia;

import com.gmail.luchyk.viktoriia.service.ShopService;

public class Main {
    public static void main(String[] args) {
        ShopService shopService = new ShopService();
        shopService.run();
    }
}