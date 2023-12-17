package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.AccountRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.UserRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
import com.gmail.luchyk.viktoriia.service.menu.AccountMenuService;
import com.gmail.luchyk.viktoriia.service.menu.UserMenuService;
import dbConfiguration.H2Connector;
import org.junit.*;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class AccountServiceTest {
    private Connection connection;
    private Scanner scanner;
    private UserRepository userRepository;
    private UserMenuService userMenuService;
    private UserService userService;
    private AccountRepository accountRepository;
    private AccountMenuService accountMenuService;
    private AccountService accountService;
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
    }

    @After
    public void end() throws SQLException {
        connection.close();
    }

    @Test
    public void createTest() {
        account.setId(1);
        account.getUser().setId(1);

        systemIn.provideLines(account.getType());
        accountService.create();
        String menuText = "Full Name: Username: Date of Birth (dd/mm/yyyy): Password: The user is successfully registered.\r\n";
        menuText += Message.ACCOUNT_TYPE.getMessage() + "\r\n";

        Account result = accountRepository.read(account.getId()).orElseThrow();

        Assert.assertEquals(account, result);
        Assert.assertEquals(menuText + Message.ACCOUNT_CREATED_SUCCESSFULLY.getMessage(), systemOutRule.getLog().trim());
    }

    @Test
    public void createExistingTest() {
        systemIn.provideLines(account.getType());
        accountService.create();
        systemIn.provideLines(account.getType());
        accountService.create();
        String menuText = "Full Name: Username: Date of Birth (dd/mm/yyyy): Password: The user is successfully registered.\r\n";
        menuText += Message.ACCOUNT_TYPE.getMessage() + "\r\n";
        menuText += Message.ACCOUNT_CREATED_SUCCESSFULLY.getMessage() + "\r\n";
        menuText += Message.ACCOUNT_TYPE.getMessage() + "\r\n";

        Assert.assertEquals(menuText + Message.ACCOUNT_EXISTS.getMessage(), systemOutRule.getLog().trim());
    }

    @Test
    public void viewTest() {
        account.setId(1);
        account.getUser().setId(1);

        systemIn.provideLines(account.getType());
        accountService.create();
        accountService.view();

        String menuText = "Full Name: Username: Date of Birth (dd/mm/yyyy): Password: The user is successfully registered.\r\n";
        menuText += Message.ACCOUNT_TYPE.getMessage() + "\r\n";
        menuText += Message.ACCOUNT_CREATED_SUCCESSFULLY.getMessage() + "\r\n";
        menuText += Message.USER_ACCOUNT.getMessage() + "\r\n";

        Assert.assertEquals(menuText + account.toString(), systemOutRule.getLog().trim());
    }

    @Test
    public void viewNoneTest() {
        accountService.view();
        String menuText = "Full Name: Username: Date of Birth (dd/mm/yyyy): Password: The user is successfully registered.\r\n";
        menuText += Message.USER_ACCOUNT.getMessage() + "\r\n";
        menuText += "com.gmail.luchyk.viktoriia.exception.AccountException: ";

        Assert.assertEquals(menuText + Message.ACCOUNT_DOES_NOT_EXIST.getMessage(), systemOutRule.getLog().trim());
    }

    @Test
    public void topUpTest() {
        account.setId(1);
        account.getUser().setId(1);
        account.setAmount(100.25);

        systemIn.provideLines(account.getType());
        accountService.create();
        systemIn.provideLines(String.valueOf(account.getAmount()));
        accountService.topUp();

        Account result = accountRepository.read(account.getId()).orElseThrow();

        Assert.assertEquals(account, result);
    }
}