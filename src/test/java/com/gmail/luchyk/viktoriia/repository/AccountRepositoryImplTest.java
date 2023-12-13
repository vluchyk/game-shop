package com.gmail.luchyk.viktoriia.repository;

import com.gmail.luchyk.viktoriia.enums.Message;
import com.gmail.luchyk.viktoriia.exception.AccountException;
import com.gmail.luchyk.viktoriia.exception.UserException;
import com.gmail.luchyk.viktoriia.model.Account;
import com.gmail.luchyk.viktoriia.model.User;
import com.gmail.luchyk.viktoriia.repository.dao.AccountRepository;
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
import java.util.Optional;

class AccountRepositoryImplTest {
    private Connection connection;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private User user = User.builder().fullName("Jane Smith").login("jsmith").password("qwerty").birthDate(LocalDate.parse("15/05/1990", formatter)).build();
    private Account account = Account.builder().amount(500.25).type("VISA").user(user).build();

    @BeforeEach
    public void init() throws SQLException, IOException, ClassNotFoundException, UserException {
        connection = H2Connector.get();
        userRepository = new UserRepositoryImpl(connection);
        accountRepository = new AccountRepositoryImpl(connection);

        account.setUser(userRepository.create(user).orElseThrow(() -> new UserException(Message.USER_NOT_CREATED.getMessage())));
    }

    @AfterEach
    public void end() throws SQLException {
        connection.close();
    }

    @Test
    public void createTest() throws AccountException {
        Account result = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        Account actual = accountRepository.read(result.getId()).orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void createNullAccountTest() {
        Account account = null;

        AccountException thrown = Assertions.assertThrows(AccountException.class, () -> accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage())), "AccountException was expected.");
        Assertions.assertEquals(Message.ACCOUNT_NOT_CREATED.getMessage(), thrown.getMessage());
    }

    @Test
    public void readTest() throws AccountException {
        Account actual = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        Account result = accountRepository.read(account.getId()).orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void readByNonExistentIdTest() {
        int nonExistingId = 999999;
        Optional<Account> actual = Optional.empty();
        Optional<Account> result = accountRepository.read(nonExistingId);

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void updateTest() throws AccountException {
        int expected = 1;
        Account saved = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        saved.setAmount(255);
        saved.setType("MASTERCARD");

        int result = accountRepository.update(saved);
        Account updated = accountRepository.read(saved.getId()).orElseThrow();

        Assertions.assertEquals(result, expected);
        Assertions.assertEquals(updated, saved);
    }

    @Test
    public void updateNonExistingAccountTest() {
        int expected = 0;
        int result = accountRepository.update(account);

        Assertions.assertEquals(result, expected);
    }

    @Test
    public void deleteTest() throws AccountException {
        Account actual = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        accountRepository.delete(actual.getId());

        Assertions.assertNull(accountRepository.read(actual.getId()).orElse(null));
    }

    @Test
    public void deleteNonExistingAccountTest() {
        int nonExistingId = 999999;
        boolean result = accountRepository.delete(nonExistingId);

        Assertions.assertFalse(result);
    }

    @Test
    public void existTest() throws AccountException {
        Account actual = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        boolean result = accountRepository.exist(actual);

        Assertions.assertTrue(result);
    }

    @Test
    public void existFailTest() {
        boolean result = accountRepository.exist(account);

        Assertions.assertFalse(result);
    }

    @Test
    public void readByUserTest() throws AccountException {
        Account actual = accountRepository.create(account).orElseThrow(() -> new AccountException(Message.ACCOUNT_NOT_CREATED.getMessage()));
        Account result = accountRepository.readByUser(user).orElseThrow();

        Assertions.assertEquals(result, actual);
    }

    @Test
    public void readByUserFailTest() {
        Account result = accountRepository.readByUser(user).orElse(null);

        Assertions.assertNull(result);
    }
}