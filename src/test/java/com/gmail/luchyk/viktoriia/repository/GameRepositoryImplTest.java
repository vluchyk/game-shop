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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class GameRepositoryImplTest {
    private Connection connection;
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
    public void init() throws SQLException, IOException, ClassNotFoundException {
        connection = H2Connector.get();
        userRepository = new UserRepositoryImpl(connection);
        accountRepository = new AccountRepositoryImpl(connection);
        gameRepository = new GameRepositoryImpl(connection);
        purchaseRepository = new PurchaseRepositoryImpl(accountRepository, gameRepository, connection);
    }

    @AfterEach
    public void end() throws SQLException {
        connection.close();
    }

    @Test
    public void createTest() throws GameException {
        Game result = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_FOUND.getMessage()));
        Game actual = gameRepository.read(result.getId()).orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void createNullGameTest() {
        Game game = null;

        GameException thrown = Assertions.assertThrows(GameException.class, () -> gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage())), "GameException was expected.");
        Assertions.assertEquals(Message.GAME_NOT_CREATED.getMessage(), thrown.getMessage());
    }

    @Test
    public void readTest() throws GameException {
        Game actual = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));
        Game result = gameRepository.read(actual.getId()).orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void readByNonExistentIdTest() {
        int nonExistingId = 999999;
        Optional<Game> actual = Optional.empty();
        Optional<Game> result = gameRepository.read(nonExistingId);

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void updateTest() throws GameException {
        int expected = 1;
        Game saved = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));
        saved.setName("Doom II");
        saved.setReleased(LocalDate.parse("01/07/1995", formatter));
        saved.setRating(10);
        saved.setCost(5);
        saved.setDescription("Description of the Doom II");

        int result = gameRepository.update(saved);
        Game updated = gameRepository.read(saved.getId()).orElseThrow();

        Assertions.assertEquals(result, expected);
        Assertions.assertEquals(updated, saved);
    }

    @Test
    public void updateNonExistingGameTest() {
        int expected = 0;
        int result = gameRepository.update(game);

        Assertions.assertEquals(result, expected);
    }

    @Test
    public void deleteTest() throws GameException {
        Game actual = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));
        gameRepository.delete(actual.getId());

        Assertions.assertNull(gameRepository.read(actual.getId()).orElse(null));
    }

    @Test
    public void deleteNonExistingUserTest() {
        int nonExistingId = 999999;
        boolean result = gameRepository.delete(nonExistingId);

        Assertions.assertFalse(result);
    }

    @Test
    public void readAll() throws GameException {
        List<Game> expectedGames = new ArrayList<>();
        Game actual = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));
        expectedGames.add(actual);

        game = Game.builder()
                .name("Tetris")
                .released(LocalDate.parse("01/07/1992", formatter))
                .rating(10)
                .cost(3.25)
                .description("Description of the Tetris")
                .build();
        actual = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));
        expectedGames.add(actual);

        game = Game.builder()
                .name("Brawl Stars")
                .released(LocalDate.parse("25/11/2010", formatter))
                .rating(11)
                .cost(7)
                .description("Description of the Brawl Stars")
                .build();
        actual = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));
        expectedGames.add(actual);

        List<Game> resultGames = gameRepository.readAll();

        Assertions.assertEquals(resultGames.size(), expectedGames.size());
        Assertions.assertEquals(resultGames, expectedGames);
    }

    @Test
    public void readByUserTest() throws UserException, AccountException, GameException {
        int expected = 1;
        User actualUser = userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        user.setId(actualUser.getId());

        Account actualAccount = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        account.setId(actualAccount.getId());

        Game actualGame = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));

        Purchase purchase = new Purchase(actualUser, actualGame);
        purchaseRepository.create(purchase).orElseThrow();

        List<Game> result = gameRepository.readBy(actualUser);

        Assertions.assertEquals(result.size(), expected);
        Assertions.assertEquals(result.get(0), game);
    }

    @Test
    public void readByNameTest() throws GameException {
        Game actual = gameRepository.create(game).orElseThrow(() -> new GameException(Message.GAME_NOT_CREATED.getMessage()));
        Game result = gameRepository.readByName("Doom").orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void readByNameNoneTest() {
        Game result = gameRepository.readByName("Doom").orElse(null);

        Assertions.assertNull(result);
    }
}