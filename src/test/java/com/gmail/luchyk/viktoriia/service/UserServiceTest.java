package com.gmail.luchyk.viktoriia.service;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.exception.UserException;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.UserRepositoryImpl;
import com.gmail.luchyk.viktoriia.repository.dao.UserRepository;
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

public class UserServiceTest {
    private Connection connection;
    private UserRepository userRepository;
    private Scanner scanner;
    private UserMenuService userMenuService;
    private UserService userService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private User user = User.builder()
            .fullName("JaneSmith")
            .login("jsmith")
            .password("qwerty")
            .birthDate(LocalDate.parse("15/05/1990", formatter))
            .build();

    @Rule
    public final TextFromStandardInputStream systemIn = emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Before
    public void init() throws SQLException, IOException, ClassNotFoundException {
        connection = H2Connector.get();
        userRepository = new UserRepositoryImpl(connection);
        scanner = new Scanner(System.in);
//        scanner.useDelimiter("\n");
        userMenuService = new UserMenuService(scanner);
        userService = new UserService(userRepository, userMenuService);
    }

    @After
    public void end() throws SQLException {
        connection.close();
    }

    @Test
    public void registerTest() {
        systemIn.provideLines(user.getFullName(), user.getLogin(), user.getBirthDate().format(formatter), user.getPassword());
        userService.register();
        boolean result = userRepository.existLogin(user);

        Assert.assertTrue(result);
    }

    @Test
    public void loginTest() throws UserException {
        userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage()));
        systemIn.provideLines(user.getLogin(), user.getPassword());
        userService.login();
        String menuText = "Username: Password: ";

        Assert.assertEquals(menuText + Message.WELCOME.getMessage(), systemOutRule.getLog().trim());
    }
}