package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.exception.AccountException;
import com.gmail.luchyk.viktoriia.exception.GameException;
import com.gmail.luchyk.viktoriia.exception.UserException;
import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.model.Game;
import com.gmail.luchyk.viktoriia.model.Purchase;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;
import com.gmail.luchyk.viktoriia.repository.dao.GameRepository;
import com.gmail.luchyk.viktoriia.repository.dao.PurchaseRepository;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
import dbConfiguration.H2Connector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

class PurchaseRepositoryImplTest {
    private Connection connection;
    private User actualUser;
    private Purchase purchase;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private GameRepository gameRepository;
    private PurchaseRepository purchaseRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Game game = Game.builder()
            .name("Doom")
            .released(LocalDate.parse("01/05/1990", formatter))
            .rating(8)
            .cost(5.25)
            .description("Description of the Doom")
            .build();
    private User user = User.builder()
            .fullName("Jane Smith")
            .login("jsmith")
            .password("qwerty")
            .birthDate(LocalDate.parse("15/05/1990", formatter))
            .build();

    private Account account = Account.builder()
            .amount(500.25)
            .type("VISA")
            .user(user)
            .build();

    @BeforeEach
    public void init() throws SQLException, IOException, ClassNotFoundException, UserException, AccountException, GameException {
        connection = H2Connector.get();
        userRepository = new UserRepositoryImpl(connection);
        accountRepository = new AccountRepositoryImpl(connection);
        gameRepository = new GameRepositoryImpl(connection);
        purchaseRepository = new PurchaseRepositoryImpl(accountRepository, gameRepository, connection);

        actualUser = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        user.setId(actualUser.getId());

        Account actualAccount = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        account.setId(actualAccount.getId());

        Game actualGame = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));

        purchase = new Purchase(actualUser, actualGame);
    }

    @AfterEach
    public void end() throws SQLException {
        connection.close();
    }

    @Test
    public void createTest() {
        Purchase result = purchaseRepository.create(purchase).orElseThrow();
        List<Purchase> actualPurchases = purchaseRepository.read(actualUser);

        Assertions.assertEquals(result, actualPurchases.get(0));
    }

    @Test
    public void readTest() {
        Purchase actual = purchaseRepository.create(purchase).orElseThrow();
        List<Purchase> resultPurchases = purchaseRepository.read(actualUser);

        Assertions.assertEquals(resultPurchases.get(0), actual);
    }

    @Test
    public void deleteTest() {
        int expected = 0;
        Purchase actualPurchase = purchaseRepository.create(purchase).orElseThrow();
        purchaseRepository.delete(actualPurchase);

        Assertions.assertEquals(purchaseRepository.read(actualUser).size(), expected);
    }
}