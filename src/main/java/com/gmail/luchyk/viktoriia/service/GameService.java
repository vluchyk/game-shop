package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.exception.GameException;
import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.model.Game;
import com.gmail.luchyk.viktoriia.model.Purchase;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.PurchaseRepositoryImpl;
import com.gmail.luchyk.viktoriia.service.menu.GameMenuService;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
public class GameService {
    private PurchaseRepositoryImpl purchaseRepository;
    private GameMenuService gameMenuService;
    private User user;
    private Account account;
    private Game game;

    public GameService(PurchaseRepositoryImpl purchaseRepository, GameMenuService gameMenuService, User user) {
        this.purchaseRepository = purchaseRepository;
        this.gameMenuService = gameMenuService;
        this.user = user;
    }

    public void view() {
        this.gameMenuService.view();
        List<Game> games = this.purchaseRepository.getGameRepository().readAll();
        if (games.isEmpty())
            System.out.println(Message.NO_GAMES.getMessage());
        else
            games.forEach(System.out::println);
    }

    public void viewMy() {
        this.gameMenuService.viewMy();
        List<Game> games = this.purchaseRepository.getGameRepository().readBy(this.user);
        if (games.isEmpty())
            System.out.println(Message.NO_GAMES.getMessage());
        else
            games.forEach(System.out::println);
    }

    public void buy() {
        String name = this.gameMenuService.buy();
        try {
            this.game = this.purchaseRepository.getGameRepository()
                    .readByName(name)
                    .orElseThrow(() -> new GameException(Message.GAME_NOT_FOUND.getMessage()));
            this.account = this.purchaseRepository.getAccountRepository()
                    .readByUser(this.user)
                    .orElseThrow(() -> new GameException(Message.ACCOUNT_DOES_NOT_EXIST.getMessage()));
            double balance = this.account.getAmount() - this.game.getCost();
            balance = (new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP)).doubleValue();
            if (balance >= 0) {
                Purchase purchase = new Purchase(this.account.getUser(), this.game);
                this.purchaseRepository.create(purchase);
                this.account.setAmount(balance);
                if (this.purchaseRepository.getAccountRepository().update(this.account) != 0) {
                    System.out.println(Message.GAME_PURCHASED_SUCCESSFULLY.getMessage());
                }
            } else {
                System.out.println(Message.ACCOUNT_NOT_ENOUGH_MONEY.getMessage());
            }
        } catch (GameException e) {
            System.out.println(e);
            System.out.println(Message.TRY_AGAIN.getMessage());
        }
    }

    public void exit() {
        System.exit(0);
    }
}