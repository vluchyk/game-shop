package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.model.Game;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.AccountRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.GameRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.PurchaseRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.UserRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;
import com.gmail.luchyk.viktoriia.repository.dao.GameRepository;
import com.gmail.luchyk.viktoriia.repository.dao.PurchaseRepository;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
import com.gmail.luchyk.viktoriia.service.menu.GameMenuService;
import com.gmail.luchyk.viktoriia.service.menu.UserMenuService;
import dbConfiguration.H2Connector;
import org.junit.*;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class GameServiceTest {
    private Connection connection;
    private Statement statement;
    private Scanner scanner;
    private UserRepository userRepository;
    private UserMenuService userMenuService;
    private UserService userService;
    private AccountRepository accountRepository;
    private GameRepository gameRepository;
    private PurchaseRepository purchaseRepository;
    private GameMenuService gameMenuService;
    private GameService gameService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private User user = User.builder()
            .fullName("JaneSmith")
            .login("jsmith")
            .password("qwerty")
            .birthDate(LocalDate.parse("15/05/1990", formatter))
            .build();
    private Account account = Account.builder()
            .amount(0)
            .type("VISA")
            .user(user)
            .build();
    private Game game = Game.builder()
            .name("Tetris")
            .released(LocalDate.parse("11/05/1998", formatter))
            .rating(10)
            .cost(10.25)
            .description("The description of the Tetris.")
            .build();

    @Rule
    public final TextFromStandardInputStream systemIn = emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Before
    public void init() throws SQLException, IOException, ClassNotFoundException {
        connection = H2Connector.get();
        scanner = new Scanner(System.in);

        userRepository = new UserRepositoryImpl(connection);
        userMenuService = new UserMenuService(scanner);
        userService = new UserService(userRepository, userMenuService);

        accountRepository = new AccountRepositoryImpl(connection);
        gameRepository = new GameRepositoryImpl(connection);
        purchaseRepository = new PurchaseRepositoryImpl(accountRepository, gameRepository, connection);

        gameMenuService = new GameMenuService(scanner);
        gameService = new GameService(purchaseRepository, gameMenuService, userService.getUser());

        statement = connection.createStatement();

        String initDB = new String(Files.readAllBytes(Paths.get("src/main/java/com/gmail/luchyk/viktoriia/ddl/insert_games.sql")));
        statement.execute(initDB);
    }

    @After
    public void end() throws SQLException {
        statement.close();
        connection.close();
    }

    @Test
    public void viewTest() {
        List<Game> expected = new ArrayList<>();
        expected.add(Game.builder().id(1).name("Angry Birds").released(LocalDate.parse("01/01/2005", formatter)).rating(12).cost(4.99).description("Description of the Angry Birds").build());
        expected.add(Game.builder().id(2).name("Tetris").released(LocalDate.parse("25/02/2000", formatter)).rating(10).cost(1.25).description("Description of the Tetris").build());
        expected.add(Game.builder().id(3).name("Brawl Stars").released(LocalDate.parse("15/06/2020", formatter)).rating(11).cost(5.59).description("Description of the Brawl Stars").build());
        expected.add(Game.builder().id(4).name("Super Bear Adventure").released(LocalDate.parse("25/08/2022", formatter)).rating(11).cost(2.25).description("Description of the Super Bear Adventure").build());
        expected.add(Game.builder().id(5).name("Doom").released(LocalDate.parse("13/04/1995", formatter)).rating(10).cost(1.15).description("Description of the Doom").build());

        gameService.view();
        StringBuilder menuText = new StringBuilder(Message.AVAILABLE_GAMES.getMessage());
        for (Game each : expected) {
            menuText.append("\r\n").append(each.toString());
        }

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }
}