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
import com.gmail.luchyk.viktoriia.service.menu.AccountMenuService;
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
    private AccountMenuService accountMenuService;
    private AccountService accountService;
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
            .released(LocalDate.parse("25/02/2000", formatter))
            .rating(10)
            .cost(1.25)
            .description("Description of the Tetris")
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

        systemIn.provideLines(user.getFullName(), user.getLogin(), user.getBirthDate().format(formatter), user.getPassword());
        userService.register();

        accountRepository = new AccountRepositoryImpl(connection);
        accountMenuService = new AccountMenuService(scanner, userService.getUser());
        accountService = new AccountService(accountRepository, accountMenuService);

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
        StringBuilder menuText = new StringBuilder();
        menuText.append(displayUserMenuText());
        menuText.append(Message.AVAILABLE_GAMES.getMessage());
        for (Game each : expected) {
            menuText.append("\r\n").append(each.toString());
        }

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }

    @Test
    public void viewMyTest() {
        account.setId(1);
        account.getUser().setId(1);
        game.setId(2);

        systemIn.provideLines(account.getType());
        accountService.create();
        systemIn.provideLines(String.valueOf(150.25));
        accountService.topUp();
        systemIn.provideLines(game.getName());
        gameService.buy();
        gameService.viewMy();
        StringBuilder menuText = new StringBuilder();
        menuText.append(displayUserMenuText());
        menuText.append(displayAccountMenuText());
        menuText.append(displayPurchaseMenuText()).append("\r\n");
        menuText.append(Message.MY_GAMES.getMessage()).append("\r\n");
        menuText.append(game.toString());

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }

    @Test
    public void viewMyNoneTest() {
        gameService.viewMy();
        StringBuilder menuText = new StringBuilder();
        menuText.append(displayUserMenuText());
        menuText.append(Message.MY_GAMES.getMessage()).append("\r\n");
        menuText.append(Message.NO_GAMES.getMessage());

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }

    @Test
    public void buyTest() {
        account.setId(1);
        account.getUser().setId(1);

        systemIn.provideLines(account.getType());
        accountService.create();
        systemIn.provideLines(String.valueOf(150.25));
        accountService.topUp();
        systemIn.provideLines(game.getName());
        gameService.buy();
        StringBuilder menuText = new StringBuilder();
        menuText.append(displayUserMenuText());
        menuText.append(displayAccountMenuText());
        menuText.append(displayPurchaseMenuText());

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }

    @Test
    public void buyNotEnoughMoneyTest() {
        account.setId(1);
        account.getUser().setId(1);

        systemIn.provideLines(account.getType());
        accountService.create();
        systemIn.provideLines(String.valueOf(0.5));
        accountService.topUp();
        systemIn.provideLines(game.getName());
        gameService.buy();
        StringBuilder menuText = new StringBuilder();
        menuText.append(displayUserMenuText());
        menuText.append(displayAccountMenuText());
        menuText.append(Message.GAME_TO_BUY.getMessage()).append("\r\n");
        menuText.append(Message.ACCOUNT_NOT_ENOUGH_MONEY.getMessage());

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }

    @Test
    public void buyGameNotFoundTest() {
        account.setId(1);
        account.getUser().setId(1);

        systemIn.provideLines(account.getType());
        accountService.create();
        systemIn.provideLines(String.valueOf(0.5));
        accountService.topUp();
        systemIn.provideLines("Angel");
        gameService.buy();
        StringBuilder menuText = new StringBuilder();
        menuText.append(displayUserMenuText());
        menuText.append(displayAccountMenuText());
        menuText.append(Message.GAME_TO_BUY.getMessage()).append("\r\n");
        menuText.append("com.gmail.luchyk.viktoriia.exception.GameException: ");
        menuText.append(Message.GAME_NOT_FOUND.getMessage()).append("\r\n");
        menuText.append(Message.TRY_AGAIN.getMessage());

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }

    @Test
    public void buyAccountDoesNotExistTest() {
        account.setId(1);
        account.getUser().setId(1);

        systemIn.provideLines(String.valueOf(0.5));
        accountService.topUp();
        systemIn.provideLines(game.getName());
        gameService.buy();
        StringBuilder menuText = new StringBuilder();
        menuText.append(displayUserMenuText());
        menuText.append("com.gmail.luchyk.viktoriia.exception.AccountException: ").append(Message.ACCOUNT_DOES_NOT_EXIST.getMessage()).append("\r\n");
        menuText.append(Message.GAME_TO_BUY.getMessage()).append("\r\n");
        menuText.append("com.gmail.luchyk.viktoriia.exception.GameException: ");
        menuText.append(Message.ACCOUNT_DOES_NOT_EXIST.getMessage()).append("\r\n");
        menuText.append(Message.TRY_AGAIN.getMessage());

        Assert.assertEquals(menuText.toString(), systemOutRule.getLog().trim());
    }

    private String displayUserMenuText() {
        StringBuilder menuText = new StringBuilder();
        menuText.append(Message.USER_FULL_NAME.getMessage());
        menuText.append(Message.USERNAME.getMessage());
        menuText.append(Message.USER_BIRTH_DAY.getMessage());
        menuText.append(Message.USER_PASSWORD.getMessage());
        menuText.append(Message.USER_REGISTERED_SUCCESSFULLY.getMessage()).append("\r\n");
        return menuText.toString();
    }

    private String displayAccountMenuText() {
        StringBuilder menuText = new StringBuilder();
        menuText.append(Message.ACCOUNT_TYPE.getMessage()).append("\r\n");
        menuText.append(Message.ACCOUNT_CREATED_SUCCESSFULLY.getMessage()).append("\r\n");
        menuText.append(Message.ACCOUNT_TOP_UP.getMessage()).append("\r\n");
        menuText.append(Message.ACCOUNT_TOPPED_UP_SUCCESSFULLY.getMessage()).append("\r\n");
        return menuText.toString();
    }

    private String displayPurchaseMenuText() {
        StringBuilder menuText = new StringBuilder();
        menuText.append(Message.GAME_TO_BUY.getMessage()).append("\r\n");
        menuText.append(Message.GAME_PURCHASED_SUCCESSFULLY.getMessage());
        return menuText.toString();
    }
}